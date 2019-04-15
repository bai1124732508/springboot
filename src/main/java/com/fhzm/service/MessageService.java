package com.fhzm.service;

import com.fhzm.entity.generator.AuthMember;
import com.fhzm.entity.generator.BaseQueryVo;
import com.fhzm.entity.generator.Message;
import com.fhzm.entity.generator.MessageLog;
import com.github.pagehelper.PageInfo;

import java.util.List;
import java.util.Map;

public interface MessageService {
    /**
     * 获取我发送的消息集合
     * @param vo
     * @return
     */
    PageInfo<Message> getSendList(BaseQueryVo vo);

    /**
     * 插入消息日志表中
     * @param logMap
     */
    void insertMessageLogByList(List<MessageLog> logMap);

    /**
     * 添加消息
     * @param message
     * @param user
     * @param add
     */
    String addMessage(Message message, AuthMember user, String add) throws Exception;

    /**
     * 删除消息
     * @param id
     * @param status
     * @param id
     */
    void removeByIds(String id, String status, String feild);

    /**
     * 获取我接受到的消息
     * @param vo
     * @return
     */
    PageInfo<Message> getReceiveList(BaseQueryVo vo);

    /**
     * 通过用户id 过去未读消息的个数
     * @param uid
     * @return
     */
    List<MessageLog> getMessageCountByUid(String uid);

    /**
     * 获取未读消息个数
     * @param uid
     * @return
     */
    Map<String,String> getMsgCountMap(String uid);
}
