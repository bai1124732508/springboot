package com.fhzm.dao.generator;

import com.fhzm.entity.generator.Configs;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * * All rights Reserved, Designed By http://www.fenghuaapp.com
 * @Description: ConfigsMapper
 * @date: 2019-04-04 10:26:12
 * @version: V1.0  
 * @Copyright: 2019 http://www.fenghuaapp.com/ Inc. All rights reserved.
 * 注意：本内容仅限于风华正茂科技(北京)有限公司内部传阅，禁止外泄以及用于其他的商业目的
 */
public interface ConfigsMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Configs record);

    int insertSelective(Configs record);

    Configs selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Configs record);

    int updateByPrimaryKeyWithBLOBs(Configs record);

    int updateByPrimaryKey(Configs record);


    /**
     * 获取配置项信息
     * @param search
     * @return
     */
    List<Configs> getConfigsListBySearch(Map<String,String> search);

    /**
     * 更新配置文件
     * @param list
     */
    void updateConfigs(@Param("list") List<Configs> list);

    /**
     * 通过id 获取value
     * @param id
     * @return
     */
    String getValueById(Integer id);

}