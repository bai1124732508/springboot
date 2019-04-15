package com.fhzm.service;

import com.fhzm.entity.generator.AuthMember;

import java.util.Map;

public interface LoginService {
    /**
     * 获取用户的信息 权限以及用户可以访问的url
     * @param user
     * @return
     */
    Map<String,Object> getSessionMapByMember(AuthMember user);
}
