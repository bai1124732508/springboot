package com.fhzm.dao.generator;

import com.fhzm.entity.generator.AuthGroup;
import com.fhzm.entity.generator.AuthGroupAccess;

/**
 * * All rights Reserved, Designed By http://www.fenghuaapp.com
 * @Description: AuthGroupAccessMapper
 * @date: 2019-03-25 16:47:51
 * @version: V1.0  
 * @Copyright: 2019 http://www.fenghuaapp.com/ Inc. All rights reserved.
 * 注意：本内容仅限于风华正茂科技(北京)有限公司内部传阅，禁止外泄以及用于其他的商业目的
 */
public interface AuthGroupAccessMapper {
    int insert(AuthGroupAccess record);

    int insertSelective(AuthGroupAccess record);

    AuthGroupAccess selectByPrimaryKey(String uid);

    int updateByPrimaryKeySelective(AuthGroupAccess record);

}