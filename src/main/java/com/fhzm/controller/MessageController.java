package com.fhzm.controller;

import com.fhzm.common.IDBuilder;
import com.fhzm.common.JsonUtils;
import com.fhzm.common.ReturnResult;
import com.fhzm.config.WebSocketServer;
import com.fhzm.entity.generator.*;
import com.fhzm.service.AuthMemberService;
import com.fhzm.service.BaseService;
import com.fhzm.service.MessageService;
import com.fhzm.service.RoleService;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@Slf4j
@Api(tags = "消息管理控制器",description="消息管理控制器",hidden=true)
@RequestMapping("message")
public class MessageController {
    @Autowired
    private MessageService messageService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private AuthMemberService authMemberService;
    @Autowired
    private BaseService baseService;

    /**
     *  获取发送消息的列表
     * @param model
     * @param vo
     * @param session
     * @return
     * @throws Exception
     */
    @GetMapping(value="send_list")
    public String sendList(Model model, BaseQueryVo vo, HttpSession session) throws Exception{
        if(vo==null){
            vo = new BaseQueryVo();
        }
        Map<String, String> search = vo.getSearch();
        if(search==null){
            search = new HashMap<String,String>();
        }
        AuthMember user = (AuthMember) session.getAttribute("user");
        if(!"1".equals(user.getUid())){
            search.put("uid",user.getUid());
        }
        vo.setSearch(search);
        PageInfo<Message> page = messageService.getSendList(vo);
        model.addAttribute("page", page);
        model.addAttribute("search", search);
        return "message/send_list";
    }


    /**
     * 去往添加用户页面
     * @return
     * @throws Exception
     */
    @GetMapping("add")
    public String add(HttpSession session,Model model) throws Exception{
        AuthMember user = (AuthMember) session.getAttribute("user");
        Map<String, String> search = new HashMap<>();
        //search.put("uid",user.getUid());
        List<AuthGroup> list = roleService.getAuthGroupListBySearch(search);
        model.addAttribute("list", list);
        List<AuthMember> userList = authMemberService.getUserListBySearch(search);
        model.addAttribute("userList", userList);
        return "message/add";
    }


    /**
     * 添加消息
     * @param message
     * @param session
     * @return
     */
    @PostMapping("add")
    @ResponseBody
    public ReturnResult addUser(Message message, HttpSession session) {
        try {
            AuthMember user = (AuthMember) session.getAttribute("user");
            message.setId(IDBuilder.getTableId());
            String msg = messageService.addMessage(message,user,"add");
            if("userNull".equals(msg)){
                return ReturnResult.error("该角色下没有用户！");
            }else{
                return new ReturnResult("1", "操作成功", 3, "/message/send_list");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ReturnResult.error("操作失败");
        }
    }


    /**
     * 去往编辑消息页面
     * @return
     * @throws Exception
     */
    @GetMapping("edit")
    public String edit(HttpSession session,Model model,String id) throws Exception{
        AuthMember user = (AuthMember) session.getAttribute("user");
        Message message = (Message) baseService.getObjById(id,Message.class);
        Map<String, String> search = new HashMap<>();
        List<AuthGroup> list = roleService.getAuthGroupListBySearch(search);
        List<AuthMember> userList = authMemberService.getUserListBySearch(search);
        model.addAttribute("list", list);
        model.addAttribute("userList", userList);
        model.addAttribute("message", message);
        return "message/edit";
    }

    /**
     * 添加消息
     * @param message
     * @param session
     * @return
     */
    @PostMapping("edit")
    @ResponseBody
    public ReturnResult editUser(Message message, HttpSession session) {
        try {
            AuthMember user = (AuthMember) session.getAttribute("user");
            Message messag = (Message) baseService.getObjById(message.getId(),Message.class);
            if(messag.getStatus() == 1){
                return ReturnResult.error("该消息已经发布了！");
            }else{
                String msg = messageService.addMessage(message,user,"edit");
                if("userNull".equals(msg)){
                    return ReturnResult.error("该角色下没有用户！");
                }else{
                    return new ReturnResult("1", "操作成功", 3, "/message/send_list");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ReturnResult.error("操作失败");
        }
    }


    /**
     * 删除消息
     * @param id
     * @return
     * @throws Exception
     */
    @PostMapping("remove")
    @ResponseBody
    public ReturnResult remove(String id) throws Exception{
        try {
            messageService.removeByIds(id, "1", "is_remove");
            return new ReturnResult("1", "操作成功", 3, "/message/send_list");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ReturnResult.error("操作失败");
    }


    /**
     * 去往添加用户页面
     * @return
     * @throws Exception
     */
    @GetMapping("look")
    public String look(HttpSession session,Model model,String id ) throws Exception{
        AuthMember user = (AuthMember) session.getAttribute("user");
        Message message = (Message) baseService.getObjById(id,Message.class);
        model.addAttribute("message", message);
        return "message/look";
    }


    /**
     *  获取发送消息的列表
     * @param model
     * @param vo
     * @param session
     * @return
     * @throws Exception
     */
    @GetMapping("receive_list")
    public String receiveList(Model model, BaseQueryVo vo, HttpSession session) throws Exception{
        if(vo==null){
            vo = new BaseQueryVo();
        }
        Map<String, String> search = vo.getSearch();
        if(search==null){
            search = new HashMap<String,String>();
        }
        AuthMember user = (AuthMember) session.getAttribute("user");
        search.put("uid",user.getUid());
        vo.setSearch(search);
        PageInfo<Message> page = messageService.getReceiveList(vo);
        model.addAttribute("page", page);
        model.addAttribute("search", search);
        return "message/receive_list";
    }

    /**
     * 删除消息
     * @param id
     * @return
     * @throws Exception
     */
    @PostMapping("receive_remove")
    @ResponseBody
    public ReturnResult receiveRemove(String id, HttpSession session) throws Exception{
        try {
            if(StringUtils.isNotEmpty(id)){
                for(String idds : id.split(",")){
                    baseService.delObjById(idds,MessageLog.class);
                }
            }
            /**
             * 更新消息的个数
             */
            Map <String,Object > map = new HashMap<String,Object >();
            AuthMember user = (AuthMember) session.getAttribute("user");
            Map<String,String> msgCount = messageService.getMsgCountMap(user.getUid());
            map.put("type","3");//删除消息 更新 消息个数
            map.put("msgCount", msgCount);
            WebSocketServer.send(JsonUtils.toJSONString(map),user.getUid());
            return new ReturnResult("1", "操作成功", 3, "/message/receive_list");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ReturnResult.error("操作失败");
    }


}
