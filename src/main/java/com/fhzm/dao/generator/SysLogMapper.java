package com.fhzm.dao.generator;

import com.fhzm.entity.generator.BaseQueryVo;
import com.fhzm.entity.generator.SysLog;

import java.util.List;
import java.util.Map;

/**
 * * All rights Reserved, Designed By http://www.fenghuaapp.com
 * @Description: SysLogMapper
 * @date: 2019-03-26 15:03:12
 * @version: V1.0  
 * @Copyright: 2019 http://www.fenghuaapp.com/ Inc. All rights reserved.
 * 注意：本内容仅限于风华正茂科技(北京)有限公司内部传阅，禁止外泄以及用于其他的商业目的
 */
public interface SysLogMapper {
    int deleteByPrimaryKey(String id);

    int insert(SysLog record);

    int insertSelective(SysLog record);

    SysLog selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(SysLog record);

    int updateByPrimaryKeyWithBLOBs(SysLog record);

    int updateByPrimaryKey(SysLog record);

    /**
     *获取权限管理 操作日志列表
     * @param vo
     * @return
     */
    List<SysLog> getList(BaseQueryVo vo);

    /**
     * 清空日志
     * @param search
     */
    void deleteBySearch(Map<String,String> search);

    /**
     * 获取权限管理
     * @param search
     * @return
     */
    List<SysLog> getSyslogList(Map<String,String> search);
}