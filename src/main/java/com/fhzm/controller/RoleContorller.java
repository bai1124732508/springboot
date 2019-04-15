package com.fhzm.controller;

import com.fhzm.annotation.Log;
import com.fhzm.common.IDBuilder;
import com.fhzm.common.ReturnResult;
import com.fhzm.common.StringUtil;
import com.fhzm.entity.generator.AuthGroup;
import com.fhzm.entity.generator.AuthMember;
import com.fhzm.entity.generator.BaseQueryVo;
import com.fhzm.entity.generator.Menu;
import com.fhzm.service.AuthMemberService;
import com.fhzm.service.BaseService;
import com.fhzm.service.RoleService;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@Slf4j
@Api(tags = "角色管理控制器",description="角色管理控制器",hidden=true)
@RequestMapping("role")
public class RoleContorller {
    @Autowired
    private BaseService baseService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private AuthMemberService authMemberService;

    /**
     * 根据查询条件分页查询角色列表
     * @param vo
     * @param model
     * @return
     * @throws Exception
     */
    @RequestMapping(value="index",method = RequestMethod.GET)
    public String showRoleByPage(BaseQueryVo vo, Model model, HttpSession session, String pid) throws Exception{
        model.addAttribute("pRoleName", "顶级角色");
        if(StringUtil.isNull(pid)||"null".equals(pid)){
            pid = "0" ;
            model.addAttribute("ppid",0);
        }else{
            AuthGroup authGroup= (AuthGroup) baseService.getObjById(pid,AuthGroup.class);
            if(authGroup!=null){
                model.addAttribute("pRoleName", authGroup.getTitle());
                model.addAttribute("ppid", authGroup.getPid());
            }
        }
        if(vo==null){
            vo = new BaseQueryVo();
        }
        Map<String, String> search = vo.getSearch();
        if(search==null){
            search = new HashMap<String,String>();
        }
        AuthMember user = (AuthMember) session.getAttribute("user");
        search.put("uid", user.getUid());
        search.put("pid", pid);
        vo.setSearch(search);
        PageInfo<AuthGroup> page= roleService.getRoleByPage(vo);
        model.addAttribute("search",search);
        model.addAttribute("page", page);
        model.addAttribute("pid", pid);
        return "role/index";
    }

    /**
     * 编辑前跳转的页面
     * @param id
     * @param model
     * @return
     * @throws Exception
     */
    @RequestMapping(value="/edit" ,method=RequestMethod.GET)
    public String edit(String id,Model model) throws Exception{
        AuthGroup authGroup= (AuthGroup) baseService.getObjById(id,AuthGroup.class);
        model.addAttribute("ckAuthGroup", authGroup);
        return "role/edit";
    }

    /**
     * 保存修改角色
     * @param authGroup
     * @return
     */
    @RequestMapping(value="/edit",method=RequestMethod.POST)
    @ResponseBody
    public ReturnResult saveEditRole(AuthGroup authGroup) {
        try {
            baseService.updateObj(authGroup);
            return ReturnResult.ok("操作成功", "index");
        } catch (Exception e) {
            e.printStackTrace();
            return ReturnResult.error("操作失败");
        }
    }

    /**
     * 添加前跳转页面
     * @return
     * @throws Exception
     */
    @RequestMapping(value="/add" ,method=RequestMethod.GET)
    public String addbefore(String pid,Model model) throws Exception{
        model.addAttribute("pRoleName", "顶级角色");
        if(StringUtil.isNull(pid)){
            pid = "0";
        }
        model.addAttribute("pid", pid);
        return "role/add";
    }

    /**
     *保存角色
     * @param authGroup
     * @param session
     * @return
     */
    @RequestMapping(value="/add",method=RequestMethod.POST)
    @ResponseBody
    public ReturnResult saveRole(AuthGroup authGroup,HttpSession session){
        try {
            AuthMember user = (AuthMember) session.getAttribute("user");
            authGroup.setId(IDBuilder.getTableId());
            authGroup.setModule(user.getUid());
            authGroup.setStatus("1");
            authGroup.setType("1");
            authGroup.setLeavel(0);
            baseService.insertObj(authGroup);
            return ReturnResult.ok("操作成功","index");
        } catch (Exception e) {
            e.printStackTrace();
            return ReturnResult.error("操作失败");
        }
    }


    /**
     * 根据ID删除角色
     * @param id
     * @return
     */
    @RequestMapping(value="/delete" ,method=RequestMethod.POST)
    @ResponseBody
    public ReturnResult delete(String id){
        try {
            baseService.delObjById(id,AuthGroup.class);
            return ReturnResult.ok("操作成功","index");
        } catch (Exception e) {
            e.printStackTrace();
            return ReturnResult.error("操作失败");
        }
    }


    /**
     * 启用/禁用角色
     * @param id
     * @param status
     * @return
     */
    @RequestMapping(value="/state/{status}",method=RequestMethod.POST)
    @ResponseBody
    public ReturnResult changeStatus(String id, @PathVariable("status")String status){
        String[] idds = id.split(",");
        List<String> list=new ArrayList<String>();
        for (String idd : idds) {
            list.add(idd);
        }
        try {
            roleService.changeRoleStatus(list,status);
            return ReturnResult.ok("操作成功","index");
        } catch (Exception e) {
            e.printStackTrace();
            return ReturnResult.error("操作失败");
        }
    }

    /**
     * 获取角色 授权的页面
     * @param id
     * @param model
     * @return
     * @throws Exception
     */
    @GetMapping("author")
    public String authorIndex(String id,Model model) throws Exception{
        AuthGroup authGroup= (AuthGroup) baseService.getObjById(id,AuthGroup.class);
        List<Menu> ruleCutomList = roleService.getRoleListByType("0");
        for (Menu ckAuthRuleCustom : ruleCutomList) {
            if(authGroup.getRules()!=null){
                ckAuthRuleCustom.setChecked(authGroup.getRules().contains(ckAuthRuleCustom.getId()));
            }else{
                ckAuthRuleCustom.setChecked(false);
            }
            List<Menu> ruleCutomList2 = roleService.getRoleListByType(ckAuthRuleCustom.getId()+"");
            if(ruleCutomList2 != null){
                for (Menu ckAuthRuleCustom2 : ruleCutomList2) {
                    if(authGroup.getRules() != null){
                        ckAuthRuleCustom2.setChecked(authGroup.getRules().contains(ckAuthRuleCustom2.getId()));
                    }else{
                        ckAuthRuleCustom2.setChecked(false);
                    }
                    List<Menu> ruleCutomList3 = roleService.getRoleListByType(ckAuthRuleCustom2.getId()+"");
                    for (Menu ckAuthRuleCustom3 : ruleCutomList3) {
                        if(authGroup.getRules()!=null){
                            ckAuthRuleCustom3.setChecked(authGroup.getRules().contains(ckAuthRuleCustom3.getId()));
                        }else{
                            ckAuthRuleCustom3.setChecked(false);
                        }
                    }
                    if(ruleCutomList3!=null){
                        ckAuthRuleCustom2.setRuleCustomList(ruleCutomList3);
                    }
                }
                ckAuthRuleCustom.setRuleCustomList(ruleCutomList2);
            }
        }
        model.addAttribute("ruleCutomList", ruleCutomList);
        model.addAttribute("id", id);
        return "role/author";
    }

    /**
     * 保存所选择的 权限
     * @param id
     * @param request
     * @return
     */
    @Log(logType=2,operationName="更改权限",operationNameEn = "change permission")
    @PostMapping("author_add")
    @ResponseBody
    public ReturnResult authorAdd(String id,HttpServletRequest request){
        String[] parameterValues = request.getParameterValues("rules");
        String ruleString = "";
        if(parameterValues!=null){
            for (String rule : parameterValues) {
                ruleString+=rule+",";
            }
            String substring = ruleString.substring(0, ruleString.length()-1);
            System.out.println(substring);
            try {
                AuthGroup ckAuthGroup = new AuthGroup();
                ckAuthGroup.setId(id);
                ckAuthGroup.setRules(substring);
                baseService.updateObj(ckAuthGroup);
                return new ReturnResult("1", "操作成功", 3, "/role/index");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return ReturnResult.error("操作失败");
    }





}
