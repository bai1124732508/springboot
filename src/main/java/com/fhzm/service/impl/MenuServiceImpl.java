package com.fhzm.service.impl;

import com.fhzm.dao.generator.MenuMapper;
import com.fhzm.entity.generator.AuthGroup;
import com.fhzm.entity.generator.BaseQueryVo;
import com.fhzm.entity.generator.Menu;
import com.fhzm.service.MenuService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("menuService")
@Transactional
public class MenuServiceImpl implements MenuService {
    @Autowired
    private MenuMapper menuMapper;

    @Override
    public List<Menu> getAllMenuCustom() {
        return menuMapper.getAllMenuCustom();
    }

    @Override
    public List<Menu> getMenuByAuthGroup(AuthGroup authGroup) {
        String rules = authGroup.getRules();
        List<String> menuIdList = Arrays.asList(rules.split(","));
        if(menuIdList.size()>0){
            List<Menu> menuList= menuMapper.getMenuListByMenuIdList(menuIdList);
            return menuList;
        }else{
            return null;
        }
    }

    @Override
    public Menu getMenuById(String id) {
        return menuMapper.selectByPrimaryKey(id);
    }

    @Override
    public PageInfo<Menu> getList(BaseQueryVo vo, String pid, String name) {
        PageHelper.startPage(vo.getPageNum(), vo.getPageSize());
        Map<String, String> search = vo.getSearch();
        if(search == null){
            search = new HashMap<String,String>();
        }
        if(pid!=null && !"".equals(pid)){
            search.put("pid", pid);
        }else{
            search.put("pid", "0");
        }
        search.put("name", name);
        vo.setSearch(search);
        List<Menu> list = menuMapper.getListByVo(vo);
        PageInfo<Menu> pageInfo = new PageInfo<>(list);
        return pageInfo;

    }

    @Override
    public Menu getCkMenuById(String pid) {
        return menuMapper.selectByPrimaryKey(pid);
    }

    @Override
    public Menu getMenuByTitle(String title) {
        return menuMapper.getMenuByTitle(title);
    }

    @Override
    public List<Menu> getMenuListByPidList(List<String> idList) {
        return menuMapper.getMenuListByPidList(idList);
    }

    @Override
    public void removeByPrimaryKeys(List<String> idList) {
        menuMapper.removeByPrimaryKeys(idList);
    }

    @Override
    public void updateMenuState(List<String> idList, String status, String fied) {
        menuMapper.updateMenuState(idList, status, fied);
    }

    @Override
    public Menu getMenuByUrl(String url) {
        return menuMapper.getMenuByUrl(url);
    }
}
