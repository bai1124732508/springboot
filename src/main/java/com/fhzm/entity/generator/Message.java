package com.fhzm.entity.generator;

import java.util.Date;

import com.fhzm.common.file.Term;
import com.fhzm.common.file.TermManager;
import lombok.Data;

/**
 * * All rights Reserved, Designed By http://www.fenghuaapp.com
 * @Description: Table message
 * @date: 2019-03-28 13:16:21
 * @version: V1.0  
 * @Copyright: 2019 http://www.fenghuaapp.com/ Inc. All rights reserved.
 * 注意：本内容仅限于风华正茂科技(北京)有限公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@Data
public class Message {
    /**
     * 文档ID
     * Column: id
     */
    private String id;

    /**
     * 标题
     * Column: title
     */
    private String title;

    /**
     * Column: create_uid
     */
    private String createUid;

    /**
     * 角色分组的id
     * Column: group_id
     */
    private String groupId;

    /**
     * 消息的分类 0.系统消息 1.告警消息 2.故障消息（Term中配置）
     * Column: type
     */
    private Integer type;

    /**
     * 状态,默认为1 ，2 已发布
     * Column: status
     */
    private Integer status;

    /**
     * 是否删除 0.没有删除 1.已经删除
     * Column: is_remove
     */
    private Integer isRemove;

    /**
     * 创建时间
     * Column: ctime
     */
    private Date ctime;

    /**
     * 修改时间
     * Column: utime
     */
    private Date utime;

    /**
     * 消息内容
     * Column: info
     */
    private String info;

    /**
     * 接收用户的id （逗号分隔）
     * Column: receive_uid
     */
    private String receiveUid;

    /**
     * 创建用户的名称
     */
    private String authMemberName;

    private String logId;

    private String statusStr;

    private String typeStr;

    public String getStatusStr() {
        Term term = TermManager.getTerm("message_status", String.valueOf(this.getStatus()));
        if( term != null ) {
            statusStr = term.getName();
        }
        return statusStr;
    }

    public String getTypeStr() {
        Term term = TermManager.getTerm("message_type", String.valueOf(this.getType()));
        if( term != null ) {
            typeStr = term.getName();
        }
        return typeStr;
    }

}