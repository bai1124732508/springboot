package com.fhzm.dao.generator;

import com.fhzm.entity.generator.BaseQueryVo;
import com.fhzm.entity.generator.Menu;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * * All rights Reserved, Designed By http://www.fenghuaapp.com
 * @Description: MenuMapper
 * @date: 2019-04-04 10:16:16
 * @version: V1.0  
 * @Copyright: 2019 http://www.fenghuaapp.com/ Inc. All rights reserved.
 * 注意：本内容仅限于风华正茂科技(北京)有限公司内部传阅，禁止外泄以及用于其他的商业目的
 */
public interface MenuMapper {
    int deleteByPrimaryKey(String id);

    int insert(Menu record);

    int insertSelective(Menu record);

    Menu selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(Menu record);

    int updateByPrimaryKey(Menu record);

    /**
     * 获取所有的菜单列表
     * @return
     */
    List<Menu> getAllMenuCustom();

    /**
     * 通过菜单id 集合去寻找 菜单
     * @param menuIdList
     * @return
     */
    List<Menu> getMenuListByMenuIdList(@Param("list")List<String> menuIdList);

    /**
     *  菜单管理列表
     * @param vo
     * @return
     */
    List<Menu> getListByVo(BaseQueryVo vo);

    /**
     *根据菜单名称 获取菜单信息
     * @param title
     * @return
     */
    Menu getMenuByTitle(String title);

    /**
     * 获取菜单列表通过 父级id
     * @param idList
     * @return
     */
    List<Menu> getMenuListByPidList(@Param("list")List<String> idList);

    /**
     * 批量删除菜单
     * @param idList
     */
    void removeByPrimaryKeys(@Param("list")List<String> idList);

    /**
     * 批量更新集合中的数据
     * @param idList
     * @param status
     * @param fied
     */
    void updateMenuState(@Param("list")List<String> idList, @Param("status")String status, @Param("fied")String fied);

    /**
     * 通过url 获取菜单的信息
     * @param url
     * @return
     */
    Menu getMenuByUrl(String url);

    /**
     * 获取一级 菜单列表
     * @param type
     * @return
     */
    List<Menu> getRoleListByType(String type);






}