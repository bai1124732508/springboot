package com.fhzm.service.impl;

import com.fhzm.dao.generator.ConfigsMapper;
import com.fhzm.entity.generator.Configs;
import com.fhzm.service.ConfigsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service("configsService")
@Transactional
public class ConfigsServiceImpl implements ConfigsService {
    @Autowired
    private ConfigsMapper configsMapper;

    @Override
    public List<Configs> getConfigsListBySearch(Map<String, String> search) {
        return configsMapper.getConfigsListBySearch(search);
    }

    @Override
    public void updateConfigs(List<Configs> list) {
        configsMapper.updateConfigs(list);
    }

    @Override
    public String getValueById(Integer id) {
        return configsMapper.getValueById(id);
    }
}
