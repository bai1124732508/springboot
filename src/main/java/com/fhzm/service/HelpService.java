package com.fhzm.service;

import com.fhzm.entity.generator.BaseQueryVo;
import com.fhzm.entity.generator.Help;
import com.github.pagehelper.PageInfo;

import java.util.List;

public interface HelpService {
    /**
     * 获取帮组列表
     * @param vo
     * @return
     */
    PageInfo<Help> getList(BaseQueryVo vo);

    /**
     * 更新状态
     * @param idList
     * @param status
     * @param is_show
     */
    void updateState(List<String> idList, String status, String is_show);
}
