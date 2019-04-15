package com.fhzm.service.impl;

import com.fhzm.dao.generator.WorldAreaMapper;
import com.fhzm.entity.generator.WorldArea;
import com.fhzm.service.WorldAreaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service("worldAreaService")
@Transactional
public class WorldAreaServiceImpl implements WorldAreaService {
    @Autowired
    private WorldAreaMapper worldAreaMapper;

    @Override
    public List<WorldArea> getCountryList(int pid) {
        return worldAreaMapper.getCountryList(pid);
    }
}
