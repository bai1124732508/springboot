package com.fhzm.controller;

import com.fhzm.common.IDBuilder;
import com.fhzm.common.MD5Util;
import com.fhzm.common.ReturnResult;
import com.fhzm.common.Sha1Util;
import com.fhzm.common.file.Term;
import com.fhzm.common.file.TermManager;
import com.fhzm.entity.generator.*;
import com.fhzm.service.*;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@Slf4j
@Api(tags = "用户管理控制器",description="用户管理控制器",hidden=true)
@RequestMapping("user")
@Data
public class UserController {
    @Value("${custom.secretkey}")
    private String secretkey;
    @Autowired
    private WorldAreaService worldAreaService;
    @Autowired
    private AuthMemberService authMemberService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private BaseService baseService;
    @Autowired
    private SysLogService sysLogService;



    /**
     * 用户管理列表页面
     * @param model
     * @param vo
     * @param session
     * @return
     * @throws Exception
     */
    @GetMapping(value="index")
    public String index(Model model, BaseQueryVo vo, HttpSession session) throws Exception{
        if(vo == null){
            vo = new BaseQueryVo();
        }
        Map<String, String> search = vo.getSearch();
        if(search == null){
            search = new HashMap<>();
        }
        AuthMember user = (AuthMember) session.getAttribute("user");
        search.put("uid", user.getUid());
        search.put("isManage", "0");
        vo.setSearch(search);
        PageInfo<AuthMember> memberList = authMemberService.getMemberList(vo);
        model.addAttribute("page", memberList);
        model.addAttribute("search", search);
        return "user/index";
    }

    /**
     * 去往添加用户页面
     * @return
     * @throws Exception
     */
    @RequestMapping(value="add",method=RequestMethod.GET)
    public String add(HttpSession session,Model model) throws Exception{
        AuthMember user = (AuthMember) session.getAttribute("user");
        Map<String, String> search = new HashMap<>();
        search.put("uid",user.getUid());
        List<AuthGroup> list = roleService.getAuthGroupListBySearch(search);
        /**
         * 查询所有国家 集合
         */
        List<WorldArea> areaList = worldAreaService.getCountryList(0);
        //获取货币的单位的集合
        List<Term> monetaryList = TermManager.getTermList("Auth_member_monetary_unit");
        model.addAttribute("monetaryList", monetaryList);
        model.addAttribute("areaList", areaList);
        model.addAttribute("list", list);
        return "user/add";
    }


    /**
     * 添加用户
     * @param authMember
     * @param session
     * @param groupId
     * @return
     */
    @PostMapping("add")
    @ResponseBody
    public ReturnResult addUser(AuthMember authMember, HttpSession session, String groupId) {
        try {
            if(org.apache.commons.lang.StringUtils.isEmpty(groupId)) {
                return ReturnResult.error("请先选择权限！");
            }
            AuthMember user = (AuthMember) session.getAttribute("user");
            authMember.setUid(IDBuilder.getTableId());
            authMember.setCtime(new Date());
            authMember.setUtime(new Date());
            authMember.setIsRemove(0);
            authMember.setLoginCount(0);
            authMember.setPassword(MD5Util.getMD5Str(Sha1Util.getSha1(authMember.getPassword())+secretkey));
            authMember.setCuid(user.getUid());
            authMember.setIsManage(0); //后台成员列表
            baseService.insertObj(authMember);
            AuthGroupAccess access = new AuthGroupAccess();
            access.setGroupId(groupId);
            access.setUid(authMember.getUid());
            baseService.insertObj(access);
            return new ReturnResult("1", "操作成功", 3, "/user/index");
        } catch (Exception e) {
            e.printStackTrace();
            return ReturnResult.error("操作失败");
        }
    }


    /**
     * 去往修改用户页面
     * @param uid
     * @param model
     * @return
     * @throws Exception
     */
    @RequestMapping(value="edit", method= RequestMethod.GET)
    public String edit(HttpSession session,String uid,Model model) throws Exception{
        AuthMember user = (AuthMember) baseService.getObjById(uid,AuthMember.class);
        Map<String, String> search = new HashMap<>();
        //search.put("uid",user.getUid());
        //查询所有的角色
        List<AuthGroup> list = roleService.getAuthGroupListBySearch(search);
        /**
         * 查询所有国家 集合
         */
        List<WorldArea> areaList = worldAreaService.getCountryList(0);
        /**
         * 查询城市集合
         */
        List<WorldArea> cityList = worldAreaService.getCountryList(user.getCountry());
        model.addAttribute("cityList", cityList);
        //获取货币的单位的集合
        List<Term> monetaryList = TermManager.getTermList("Auth_member_monetary_unit");
        AuthGroupAccess access = (AuthGroupAccess) baseService.getObjById(uid,AuthGroupAccess.class);
        model.addAttribute("access", access);
        model.addAttribute("user", user);
        model.addAttribute("monetaryList", monetaryList);
        model.addAttribute("areaList", areaList);
        model.addAttribute("list", list);
        return "user/edit";
    }




    /**
     * 用户管理列表页面
     * @param model
     * @param vo
     * @param session
     * @return
     * @throws Exception
     */
    @GetMapping(value="log_index")
    public String logIndex(Model model, BaseQueryVo vo, HttpSession session) throws Exception{
        if(vo==null){
            vo = new BaseQueryVo();
        }
        Map<String, String> search = vo.getSearch();
        if(search==null){
            search = new HashMap<String,String>();
        }
        /**
         * 获取用户信息 判断用户的角色 如果是普通角色  只能查看自己的登录记录
         */
        AuthMember user = (AuthMember) session.getAttribute("user");
        if(!"1".equals(user.getUid())){ // 不是admin
            AuthGroupAccess access = (AuthGroupAccess) baseService.getObjById(user.getUid(),AuthGroupAccess.class);
            if("2".equals(access.getGroupId())){//如果是普通角色  只能查看自己的登录记录
                search.put("uid",user.getUid());
            }
        }
        search.put("logType","1");
        vo.setSearch(search);
        PageInfo<SysLog> page = sysLogService.getList(vo);
        model.addAttribute("page", page);
        model.addAttribute("search", search);
        return "user/log_index";
    }





}
