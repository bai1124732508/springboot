package com.fhzm.dao.generator;

import com.fhzm.entity.generator.BaseQueryVo;
import com.fhzm.entity.generator.Message;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * * All rights Reserved, Designed By http://www.fenghuaapp.com
 * @Description: MessageMapper
 * @date: 2019-03-28 13:16:21
 * @version: V1.0  
 * @Copyright: 2019 http://www.fenghuaapp.com/ Inc. All rights reserved.
 * 注意：本内容仅限于风华正茂科技(北京)有限公司内部传阅，禁止外泄以及用于其他的商业目的
 */
public interface MessageMapper {
    int deleteByPrimaryKey(String id);

    int insert(Message record);

    int insertSelective(Message record);

    Message selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(Message record);

    int updateByPrimaryKeyWithBLOBs(Message record);

    int updateByPrimaryKey(Message record);

    /**
     * 分页获取发送消息的集合
     * @param vo
     * @return
     */
    List<Message> getSendList(BaseQueryVo vo);

    /**
     * 删除消息 根据id
     * @param list
     * @param status
     * @param feild
     */
    void removeByIds(@Param("list")List<String> list, @Param("status") String status, @Param("fied") String feild);

    /**
     * 获取我接受到的消息
     * @param vo
     * @return
     */
    List<Message> getReceiveList(BaseQueryVo vo);
}