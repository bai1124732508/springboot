package com.fhzm.controller;

import com.fhzm.common.IDBuilder;
import com.fhzm.common.ReturnResult;
import com.fhzm.common.file.Term;
import com.fhzm.common.file.TermManager;
import com.fhzm.entity.generator.AuthMember;
import com.fhzm.entity.generator.BaseQueryVo;
import com.fhzm.entity.generator.Help;
import com.fhzm.service.BaseService;
import com.fhzm.service.HelpService;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.*;

@Controller
@Slf4j
@Api(tags = "帮助中心控制器",description="帮助中心控制器",hidden=true)
@RequestMapping("help")
public class HelpContorller {

    @Autowired
    private BaseService baseService;
    @Autowired
    private HelpService helpService;



    /**
     * 关于我们列表页面
     * @param model
     * @param vo
     * @param session
     * @return
     * @throws Exception
     */
    @GetMapping("index")
    public String index(Model model, BaseQueryVo vo, HttpSession session) throws Exception{
        if(vo == null){
            vo = new BaseQueryVo();
        }
        Map<String, String> search = vo.getSearch();
        if(search == null){
            search = new HashMap<>();
        }
        vo.setSearch(search);
        PageInfo<Help> page = helpService.getList(vo);
        List<Term> typeList = TermManager.getTermList("help_type");
        model.addAttribute("typeList", typeList);
        model.addAttribute("page", page);
        model.addAttribute("search", search);
        return "help/index";
    }


    /**
     * 编辑关于我们
     * @param model
     * @param session
     * @return
     * @throws Exception
     */
    @GetMapping("add")
    public String add(Model model, HttpSession session,String id) throws Exception{
        List<Term> typeList = TermManager.getTermList("help_type");
        model.addAttribute("typeList",typeList);
        return "help/add";
    }


    /**
     * 添加帮组
     * @param help
     * @param session
     * @return
     */
    @PostMapping("add")
    @ResponseBody
    public ReturnResult addHelp(Help help, HttpSession session) {
        try {
            AuthMember user = (AuthMember) session.getAttribute("user");
            help.setId(IDBuilder.getTableId());
            help.setCreateId(user.getUid());
            baseService.insertObj(help);
            return new ReturnResult("1", "操作成功", 3, "/help/index");
        } catch (Exception e) {
            e.printStackTrace();
            return ReturnResult.error("操作失败");
        }
    }

    /**
     * 编辑我的帮组
     * @param model
     * @param session
     * @param id
     * @return
     * @throws Exception
     */
    @GetMapping("edit")
    public String edit(Model model, HttpSession session,String id) throws Exception{
        Help help = (Help) baseService.getObjById(id,Help.class);
        model.addAttribute("help",help);
        List<Term> typeList = TermManager.getTermList("help_type");
        model.addAttribute("typeList",typeList);
        return "help/edit";
    }

    /**
     * 保存编辑的帮助中心
     * @param help
     * @param session
     * @return
     */
    @PostMapping("edit")
    @ResponseBody
    public ReturnResult editHelp(Help help, HttpSession session) {
        try {
            AuthMember user = (AuthMember) session.getAttribute("user");
            help.setUtime(new Date());
            baseService.updateObj(help);
            return new ReturnResult("1", "操作成功", 3, "/help/index");
        } catch (Exception e) {
            e.printStackTrace();
            return ReturnResult.error("操作失败");
        }
    }

    /**
     * 禁用或者启用的状态
     * @param id
     * @param status
     * @return
     * @throws Exception
     */
    @PostMapping("state")
    @ResponseBody
    public ReturnResult updateState(String id,String status) throws Exception{
        try {
            String[] ids = id.split(",");
            List<String> idList=new ArrayList<String>();
            for (String string : ids) {
                idList.add(string);
            }
            helpService.updateState(idList,status,"is_show");
            return new ReturnResult("1", "操作成功", 3, "/help/index");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ReturnResult.error("操作失败");
    }



}
