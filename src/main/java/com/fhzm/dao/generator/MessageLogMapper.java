package com.fhzm.dao.generator;

import com.fhzm.entity.generator.MessageLog;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * * All rights Reserved, Designed By http://www.fenghuaapp.com
 * @Description: MessageLogMapper
 * @date: 2019-04-01 11:00:45
 * @version: V1.0  
 * @Copyright: 2019 http://www.fenghuaapp.com/ Inc. All rights reserved.
 * 注意：本内容仅限于风华正茂科技(北京)有限公司内部传阅，禁止外泄以及用于其他的商业目的
 */
public interface MessageLogMapper {
    int deleteByPrimaryKey(String id);

    int insert(MessageLog record);

    int insertSelective(MessageLog record);

    MessageLog selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(MessageLog record);

    int updateByPrimaryKey(MessageLog record);


    /**
     * 插入日志记录
     * @param list
     */
    void insertMessageLogByList(@Param("list") List<MessageLog> list);


    /**
     *通过用户id 过去未读消息的个数
     * @param uid
     * @return
     */
    List<MessageLog> getMessageCountByUid(String uid);

}