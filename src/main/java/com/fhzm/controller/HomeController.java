package com.fhzm.controller;

import com.fhzm.entity.generator.*;
import com.fhzm.service.BaseService;
import com.fhzm.service.ConfigsService;
import com.fhzm.service.MessageService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@Slf4j
@Api(tags = "后台主页控制器",description="后台主页控制器",hidden=true)
public class HomeController {
    @Autowired
    private ConfigsService configsService;
    @Autowired
    private BaseService baseService;
    @Value("${file.url}")
    private String fileUrl;//虚拟路径
    @Autowired
    private MessageService messageService;


    @GetMapping("/")
    public String index(HttpSession session,Model model) throws Exception {
        /**
         * 查询logo 和 平台名称
         */
        String platformName = configsService.getValueById(1);
        String logoId = configsService.getValueById(2);
        Attach attach = (Attach) baseService.getObjById(logoId,Attach.class);
        String logoPath= fileUrl+attach.getSavePath()+attach.getSaveName();
        model.addAttribute("logoPath", logoPath);
        model.addAttribute("platformName", platformName);
        AuthMember user = (AuthMember) session.getAttribute("user");
        /**
         * 查询自己的未读消息的个数
         */
        Map<String,String> msgCount = messageService.getMsgCountMap(user.getUid());
        model.addAttribute("msgCount", msgCount);
        boolean isShowMsg = false;
        /**
         * 判断当前用户有无接受到的消息权限
         */
        List<String> urlList= (List<String>) session.getAttribute("urlList");
        for(String url : urlList){
            if(url.equals("message/receive_list")){ //接受到的消息
                isShowMsg = true;
                break;
            }
        }
        model.addAttribute("isShowMsg", isShowMsg);
        return "home/index";
    }




    @GetMapping("/home/public")
    public String homePublic(HttpSession session){
        return "home/public";
    }
}
