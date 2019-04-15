package com.fhzm.controller;


import com.fhzm.common.IDBuilder;
import com.fhzm.common.ReturnResult;
import com.fhzm.common.SpringUtils;
import com.fhzm.common.StringUtil;
import com.fhzm.entity.generator.BaseQueryVo;
import com.fhzm.entity.generator.Menu;
import com.fhzm.service.BaseService;
import com.fhzm.service.MenuService;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.*;


@Controller
@Slf4j
@Api(tags = "菜单管理控制器",description="菜单管理控制器",hidden=true)
@RequestMapping("menu")
public class MenuController {

    @Autowired
    private MenuService menuservice;
    @Autowired
    private BaseService baseService;



    /**
     * 菜单管理列表
     * @param vo
     * @param model
     * @param pid
     * @return
     * @throws Exception
     */
    @GetMapping("index")
    @ApiOperation(value = "菜单管理主页" ,  notes="菜单管理主页")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pid", value = "上级的id", required = false, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "level", value = "层级", required = false, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "name", value = "菜单的名称", required = false, paramType = "query", dataType = "String")
    })
    public String index(BaseQueryVo vo, Model model, String pid, String level, String name,HttpSession session) throws Exception{
        PageInfo<Menu> page= menuservice.getList(vo,pid,name);
        String lang = (String) session.getAttribute("lang");
        String title = "";
        if(pid!=null && !"".equals(pid)){
            if(!"0".equals(pid)){
                Menu menu = menuservice.getCkMenuById(pid);
                if(menu!=null){
                    title = menu.getTitle();
                }
            }
        }
        model.addAttribute("page", page);
        model.addAttribute("pid", pid);
        model.addAttribute("level", level);
        model.addAttribute("parentTitle", title);
        return "menu/index";
    }

    /**
     * 跳转到菜单添加页面
     * @param model
     * @param vo
     * @return
     * @throws Exception
     */
    @GetMapping("add")
    public String add(Model model,BaseQueryVo vo,String pid,String level) throws Exception{
        List<Menu> menulist = menuservice.getAllMenuCustom();
        model.addAttribute("menulist",menulist);
        model.addAttribute("pid",pid);
        model.addAttribute("level",level);
        return "menu/add";
    }


    /**
     * 添加菜单
     * @param menu
     * @return
     */
    @PostMapping("add")
    @ResponseBody
    public ReturnResult doAdd(@ModelAttribute Menu menu){
        try{
            Menu custom = menuservice.getMenuByTitle(menu.getTitle());
            if(custom == null){
                menu.setId(IDBuilder.getTableId());
                menu.setStatus(1);
                Menu ckMenuById = menuservice.getCkMenuById(menu.getPid());
                if(ckMenuById!=null){
                    if(ckMenuById.getLevel()==null){
                        menu.setLevel(1);
                    }else{
                        menu.setLevel(ckMenuById.getLevel()+1);
                    }
                }else{
                    menu.setLevel(1);
                }
                menu.setCtime(new Date());
                menu.setUtime(new Date());
                baseService.insertObj(menu);
                return new ReturnResult("1", "操作成功", 3, "/menu/index");
            }
            return ReturnResult.error("菜单名称已经存在");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ReturnResult.error("操作失败");
    }


    /**
     * 跳转到才对那编辑页面
     * @param id
     * @param model
     * @param vo
     * @return
     * @throws Exception
     */
    @RequestMapping(value="edit",method = RequestMethod.GET)
    public String edit(String id,Model model,BaseQueryVo vo) throws Exception{
        Menu menu = menuservice.getCkMenuById(id);
        model.addAttribute("menu",menu);
        List<Menu> menulist = menuservice.getAllMenuCustom();
        model.addAttribute("menulist",menulist);
        return "menu/edit";
    }

    /**
     * 编辑菜单
     * @param menu
     * @return
     */
    @PostMapping("edit")
    @ResponseBody
    public ReturnResult doEdit(@ModelAttribute Menu menu){
        try{
            Menu custom = menuservice.getMenuByTitle(menu.getTitle());
            if(custom!=null && !menu.getId().equals(custom.getId())){
                return ReturnResult.error("菜单名称已经存在");//
            }
            menu.setUtime(new Date());
            baseService.updateObj(menu);
            return new ReturnResult("1", "操作成功", 3, "/menu/index");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ReturnResult.error("操作失败");
    }


    /**
     * 删除菜单操作 永久删除
     * @param id
     * @return
     * @throws Exception
     */
    @PostMapping("remove")
    @ResponseBody
    public ReturnResult remove(String id) throws Exception{
        try {
            String[] ids = id.split(",");
            List<String> idList=new ArrayList<String>();
            for (String string : ids) {
                idList.add(string);
            }
            List<Menu> clist = menuservice.getMenuListByPidList(idList);
            if(clist.size()>0){
                return ReturnResult.error("存在子集菜单，不能删除");
            }
            menuservice.removeByPrimaryKeys(idList);
            return new ReturnResult("1", "操作成功", 3, "/menu/index");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ReturnResult.error("操作失败");
    }


    /**
     * 禁用或者启用的状态
     * @param id
     * @param status
     * @return
     * @throws Exception
     */
    @PostMapping("state")
    @ResponseBody
    public ReturnResult updateState(String id,String status) throws Exception{
        try {
            String[] ids = id.split(",");
            List<String> idList=new ArrayList<String>();
            for (String string : ids) {
                idList.add(string);
            }
            menuservice.updateMenuState(idList,status,"status");
            return new ReturnResult("1", "操作成功", 3, "/menu/index");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ReturnResult.error("操作失败");
    }


    /**
     * 获得面包屑
     * @param url
     * @return
     */
    @ResponseBody
    @RequestMapping("breadcrumb")
    public List<Map<String,String>> getBreadcrumb(String url, HttpServletRequest request, HttpSession session){
        String lang = (String) session.getAttribute("lang");
        String rootPath = request.getScheme()+"://"+request.getServerName();
        if(80==request.getServerPort()) {
            rootPath+="/";
        }else{
            rootPath+= ":"+request.getServerPort()+"/";
        }
        try {
            StringBuilder sb = new StringBuilder();
            url = url.replace(rootPath, "");
            if(StringUtil.isNotNull(url)){
                url = url.substring(1);
            }

            String[] split = url.split("/");
            if(split.length<2){
                return null;
            }
            sb.append(split[0]);
            sb.append("/");
            if(split[1].indexOf("?")>0){
                sb.append(split[1].substring(0,split[1].indexOf("?")));
            }else{
                sb.append(split[1]);
            }
            String  newnrl= sb.toString();
            List<Map<String,String>> list= new ArrayList<Map<String,String>>();
            MenuService  menuService = (MenuService) SpringUtils.getBean("menuService");
            Menu ckMenuCustomByUrl = menuService.getMenuByUrl(newnrl);
            if(ckMenuCustomByUrl!=null){
                Menu ckMenuById = menuService.getCkMenuById(ckMenuCustomByUrl.getPid());
                if("0".equals(ckMenuById.getPid())){//列表
                    Map<String,String> map =new HashMap<String,String>();
                    map.put("title", ckMenuById.getTitle());
                    map.put("url", "");
                    list.add(map);//父标题
                    Map<String,String> map1 =new HashMap<String,String>();//分组
                    map1.put("title",ckMenuCustomByUrl.getGroupId());
                    map1.put("url", "");
                    list.add(map1);
                }else{
                    Menu p = menuService.getCkMenuById(ckMenuById.getPid());
                    Map<String,String> map =new HashMap<String,String>();//祖父标题
                    map.put("title", p.getTitle());
                    map.put("url", "");
                    list.add(map);
                    Map<String,String> map1 =new HashMap<String,String>();//父祖名
                    map1.put("title", ckMenuById.getGroupId());
                    map1.put("url", "");
                    list.add(map1);
                    Map<String,String> map2 =new HashMap<String,String>();//父标题
                    map2.put("title", ckMenuById.getTitle());
                    map2.put("url", ckMenuById.getUrl());
                    list.add(map2);
                }
                Map<String,String> map2 =new HashMap<String,String>();
                map2.put("title", ckMenuCustomByUrl.getTitle());
                map2.put("url", "");
                list.add(map2);
            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }



}
