package com.fhzm.service.impl;

import com.fhzm.common.IDBuilder;
import com.fhzm.common.JsonUtils;
import com.fhzm.config.WebSocketServer;
import com.fhzm.dao.generator.MessageLogMapper;
import com.fhzm.dao.generator.MessageMapper;
import com.fhzm.entity.generator.AuthMember;
import com.fhzm.entity.generator.BaseQueryVo;
import com.fhzm.entity.generator.Message;
import com.fhzm.entity.generator.MessageLog;
import com.fhzm.service.AuthMemberService;
import com.fhzm.service.BaseService;
import com.fhzm.service.MessageService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service("messageService")
@Transactional
@Slf4j

public class MessageServiceImpl implements MessageService {
    @Autowired
    private MessageMapper messageMapper;
    @Autowired
    private MessageLogMapper messageLogMapper;
    @Autowired
    private AuthMemberService authMemberService;
    @Autowired
    private BaseService baseService;
    @Autowired
    private MessageService messageService;

    @Override
    public PageInfo<Message> getSendList(BaseQueryVo vo) {
        PageHelper.startPage(vo.getPageNum(), vo.getPageSize());
        List<Message> list= messageMapper.getSendList(vo);
        PageInfo<Message> pageInfo = new PageInfo<>(list);
        return pageInfo;
    }

    @Override
    public void insertMessageLogByList(List<MessageLog> logList) {
        messageLogMapper.insertMessageLogByList(logList);
    }

    @Override
    public String addMessage(Message message, AuthMember user, String type) throws Exception {
        Map<String, String> search = new HashMap<>();
        message.setCtime(new Date());
        message.setUtime(new Date());
        message.setCreateUid(user.getUid());
        message.setType(0);
        /**
         * 判断状态
         */
        List<MessageLog> logList = new ArrayList<>();
        if(message.getStatus() == 1){ //发布
            if( "all".equals(message.getReceiveUid()) ){ //发送的是全部的用户
                log.debug("发送的是全部的用户");
                List<AuthMember> userList = new ArrayList<>();
                /**
                 * 判端 有无选择角色
                 */
                if("all".equals(message.getGroupId())){ // 没有选择角色 //
                    log.debug("发送的是全部的角色");
                    userList = authMemberService.getUserListBySearch(search);
                }else{ //选择了角色
                    search.put("groupId", message.getGroupId());
                    userList = authMemberService.getUserListBySearch(search);
                }
                if( userList.size() > 0){
                    for(AuthMember authMember : userList){
                        logList.add(new MessageLog(IDBuilder.getTableId(),authMember.getUid(),message.getId(),message.getType(),0,new Date(),new Date()));
                    }
                }else{
                    return "userNull";
                }
            }else{
                if(StringUtils.isNotEmpty(message.getReceiveUid())){
                    for(String uid : message.getReceiveUid().split(",")){
                        logList.add(new MessageLog(IDBuilder.getTableId(),uid,message.getId(),message.getType(),0,new Date(),new Date()));
                    }
                }
            }
        }
        if("add".equals(type)){ //新增
            baseService.insertObj(message);
        }else{ //修改
            baseService.updateObj(message);
        }
        if(message.getStatus() == 1 && logList.size() > 0){
            messageService.insertMessageLogByList(logList);
            /**
             * WebSocketServer 发送消息推送
             */
            for(MessageLog log : logList){
                Map <String,String > map = new HashMap<String,String >();
                map.put("type","2");///有新增的消息
                map.put("msgType",log.getType()+"");
                WebSocketServer.send(JsonUtils.toJSONString(map), log.getUid());
            }
        }
        return "ok";
    }

    @Override
    public void removeByIds(String id, String status, String feild) {
        String[] ids = id.split(",");
        List<String> uidList=new ArrayList<String>();
        for (String string : ids) {
            uidList.add(string);
        }
        messageMapper.removeByIds(uidList,status,feild);
    }

    @Override
    public PageInfo<Message> getReceiveList(BaseQueryVo vo) {
        PageHelper.startPage(vo.getPageNum(), vo.getPageSize());
        List<Message> list= messageMapper.getReceiveList(vo);
        PageInfo<Message> pageInfo = new PageInfo<>(list);
        return pageInfo;
    }

    @Override
    public List<MessageLog> getMessageCountByUid(String uid) {
        return messageLogMapper.getMessageCountByUid(uid);
    }

    @Override
    public Map<String, String> getMsgCountMap(String uid) {
        Map<String,String> msgCount = new HashMap<>();
        /**
         * 初始值
         */
        msgCount.put("0","0");
        msgCount.put("1","0");
        msgCount.put("2","0");
        int count = 0 ;
        List<MessageLog> list = messageService.getMessageCountByUid(uid);
        if(list.size() > 0 ){
            for(MessageLog msg : list){
                msgCount.put(msg.getType()+"",msg.getId());
                count += Integer.parseInt(msg.getId());
            }
        }
        msgCount.put("all",count+"");
        return msgCount;
    }


}
