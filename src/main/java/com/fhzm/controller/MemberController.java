package com.fhzm.controller;

import com.fhzm.common.*;
import com.fhzm.entity.generator.AuthGroup;
import com.fhzm.entity.generator.AuthGroupAccess;
import com.fhzm.entity.generator.AuthMember;
import com.fhzm.entity.generator.BaseQueryVo;
import com.fhzm.service.AuthMemberService;
import com.fhzm.service.BaseService;
import com.fhzm.service.RoleService;
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
import java.util.*;

@Controller
@Slf4j
@Api(tags = "角色管理控制器",description="角色管理控制器",hidden=true)
@RequestMapping("member")
@Data
public class MemberController {
    @Value("${custom.secretkey}")
    private String secretkey;

    @Autowired
    private AuthMemberService authMemberService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private BaseService baseService;

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
        if(vo==null){
            vo = new BaseQueryVo();
        }
        Map<String, String> search = vo.getSearch();
        if(search==null){
            search = new HashMap<>();
        }
        AuthMember user = (AuthMember) session.getAttribute("user");
        search.put("uid", user.getUid());
        search.put("isManage", "1");
        vo.setSearch(search);
        PageInfo<AuthMember> memberList = authMemberService.getMemberList(vo);
        model.addAttribute("page", memberList);
        model.addAttribute("search", search);
        return "member/index";
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
        model.addAttribute("list", list);
        return "member/add";
    }

    /**
     * 添加用户
     * @param authMember
     * @param session
     * @param groupId
     * @return
     */
    @RequestMapping(value="add",method=RequestMethod.POST)
    @ResponseBody
    public ReturnResult doadd(AuthMember authMember,HttpSession session,String groupId) {
        try {
            if(org.apache.commons.lang.StringUtils.isEmpty(groupId)) {
                return ReturnResult.error("请选择所属角色");
            }
            AuthMember user = (AuthMember) session.getAttribute("user");
            authMember.setUid(IDBuilder.getTableId());
            authMember.setCtime(new Date());
            authMember.setUtime(new Date());
            authMember.setIsRemove(0);
            authMember.setLoginCount(0);
            authMember.setPassword(MD5Util.getMD5Str(Sha1Util.getSha1(authMember.getPassword())+secretkey));
            authMember.setCuid(user.getUid());
            authMember.setIsManage(1); //后台成员列表
            baseService.insertObj(authMember);
            AuthGroupAccess access = new AuthGroupAccess();
            access.setGroupId(groupId);
            access.setUid(authMember.getUid());
            baseService.insertObj(access);
            return new ReturnResult("1", "操作成功", 3, "/member/index");
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
        AuthMember authMember = (AuthMember) session.getAttribute("user");
        Map<String, String> search = new HashMap<>();
        search.put("uid",authMember.getUid());
        List<AuthGroup> list = roleService.getAuthGroupListBySearch(search);
        AuthGroupAccess access = (AuthGroupAccess) baseService.getObjById(uid,AuthGroupAccess.class);
        model.addAttribute("access", access);
        model.addAttribute("list", list);
        model.addAttribute("user", user);
        return "member/edit";
    }


    /**
     * 修改用户
     * @param authMember
     * @param session
     * @param groupId
     * @return
     */
    @RequestMapping(value="edit",method=RequestMethod.POST)
    @ResponseBody
    public ReturnResult edit(AuthMember authMember,HttpSession session,String groupId) {
        try {
            if(org.apache.commons.lang.StringUtils.isEmpty(groupId)) {
                return ReturnResult.error("请选择所属角色");
            }
            authMember.setUtime(new Date());
            if(StringUtil.isNotNull(authMember.getPassword())){
                authMember.setPassword(MD5Util.getMD5Str(Sha1Util.getSha1(authMember.getPassword())+secretkey));
            }
            baseService.updateObj(authMember);
            AuthGroupAccess access = new AuthGroupAccess();
            access.setGroupId(groupId);
            access.setUid(authMember.getUid());
            baseService.updateObj(access);
            return new ReturnResult("1", "操作成功", 3, "/member/index");
        } catch (Exception e) {
            e.printStackTrace();
            return ReturnResult.error("操作失败");
        }
    }

    /**
     * 删除用户
     * @param uid
     * @return
     * @throws Exception
     */
    @RequestMapping(value="remove")
    @ResponseBody
    public ReturnResult remove(String uid) throws Exception{
        try {
            String[] uids = uid.split(",");
            List<String> uidList=new ArrayList<String>();
            for (String string : uids) {
                uidList.add(string);
            }
            authMemberService.updateAuthMemberState(uidList, "1", "is_remove");
            return new ReturnResult("1", "操作成功", 3, "/member/index");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ReturnResult.error("操作失败");
    }


    /**
     * 修改用户启用,禁止
     * @param uid
     * @param status
     * @return
     * @throws Exception
     */
    @RequestMapping(value="state/{status}",method=RequestMethod.POST)
    @ResponseBody
    public ReturnResult state(String uid,@PathVariable("status")String status) throws Exception{
        String[] uids = uid.split(",");
        List<String> uidList=new ArrayList<String>();
        for (String string : uids) {
            uidList.add(string);
        }
        authMemberService.updateAuthMemberState(uidList,status,"status");
        return new ReturnResult("1", "操作成功", 3, "/member/index");
    }



    /**
     * 验证系统中是否存在相同的邮箱和手机号
     * @param param
     * @param name
     * @param uid
     * @return
     * @throws Exception
     */
    @RequestMapping(value="validform")
    @ResponseBody
    public Map<String,Object> validform(String param,String name,String uid) throws Exception{
        System.out.println(name);
        System.out.println(param);
        Map<String,Object> map=new HashMap<String,Object>();
        List<AuthMember> list = new ArrayList<>(0);
        if(StringUtil.isNotNull(uid)) {
            list = authMemberService.validform(name,param,uid);
        }else {
            list = authMemberService.validform(name,param,"");
        }
        if(name.equals("username")){
            if (list.size()>0) {
                map.put("info", " 系统中已经存在该名称");
                map.put("status", "n");
            }else {
                map.put("info", "该名称可用");
                map.put("status", "y");
            }
        }
        if(name.equals("phone")){
            if (list.size()>0) {
                map.put("info", "系统中已经存在该手机号");
                map.put("status", "n");
            }else {
                map.put("info", "该手机号可用");
                map.put("status", "y");
            }
        }
        if(name.equals("email")){
            if (list.size()>0) {
                map.put("info", "系统中已经存在该邮箱");
                map.put("status", "n");
            }else {
                map.put("info", " 该邮箱可用");
                map.put("status", "y");
            }
        }
        return map;
    }




}
