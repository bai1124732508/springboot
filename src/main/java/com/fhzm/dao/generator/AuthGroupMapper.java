package com.fhzm.dao.generator;

import com.fhzm.entity.generator.AuthGroup;
import com.fhzm.entity.generator.BaseQueryVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * * All rights Reserved, Designed By http://www.fenghuaapp.com
 * @Description: AuthGroupMapper
 * @date: 2019-03-21 16:29:37
 * @version: V1.0  
 * @Copyright: 2019 http://www.fenghuaapp.com/ Inc. All rights reserved.
 * 注意：本内容仅限于风华正茂科技(北京)有限公司内部传阅，禁止外泄以及用于其他的商业目的
 */
public interface AuthGroupMapper {
    int deleteByPrimaryKey(String id);

    int insert(AuthGroup record);

    int insertSelective(AuthGroup record);

    AuthGroup selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(AuthGroup record);

    int updateByPrimaryKeyWithBLOBs(AuthGroup record);

    int updateByPrimaryKey(AuthGroup record);

    /**
     *根据用户的id 获取权限
     * @param uid
     * @return
     */
    AuthGroup getRoleListByUserId(String uid);

    /**
     * 分页获取权限角色管理页面
     * @param vo
     * @return
     */
    List<AuthGroup> getRoleByPage(BaseQueryVo vo);

    /**
     * 根据roleID集合改变状态
     * @param list
     * @param status
     */
    void changeRoleStatus(@Param("list") List<String> list,@Param("status")  String status);

    /**获取该用户创建的所有角色
     *
     * @param search
     * @return
     */
    List<AuthGroup> getAuthGroupListBySearch(Map<String,String> search);
}