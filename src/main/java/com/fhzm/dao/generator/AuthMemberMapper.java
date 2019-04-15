package com.fhzm.dao.generator;

import com.fhzm.entity.generator.AuthMember;
import com.fhzm.entity.generator.BaseQueryVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * * All rights Reserved, Designed By http://www.fenghuaapp.com
 * @Description: AuthMemberMapper
 * @date: 2019-04-04 10:39:04
 * @version: V1.0  
 * @Copyright: 2019 http://www.fenghuaapp.com/ Inc. All rights reserved.
 * 注意：本内容仅限于风华正茂科技(北京)有限公司内部传阅，禁止外泄以及用于其他的商业目的
 */
public interface AuthMemberMapper {
    int deleteByPrimaryKey(String uid);

    int insert(AuthMember record);

    int insertSelective(AuthMember record);

    AuthMember selectByPrimaryKey(String uid);

    int updateByPrimaryKeySelective(AuthMember record);

    int updateByPrimaryKey(AuthMember record);


    /**
     * 获取系统中是否存在该用户
     * @param member
     * @return
     */
    AuthMember checkAuthMemberByMember(AuthMember member);

    /**
     *通过用户名查找 用户信息
     * @param username
     * @return
     */
    AuthMember getCkAuthMemberCustomByUserName(String username);

    /**
     * 获取成员列表
     * @param vo
     * @return
     */
    List<AuthMember> getMemberList(BaseQueryVo vo);

    /**
     * 验证系统中是否存在相同的手机号 和邮箱
     * @param name
     * @param param
     * @param uid
     * @return
     */
    List<AuthMember> validform(@Param("name") String name, @Param("param") String param, @Param("uid")String uid);

    /**
     *
     * @param list
     * @param status
     * @param fied
     */
    void updateAuthMemberState(@Param("list")List<String> list,@Param("status") String status, @Param("fied")String fied);

    /**
     * 获取用户的列表
     * @param search
     * @return
     */
    List<AuthMember> getUserListBySearch(Map<String,String> search);

    /**
     *获取该账号是否在数据库中存在
     * @param account
     * @return
     */
    AuthMember getAppIsExistenceByAccount(@Param("account")String account);

}