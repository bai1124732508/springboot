package com.fhzm.service.impl;

import com.fhzm.common.JsonUtils;
import com.fhzm.common.SortListUtil;
import com.fhzm.dao.generator.AuthMemberMapper;
import com.fhzm.entity.generator.AuthGroup;
import com.fhzm.entity.generator.AuthMember;
import com.fhzm.entity.generator.Menu;
import com.fhzm.service.LoginService;
import com.fhzm.service.MenuService;
import com.fhzm.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

import static com.fhzm.common.IpgetUtil.getIp;

@Service("loginService")
@Transactional
public class LoginServiceImpl implements LoginService {
    @Autowired
    private MenuService meunService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private AuthMemberMapper authMemberMapper;



    @Override
    public Map<String, Object> getSessionMapByMember(AuthMember user) {
        Map<String,Object> session=new HashMap<String, Object>();
        //用户验证通过  角色集合
        List<Menu> menuList =null;;
        if(user.getUid().equals("1")){//admin 登录拥有所有列表
            menuList = meunService.getAllMenuCustom();
        }else{
            AuthGroup authGroup = roleService.getRoleListByUserId(user.getUid());
            if(authGroup != null ){
                menuList = meunService.getMenuByAuthGroup(authGroup);
            }
        }
        if(menuList==null){
            menuList = new ArrayList<>();
        }
        if(menuList.size()>0){
            List<Menu> classMenulist = new ArrayList<Menu>();
            Map<String,List<Menu>> map=new  HashMap<String,List<Menu>>();
            List<String> urlList=new ArrayList<String>();
            for(Menu meun:menuList){  //按照pid进行分组
                urlList.add(meun.getUrl());
                if(map.containsKey(meun.getPid())){
                    List<Menu> meplist=map.get(meun.getPid());
                    meplist.add(meun);
                    map.put(meun.getPid(), meplist);
                }
                else{
                    List<Menu> meplist=new ArrayList<Menu>();
                    meplist.add(meun);
                    map.put(meun.getPid(), meplist);
                }
            }
            List<Menu> allsonFathList=new ArrayList<Menu>();
            for(Map.Entry<String,List<Menu>>  entry:map.entrySet()){
                String id=entry.getKey();  //根据pid进行封装
                if(!"0".equals(id)){
                    List<Menu> menulist=entry.getValue();
                    for(Menu menu:menulist){  //该pid下子集进行封装
                        menu.setRuleCustomList(map.get(menu.getId()));
                    }
                    Menu meunCustom = meunService.getMenuById(id);
                    if(meunCustom!=null){
                        meunCustom.setRuleCustomList(menulist);
                    }else{
                        System.out.println("-----------------:"+id);
                    }
                    allsonFathList.add(meunCustom);
                }
            }
            //首先将父id为0放入集合
            for(Menu menu:allsonFathList){
                if("0".equals(menu.getPid())){
                    classMenulist.add(menu);
                }
            }
            //更新最后登录时间以及ip
            user.setLastLoginTime(new Date());
            authMemberMapper.updateByPrimaryKeySelective(user);
            List <Menu>  list = (List<Menu>) SortListUtil.sort(classMenulist,"sort",SortListUtil.ASC);
            session.put("user", user);
            session.put("menulist", list);
            session.put("urlList", urlList);
            session.put("menulistJson", JsonUtils.toJSONString(classMenulist));
        }
        return session;
    }
}
