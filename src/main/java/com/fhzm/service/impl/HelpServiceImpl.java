package com.fhzm.service.impl;

import com.fhzm.dao.generator.HelpMapper;
import com.fhzm.entity.generator.AboutAndService;
import com.fhzm.entity.generator.BaseQueryVo;
import com.fhzm.entity.generator.Help;
import com.fhzm.service.HelpService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service("helpService")
@Transactional
public class HelpServiceImpl implements HelpService {
    @Autowired
    private HelpMapper helpMapper;

    @Override
    public PageInfo<Help> getList(BaseQueryVo vo) {
        PageHelper.startPage(vo.getPageNum(), vo.getPageSize());
        List<Help> list= helpMapper.getList(vo);
        PageInfo<Help> pageInfo = new PageInfo<>(list);
        return pageInfo;
    }

    @Override
    public void updateState(List<String> idList, String status, String is_show) {
        helpMapper.updateState(idList,status,is_show);
    }
}
