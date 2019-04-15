package com.fhzm.service.impl;

import com.fhzm.dao.generator.AuthMemberMapper;
import com.fhzm.entity.generator.AuthMember;
import com.fhzm.entity.generator.BaseQueryVo;
import com.fhzm.service.AuthMemberService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.List;
import java.util.Map;


@Service("authMemberService")
@Transactional
public class AuthMemberServiceImpl implements AuthMemberService {
    @Autowired
    private AuthMemberMapper authMemberMapper;

    @Override
    public AuthMember checkAuthMemberByMember(AuthMember member) {
        return authMemberMapper.checkAuthMemberByMember(member);
    }

    @Override
    public AuthMember getCkAuthMemberCustomByUserName(String username) {
        return authMemberMapper.getCkAuthMemberCustomByUserName(username);
    }

    @Override
    public PageInfo<AuthMember> getMemberList(BaseQueryVo vo) {
        PageHelper.startPage(vo.getPageNum(), vo.getPageSize());
        List<AuthMember> list= authMemberMapper.getMemberList(vo);
        PageInfo<AuthMember> pageInfo = new PageInfo<>(list);
        return pageInfo;
    }

    @Override
    public List<AuthMember> validform(String name, String param, String uid) {
        return authMemberMapper.validform( name,  param,uid);
    }

    @Override
    public void updateAuthMemberState(List<String> uidList, String status, String fied) {
        authMemberMapper.updateAuthMemberState(uidList,status,fied);
    }

    @Override
    public List<AuthMember> getUserListBySearch(Map<String, String> search) {
        return authMemberMapper.getUserListBySearch(search);
    }

    @Override
    public AuthMember getAppIsExistenceByAccount(String account) {
        return authMemberMapper.getAppIsExistenceByAccount(account);
    }
}
