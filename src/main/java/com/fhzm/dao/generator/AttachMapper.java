package com.fhzm.dao.generator;

import com.fhzm.entity.generator.Attach;

/**
 * * All rights Reserved, Designed By http://www.fenghuaapp.com
 * @Description: AttachMapper
 * @date: 2019-03-28 13:31:43
 * @version: V1.0  
 * @Copyright: 2019 http://www.fenghuaapp.com/ Inc. All rights reserved.
 * 注意：本内容仅限于风华正茂科技(北京)有限公司内部传阅，禁止外泄以及用于其他的商业目的
 */
public interface AttachMapper {
    int deleteByPrimaryKey(String attachId);

    int insert(Attach record);

    int insertSelective(Attach record);

    Attach selectByPrimaryKey(String attachId);

    int updateByPrimaryKeySelective(Attach record);

    int updateByPrimaryKey(Attach record);

    /**
     * 根据hash值获取文件资源
     * @param hash
     * @return
     * @throws Exception
     */
    Attach getAttachByHashcode(String hash) throws Exception;
}