package com.fhzm.dao.generator;

import com.fhzm.entity.generator.WorldArea;

import java.util.List;

/**
 * * All rights Reserved, Designed By http://www.fenghuaapp.com
 * @Description: WorldAreaMapper
 * @date: 2019-03-27 10:11:04
 * @version: V1.0  
 * @Copyright: 2019 http://www.fenghuaapp.com/ Inc. All rights reserved.
 * 注意：本内容仅限于风华正茂科技(北京)有限公司内部传阅，禁止外泄以及用于其他的商业目的
 */
public interface WorldAreaMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(WorldArea record);

    int insertSelective(WorldArea record);

    WorldArea selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(WorldArea record);

    int updateByPrimaryKey(WorldArea record);

    /**
     * 获取所有国家的集合
     * @return
     */
    List<WorldArea> getCountryList(int pid);
}