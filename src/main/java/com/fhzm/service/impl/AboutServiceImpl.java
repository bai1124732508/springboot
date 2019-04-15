package com.fhzm.service.impl;

import com.fhzm.dao.generator.AboutAndServiceMapper;
import com.fhzm.entity.generator.AboutAndService;
import com.fhzm.entity.generator.BaseQueryVo;
import com.fhzm.entity.generator.SysLog;
import com.fhzm.service.AboutService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service("aboutAndService")
@Transactional
public class AboutServiceImpl implements AboutService {
    @Autowired
    private AboutAndServiceMapper aboutAndServiceMapper;

    @Override
    public PageInfo<AboutAndService> getAboutList(BaseQueryVo vo) {
        PageHelper.startPage(vo.getPageNum(), vo.getPageSize());
        List<AboutAndService> list= aboutAndServiceMapper.getAboutList(vo);
        PageInfo<AboutAndService> pageInfo = new PageInfo<>(list);
        return pageInfo;
    }

    @Override
    public void updateAboutState(List<String> idList, String status, String is_show) {
        aboutAndServiceMapper.updateAboutState(idList,status,is_show);
    }

    @Override
    public void deleteByLanguageId(String languageId) {
        aboutAndServiceMapper.deleteByLanguageId(languageId);
    }

    @Override
    public AboutAndService getAboutAndServiceBySearch(Map<String, String> search) {
        return aboutAndServiceMapper.getAboutAndServiceBySearch(search);
    }
}
