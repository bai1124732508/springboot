package com.fhzm.controller;

import com.fhzm.annotation.Log;
import com.fhzm.common.LoginCookieUtil;
import com.fhzm.common.MD5Util;
import com.fhzm.common.ReturnResult;
import com.fhzm.common.Sha1Util;
import com.fhzm.entity.generator.AuthMember;
import com.fhzm.service.AuthMemberService;
import com.fhzm.service.LoginService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Controller
@Slf4j
@Api(tags = "后台登录控制器",description="后台登录控制器",hidden=true)
public class LoginController {
    @Value("${custom.secretkey}")
    private String secretkey;

    @Autowired
    private AuthMemberService authMemberService;
    @Autowired
    private LoginService loginService;

    /**
     * 进入登录页面
     * @param map
     * @param model
     * @return
     */
    @RequestMapping(value = "/user/login",method = RequestMethod.GET)
    public String login (HashMap<String, Object> map, Model model){
        map.put("hello", "欢迎进入HTML页面");
        return "login/index";
    }

    /**
     *后台登录方法
     * @param member
     * @param model
     * @param session
     * @param response
     * @param request
     * @param online
     * @return
     */
    @Log(logType=1,operationName="后台登录方法",operationNameEn="login")
    @ApiOperation(value = "后台登录方法" ,  notes="后台登录方法")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "member", value = "登录成员账号密码信息", required = true, paramType = "query", dataType = "com.fhzm.entity.generator.AuthMember"),
            @ApiImplicitParam(name = "online", value = "保持登录两周选项", required = false, paramType = "query", dataType = "String")
    })
    @PostMapping(value = "/user/login")
    @ResponseBody
    public ReturnResult doLogin(@ModelAttribute AuthMember member, Model model, HttpSession session, HttpServletResponse response, HttpServletRequest request, String online) {
        //验证用户名以及密码
        String msg ="";
        member.setPassword(MD5Util.getMD5Str(Sha1Util.getSha1(member.getPassword())+secretkey));
        try {
            AuthMember ckAuthMember = authMemberService.checkAuthMemberByMember(member);
            if(ckAuthMember==null){ //验证失败 ，跳回登录页面
                msg = "用户名或密码错误";
                log.info(msg);
                model.addAttribute("errormsg", msg);
                return ReturnResult.error(msg);
            }
            if("2".equals(ckAuthMember.getStatus())){
                msg ="该用户已禁用，请联系管理员";
                log.info(msg);
                return ReturnResult.error(msg);
            }
            Map<String,Object> map= loginService.getSessionMapByMember(ckAuthMember);
            if(map.isEmpty()){
                msg = "该帐号无可用授权";
                log.info(msg);
                return ReturnResult.error(msg);
            }
            session.setAttribute("user", ckAuthMember);
            session.setAttribute("menulist", map.get("menulist"));
            session.setAttribute("urlList", map.get("urlList"));
            session.setAttribute("menulistJson", map.get("menulistJson"));
            if(online!=null && online.equals("true")){
                LoginCookieUtil.saveCookie(ckAuthMember, response);
            }
            msg ="登录成功";
            log.info(msg);
            return ReturnResult.ok(msg, 0,"/");
        } catch (Exception e) {
            e.printStackTrace();
            log.error("操作失败");
            return ReturnResult.error("操作失败");
        }
    }

    /**
     * 登出
     * @param session
     * @param response
     * @throws IOException
     */
    @GetMapping("/login/logout")
    public void logout(HttpSession session,HttpServletResponse response) throws IOException {
        session.removeAttribute("user");
        session.removeAttribute("menulist");
        session.removeAttribute("urlList");
        LoginCookieUtil.clearCookie(response);
        response.sendRedirect("/user/login");
    }

}
