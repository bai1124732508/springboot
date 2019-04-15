package com.fhzm.service.impl;

import com.fhzm.dao.generator.SysLogMapper;
import com.fhzm.entity.generator.AuthGroup;
import com.fhzm.entity.generator.BaseQueryVo;
import com.fhzm.entity.generator.SysLog;
import com.fhzm.service.SysLogService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service("sysLogService")
@Transactional
public class SysLogServiceImpl implements SysLogService {
    @Autowired
    private SysLogMapper sysLogMapper;

    @Override
    public PageInfo<SysLog> getList(BaseQueryVo vo) {
        PageHelper.startPage(vo.getPageNum(), vo.getPageSize());
        List<SysLog> list= sysLogMapper.getList(vo);
        PageInfo<SysLog> pageInfo = new PageInfo<>(list);
        return pageInfo;
    }

    @Override
    public void deleteBySearch(Map<String, String> search) {
        sysLogMapper.deleteBySearch(search);
    }

    @Override
    public List<SysLog> getSyslogList(Map<String, String> search) {
        return sysLogMapper.getSyslogList(search);
    }
}
