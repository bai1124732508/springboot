package com.fhzm.service;

import com.fhzm.entity.generator.WorldArea;

import java.util.List;

public interface WorldAreaService {
    /**
     * 获取全部的国家
     * @return
     * @param i
     */
    List<WorldArea> getCountryList(int pid);
}
