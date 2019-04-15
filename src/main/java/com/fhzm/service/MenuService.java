package com.fhzm.service;

import com.fhzm.entity.generator.AuthGroup;
import com.fhzm.entity.generator.BaseQueryVo;
import com.fhzm.entity.generator.Menu;
import com.github.pagehelper.PageInfo;

import java.util.List;

public interface MenuService {
    /**
     * 获取所有的菜单
     * @return
     */
    List<Menu> getAllMenuCustom();

    /**
     * 获取菜单集合 通过角色 分组
     * @param authGroup
     * @return
     */
    List<Menu> getMenuByAuthGroup(AuthGroup authGroup);

    /**
     * 通过id 获取菜单信息
     * @param id
     * @return
     */
    Menu getMenuById(String id);

    /**
     * 获取菜单列表
     * @param vo
     * @param pid
     * @param name
     * @return
     */
    PageInfo<Menu> getList(BaseQueryVo vo, String pid, String name);

    /**
     * 根据父id 获取菜单列表
     * @param pid
     * @return
     */
    Menu getCkMenuById(String pid);

    /**
     * 根据菜单名称 获取菜单信息
     * @param title
     * @return
     */
    Menu getMenuByTitle(String title);

    /**
     * 获取自己菜单列表
     * @param idList
     * @return
     */
    List<Menu> getMenuListByPidList(List<String> idList);

    /**
     * 删除选中的菜单
     * @param idList
     */
    void removeByPrimaryKeys(List<String> idList);

    /**
     * 批量更新集合中的菜单
     * @param idList
     * @param status
     * @param status1
     */
    void updateMenuState(List<String> idList, String status, String status1);

    /**
     * 根据utl 获取菜单的信息
     * @param newnrl
     * @return
     */
    Menu getMenuByUrl(String newnrl);
}
