package com.fhzm.controller;

import com.fhzm.common.ReturnResult;
import com.fhzm.entity.generator.*;
import com.fhzm.service.AboutService;
import com.fhzm.service.BaseService;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@Slf4j
@Api(tags = "关于我们和服务协议控制器",description="关于我们和服务协议控制器",hidden=true)
@RequestMapping("about")
public class AboutContorller {

    @Value("${file.path}")
    private String filePath;//虚拟路径
    @Value("${file.url}")
    private String fileUrl;//虚拟路径
    @Autowired
    private AboutService aboutService;
    @Autowired
    private BaseService baseService;

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
        AuthMember user = (AuthMember) session.getAttribute("user");
        search.put("type","0");
        vo.setSearch(search);
        PageInfo<AboutAndService> memberList = aboutService.getAboutList(vo);
        model.addAttribute("page", memberList);
        model.addAttribute("search", search);
        return "about/index";
    }


    /**
     * 编辑关于我们
     * @param model
     * @param session
     * @return
     * @throws Exception
     */
    @GetMapping("edit")
    public String edit(Model model, HttpSession session,String id) throws Exception{
        AboutAndService about = (AboutAndService) baseService.getObjById(id,AboutAndService.class);
        List<Attach> list = new ArrayList<>();
        if(about != null && StringUtils.isNotEmpty(about.getLogo())){
            Attach attach = (Attach) baseService.getObjById(about.getLogo(),Attach.class);
            list.add(attach);
        }
        model.addAttribute("attachList",list);
        model.addAttribute("about", about);
        return "about/edit";
    }



    private void getAboutAndServiceInfo(Model model, String id) throws Exception {
        AboutAndService about = (AboutAndService) baseService.getObjById(id,AboutAndService.class);
        if(about.getType() == 0) { //关于我们
            String logoPath = "";
            if(about != null && StringUtils.isNotEmpty(about.getLogo())){
                Attach attach = (Attach) baseService.getObjById(about.getLogo(),Attach.class);
                logoPath= fileUrl+attach.getSavePath()+attach.getSaveName();
            }
            model.addAttribute("logoPath", logoPath);
        }
        model.addAttribute("about", about);

    }


    /**
     * 保存
     * @param about
     * @return
     */
    @PostMapping("edit")
    @ResponseBody
    public ReturnResult edit(AboutAndService about) {
        try {
            baseService.updateObj(about);
            return new ReturnResult("1", "操作成功", 3, "/about/index");
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
            aboutService.updateAboutState(idList,status,"is_show");
            return new ReturnResult("1", "操作成功", 3, "/about/index");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ReturnResult.error("操作失败");
    }

    /**
     * 查看关于我们
     * @param model
     * @param session
     * @param id
     * @return
     * @throws Exception
     */
    @GetMapping("look")
    public String look(Model model, HttpSession session,String id) throws Exception{
        getAboutAndServiceInfo(model, id);
        return "about/look";
    }

    /**
     * 关于我们列表页面
     * @param model
     * @param vo
     * @param session
     * @return
     * @throws Exception
     */
    @GetMapping("service_agreement")
    public String serviceAgreement(Model model, BaseQueryVo vo, HttpSession session) throws Exception{
        if(vo == null){
            vo = new BaseQueryVo();
        }
        Map<String, String> search = vo.getSearch();
        if(search == null){
            search = new HashMap<>();
        }
        AuthMember user = (AuthMember) session.getAttribute("user");
        search.put("type","1");
        vo.setSearch(search);
        PageInfo<AboutAndService> memberList = aboutService.getAboutList(vo);
        model.addAttribute("page", memberList);
        model.addAttribute("search", search);
        return "about/service_agreement";
    }




}
