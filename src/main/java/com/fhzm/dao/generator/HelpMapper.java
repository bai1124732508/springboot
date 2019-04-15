package com.fhzm.dao.generator;

import com.fhzm.entity.generator.BaseQueryVo;
import com.fhzm.entity.generator.Help;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * * All rights Reserved, Designed By http://www.fenghuaapp.com
 * @Description: HelpMapper
 * @date: 2019-04-04 11:00:45
 * @version: V1.0  
 * @Copyright: 2019 http://www.fenghuaapp.com/ Inc. All rights reserved.
 * 注意：本内容仅限于风华正茂科技(北京)有限公司内部传阅，禁止外泄以及用于其他的商业目的
 */
public interface HelpMapper {
    int deleteByPrimaryKey(String id);

    int insert(Help record);

    int insertSelective(Help record);

    Help selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(Help record);

    int updateByPrimaryKeyWithBLOBs(Help record);

    int updateByPrimaryKey(Help record);



    /**
     * 获取使用帮助
     * @param vo
     * @return
     */
    List<Help> getList(BaseQueryVo vo);

    /**
     * 更改状态
     * @param idList
     * @param status
     * @param fied
     */
    void updateState(@Param("list")List<String> idList, @Param("status")String status, @Param("fied")String fied);


}