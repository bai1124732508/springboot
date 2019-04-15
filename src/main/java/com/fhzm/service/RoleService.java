package com.fhzm.service;

import com.fhzm.entity.generator.AuthGroup;
import com.fhzm.entity.generator.BaseQueryVo;
import com.fhzm.entity.generator.Menu;
import com.github.pagehelper.PageInfo;

import java.util.List;
import java.util.Map;

public interface RoleService {
    /**
     * 根据用户的id 获取权限
     * @param uid
     * @return
     */
    AuthGroup getRoleListByUserId(String uid);

    /**
     * 分页获取 权限页面
     * @param vo
     * @return
     */
    PageInfo<AuthGroup> getRoleByPage(BaseQueryVo vo);

    /**
     * 根据roleID集合改变状态
     * @param list
     * @param status
     */
    void changeRoleStatus(List<String> list, String status);

    /**
     * 获取所有一级菜单
     * @param type
     * @return
     */
    List<Menu> getRoleListByType(String type);

    /**
     * 获取该用户创建的所有角色
     * @param search
     * @return
     */
    List<AuthGroup> getAuthGroupListBySearch(Map<String,String> search);
}
