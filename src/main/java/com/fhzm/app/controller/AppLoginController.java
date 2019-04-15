package com.fhzm.app.controller;

import com.fhzm.common.*;
import com.fhzm.common.Jwt.JwtConstant;
import com.fhzm.common.Jwt.JwtUtils;
import com.fhzm.entity.generator.AboutAndService;
import com.fhzm.entity.generator.AuthGroupAccess;
import com.fhzm.entity.generator.AuthMember;
import com.fhzm.service.AboutService;
import com.fhzm.service.AttachService;
import com.fhzm.service.AuthMemberService;
import com.fhzm.service.BaseService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 用于用户登录操作
 * * All rights Reserved, Designed By http://www.fenghuaapp.com
 * @Description: AboutAndServiceMapper
 * @date: 2019-03-29 14:14:20
 * @version: V1.0
 * @Copyright: 2019 http://www.fenghuaapp.com/ Inc. All rights reserved.
 * 注意：本内容仅限于风华正茂科技(北京)有限公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@Controller
@Slf4j
@Data
@Api(tags = "APP用户登录控制器",description="APP用户登录控制器",hidden=true)
@RequestMapping("app/login")
public class AppLoginController {
    @Value("${file.path}")
    private String filePath;//虚拟路径
    @Autowired
    private AttachService attachService;
    @Autowired
    private BaseService baseService;
    @Autowired
    private AuthMemberService authMemberService;
    @Value("${custom.secretkey}")
    private String secretkey;
    @Autowired
    private AboutService aboutService;



    @PostMapping("post_login")
    @ResponseBody
    @ApiOperation(value = "登录接口（使用密码登录或快速登录）" ,  notes="登录接口（使用密码登录或快速登录）")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "account", value = "用户的账号（邮箱号或者手机号）", required = true, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "password", value = "用户的密码（前台加密MD5传值）", required = false, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "code", value = "快速登录的验证码", required = false, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "sendType", value = "0注册验证码   1找回验证码  2登录", required = false, paramType = "query", dataType = "String")
    })
    public ReturnResult postLogin(String account, String password, String code,String sendType) {
        try{
            if(StringUtils.isEmpty(password) && StringUtils.isEmpty(code) ) {
                return ReturnResult.error("0","参数不全！");
            }
            log.debug("判断用户是否存在数据库中");
            AuthMember authMember = authMemberService.getAppIsExistenceByAccount(account);
            if(authMember == null ){
                return ReturnResult.error("0","该用户不存在");
            }else if(authMember.getStatus() == 2 ){ //用户被禁用
                return ReturnResult.error("0","用户被禁用");
            }
            if(StringUtils.isNotEmpty(password )){ //用密码进行登陆的
                log.debug("用密码进行登陆的");
                String md5Password = MD5Util.getMD5Str(Sha1Util.getSha1(password)+secretkey);
                if(!md5Password.equals(authMember.getPassword())){
                    log.debug("用户名或密码错误");
                    return ReturnResult.error("0","用户名或密码错误");
                }
            }else if(StringUtils.isNotEmpty( code )){ //用验证码登陆的
                String cacheCode = CacheManager.getData(account+code+sendType);
                if(!code.equals(cacheCode)){
                    log.debug("验证码错误！");
                    return ReturnResult.error("0","验证码错误");
                }
            }
            // 保存 Jwt 生成 accessToken
            String accessToken = JwtUtils.createJWT(JwtConstant.JWT_ID, authMember,-1);
            Map<String,String> map = new HashMap<String,String>();
            map.put("accessToken", accessToken);
            map.put("uid", authMember.getUid());
            map.put("nickname", authMember.getNickname());
            if(StringUtils.isNotEmpty(sendType)){
                CacheManager.clear(account+code+sendType);
            }
            return ReturnResult.ok("1", "操作成功", map);
        }catch (Exception e) {
            e.printStackTrace();
            return ReturnResult.error("操作失败");
        }
    }


    @PostMapping("register")
    @ResponseBody
    @ApiOperation(value = "注册接口（手机号和邮箱号注册）" ,  notes="注册接口（手机号和邮箱号注册）")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "account", value = "用户的账号（邮箱号或者手机号）", required = true, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "password", value = "用户的密码（前台加密MD5传值）", required = true, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "code", value = "注册的验证码", required = true, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "sendType", value = "0注册验证码   1找回验证码  2登录", required = true, paramType = "query", dataType = "String")
    })
    public ReturnResult register(String account, String password, String code,String sendType) {
        try{
            if(StringUtils.isEmpty(account) || StringUtils.isEmpty(password) || StringUtils.isEmpty(code) || StringUtils.isEmpty(sendType)  ) { //用密码进行登陆的
                return ReturnResult.error("0","参数不全！");
            }
            log.debug("判断用户是否存在数据库中");
            AuthMember authMember = authMemberService.getAppIsExistenceByAccount(account);
            if(authMember != null ){
                return ReturnResult.error("0","该用户已经注册！");
            }
            String cacheCode = CacheManager.getData(account+code+sendType);
            if(!code.equals(cacheCode)){
                return ReturnResult.error("0","验证码错误！");
            }
            if(StringUtils.isNotEmpty(password )){ //用密码进行登陆的
                String md5Password = MD5Util.getMD5Str(Sha1Util.getSha1(password)+secretkey);
                authMember = new AuthMember();
                authMember.setUid(IDBuilder.getTableId());
                authMember.setCtime(new Date());
                authMember.setUtime(new Date());
                authMember.setIsRemove(0);
                authMember.setLoginCount(0);
                authMember.setPassword(md5Password);
                authMember.setCuid("1");
                authMember.setIsManage(0); //后台成员列表
                if( Validator.isEmail( account ) ) { //邮箱
                    authMember.setEmail(account);
                }
                if( Validator.isPhone( account ) ) { //邮箱
                    authMember.setPhone(account);
                }
                baseService.insertObj(authMember);
                AuthGroupAccess access = new AuthGroupAccess();
                access.setGroupId("2");//普通角色
                access.setUid(authMember.getUid());
                baseService.insertObj(access);
            }
            CacheManager.clear(account+code+sendType);
            return ReturnResult.ok("1", "操作成功", null);
        }catch (Exception e) {
            e.printStackTrace();
            return ReturnResult.error("操作失败");
        }
    }



    @PostMapping("forgot_password")
    @ResponseBody
    @ApiOperation(value = "找回密码接口（手机号和邮箱号）" ,  notes="找回密码接口（手机号和邮箱号）")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "account", value = "用户的账号（邮箱号或者手机号）", required = true, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "password", value = "用户的密码（前台加密MD5传值）", required = true, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "code", value = "注册的验证码", required = true, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "sendType", value = "0注册验证码   1找回验证码  2登录", required = true, paramType = "query", dataType = "String")
    })
    public ReturnResult forgotPassword(String account, String password, String code,String sendType) {
        try{
            if(StringUtils.isEmpty(account) || StringUtils.isEmpty(password) || StringUtils.isEmpty(code)
                    || StringUtils.isEmpty(sendType) ) { //用密码进行登陆的
                return ReturnResult.error("0","参数不全！");
            }
            log.debug("判断用户是否存在数据库中");
            AuthMember authMember = authMemberService.getAppIsExistenceByAccount(account);
            if(authMember == null){
                return ReturnResult.error("0","用户不存在！");
            }
            String cacheCode = CacheManager.getData(account+code+sendType);
            if(!code.equals(cacheCode)){
                return ReturnResult.error("0","验证码错误！");
            }
            if(authMember != null  && StringUtils.isNotEmpty( password )){ //用密码进行登陆的
                authMember.setUid(authMember.getUid());
                authMember.setUtime(new Date());
                authMember.setPassword(MD5Util.getMD5Str(Sha1Util.getSha1(password)+secretkey));
                baseService.updateObj(authMember);
            }
            CacheManager.clear(account+code+sendType);
            return ReturnResult.ok("1", "操作成功", null);
        }catch (Exception e) {
            e.printStackTrace();
            return ReturnResult.error("操作失败");
        }
    }


    @GetMapping("service_agreement")
    @ResponseBody
    @ApiOperation(value = "获取服务协议" ,  notes="获取服务协议")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "lang", value = "获取需要的语言", required = true, paramType = "query", dataType = "String")
    })
    public ReturnResult getServiceAgreement(String lang) throws IOException {
        try{
            if( StringUtils.isEmpty( lang )  ) {
                return ReturnResult.error("0","参数不全！");
            }
            Map<String, String> search = new HashMap<>();
            search.put("type","1");
            search.put("is_show","0");
            search.put("lang","lang");
            AboutAndService service = aboutService.getAboutAndServiceBySearch(search);
            return ReturnResult.ok("1", "操作成功", service);
        }catch (Exception e) {
            e.printStackTrace();
            return ReturnResult.error("操作失败");
        }
    }




}
