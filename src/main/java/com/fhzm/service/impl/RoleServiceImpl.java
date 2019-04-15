package com.fhzm.service.impl;

import com.fhzm.dao.generator.AuthGroupMapper;
import com.fhzm.dao.generator.MenuMapper;
import com.fhzm.entity.generator.AuthGroup;
import com.fhzm.entity.generator.BaseQueryVo;
import com.fhzm.entity.generator.Menu;
import com.fhzm.service.RoleService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service("roleService")
@Transactional
public class RoleServiceImpl implements RoleService {
    @Autowired
    private AuthGroupMapper authGroupMapper;
    @Autowired
    private MenuMapper menuMapper;

    @Override
    public AuthGroup getRoleListByUserId(String uid) {
        return authGroupMapper.getRoleListByUserId(uid);
    }

    @Override
    public PageInfo<AuthGroup> getRoleByPage(BaseQueryVo vo) {
        PageHelper.startPage(vo.getPageNum(), vo.getPageSize());
        List<AuthGroup> list= authGroupMapper.getRoleByPage(vo);
        PageInfo<AuthGroup> pageInfo = new PageInfo<>(list);
        return pageInfo;
    }

    @Override
    public void changeRoleStatus(List<String> list, String status) {
        authGroupMapper.changeRoleStatus(list,status);
    }

    @Override
    public List<Menu> getRoleListByType(String type) {
        return menuMapper.getRoleListByType(type);
    }

    @Override
    public List<AuthGroup> getAuthGroupListBySearch(Map<String, String> search) {
        return authGroupMapper.getAuthGroupListBySearch(search);
    }
}
