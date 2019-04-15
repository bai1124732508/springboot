package com.fhzm.service;

import com.fhzm.entity.generator.BaseQueryVo;
import com.fhzm.entity.generator.SysLog;
import com.github.pagehelper.PageInfo;

import java.util.List;
import java.util.Map;

public interface SysLogService {
    /**
     * 获取权限管理 操作日志列表
     * @param vo
     * @return
     */
    PageInfo<SysLog> getList(BaseQueryVo vo);

    /**
     * 清空日志
     * @param search
     */
    void deleteBySearch(Map<String,String> search);

    /**
     * 获取日志集合
     * @param search
     * @return
     */
    List<SysLog> getSyslogList(Map<String,String> search);
}
