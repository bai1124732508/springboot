package com.fhzm.service;

import com.fhzm.entity.generator.AuthMember;
import com.fhzm.entity.generator.BaseQueryVo;
import com.github.pagehelper.PageInfo;

import java.util.List;
import java.util.Map;

public interface AuthMemberService {
    /**
     * 获取系统中是否存在该用户
     * @param member
     * @return
     */
    AuthMember checkAuthMemberByMember(AuthMember member);

    /**
     * 通过用户名查找 用户信息
     * @param username
     * @return
     */
    AuthMember getCkAuthMemberCustomByUserName(String username);

    /**
     * 获取成员列表
     * @param vo
     * @return
     */
    PageInfo<AuthMember> getMemberList(BaseQueryVo vo);

    /**
     * 验证系统中是否存在相同的手机号 和邮箱
     * @param name
     * @param param
     * @param uid
     * @return
     */
    List<AuthMember> validform(String name, String param, String uid);

    /**
     * 更新成员的信息
     * @param uidList
     * @param s
     * @param is_remove
     */
    void updateAuthMemberState(List<String> uidList, String s, String is_remove);

    /**
     * 获取用户的列表
     * @param search
     * @return
     */
    List<AuthMember> getUserListBySearch(Map<String,String> search);

    /**
     * 获取该账号是否在数据库中存在
     * @param account
     * @return
     */
    AuthMember getAppIsExistenceByAccount(String account);
}
