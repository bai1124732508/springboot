package com.fhzm.service;

import com.fhzm.entity.generator.Configs;

import java.util.List;
import java.util.Map;

public interface ConfigsService {
    /**
     * 获取配置项集合
     * @param search
     * @return
     */
    List<Configs> getConfigsListBySearch(Map<String,String> search);

    /**
     * 更新配置文件表
     * @param list
     */
    void updateConfigs(List<Configs> list);

    /**
     * 通过id 获取value
     * @param id
     * @return
     */
    String getValueById(Integer id);
}
