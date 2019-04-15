package com.fhzm.entity.generator;

import lombok.Data;

import java.util.Date;

/**
 * * All rights Reserved, Designed By http://www.fenghuaapp.com
 * @Description: Table message_log
 * @date: 2019-04-01 11:00:45
 * @version: V1.0  
 * @Copyright: 2019 http://www.fenghuaapp.com/ Inc. All rights reserved.
 * 注意：本内容仅限于风华正茂科技(北京)有限公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@Data
public class MessageLog {
    /**
     * 文档ID
     * Column: id
     */
    private String id;

    /**
     * 用户的id
     * Column: uid
     */
    private String uid;

    /**
     * 消息的id
     * Column: message_id
     */
    private String messageId;

    /**
     * 消息的分类 0.系统消息 1.告警消息 2.故障消息（Term中配置）
     * Column: type
     */
    private Integer type;

    /**
     * 0.消息未读 1.消息已读
     * Column: is_look
     */
    private Integer isLook;

    /**
     * 创建时间
     * Column: ctime
     */
    private Date ctime;

    /**
     * 创建时间
     * Column: utime
     */
    private Date utime;


    public MessageLog(){
        super();
    }
    public MessageLog(String id, String uid, String messageId,Integer type, Integer isLook , Date ctime, Date utime) {
        this.id = id;
        this.uid = uid;
        this.messageId = messageId;
        this.type = type;
        this.isLook = isLook;
        this.ctime = ctime;
        this.ctime = utime;
    }



}