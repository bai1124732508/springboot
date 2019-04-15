package com.fhzm.controller;


import com.fhzm.common.FileUtils;
import com.fhzm.common.IDBuilder;
import com.fhzm.common.JsonUtils;
import com.fhzm.entity.generator.Attach;
import com.fhzm.entity.generator.AuthMember;
import com.fhzm.entity.generator.WorldArea;
import com.fhzm.service.AttachService;
import com.fhzm.service.AuthMemberService;
import com.fhzm.service.BaseService;
import com.fhzm.service.WorldAreaService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;

@Controller
@Slf4j
@Api(tags = "工具类控制器",description="工具类控制器",hidden=true)
@RequestMapping("common")
public class CommonController {
    @Autowired
    private WorldAreaService worldAreaService;
    @Value("${file.path}")
    private String filePath;//虚拟路径
    @Autowired
    private AttachService attachService;
    @Autowired
    private BaseService baseService;
    @Autowired
    private AuthMemberService authMemberService;


    /**
     *  获取国家的省份 集合通过国家的id
     * @param id
     * @return
     * @throws Exception
     */
    @GetMapping(value="getProvinceByPid")
    @ResponseBody
    public List<WorldArea>  getProvinceByPid(String id) throws Exception{
        /**
         * 查询所有国家 集合
         */
        if(StringUtils.isNotEmpty(id)){
            List<WorldArea> areaList = worldAreaService.getCountryList(Integer.parseInt(id));
            return areaList;
        }
        return null;
    }

    /**
     * 上传图片
     * @param file
     * @return
     */
    @RequestMapping(value = "uploadImg")
    @ResponseBody
    public void editorUploadImg(@RequestParam(value="file",required=false) MultipartFile file, HttpServletRequest request, HttpServletResponse response) throws IOException {
        //response.addHeader("Content-Type", "text/html");
        List<Map<String,Object>> list = new ArrayList<>();
        Map<String,Object>  map = new HashMap<String, Object>();
        //返回FastDFS配置好的图片路径
        //正常上传
        try {
            if (file != null) {
                String hash = FileUtils.getHash(file.getInputStream(),"SHA1");
                Attach attach= attachService.getAttachByHashcode(hash);
                if(attach!=null){ //

                    map.put("code", 200);
                    map.put("attachId", attach.getAttachId());
                    map.put("msg", "success");
                    //list.add(map);
                    response.setContentType("application/json; charset=UTF-8");
                    response.getWriter().print(JsonUtils.toJSONString(map));
                }
                else{//上传
                    attach=new Attach();
                    attach.setAttachId(IDBuilder.getTableId());
                    attach.setCtime(new Date());
                    attach.setName(file.getOriginalFilename());
                    attach.setType(file.getContentType());
                    attach.setSize(String.valueOf(file.getSize()));
                    String fileName = file.getOriginalFilename();
                    String prefix=fileName.substring(fileName.lastIndexOf(".")+1);
                    attach.setExtension(prefix);
                    attach.setHash(hash);
                    attach.setFrom((byte)1);
                    attach.setMime(file.getContentType());
                    BufferedImage image = ImageIO.read(file.getInputStream());
                    if(image!=null){
                        attach.setWidth(image.getWidth());
                        attach.setHeight(image.getHeight());
                    }
                    //按时间拼接存储地址
                    StringBuilder sb = new StringBuilder();
                    Calendar now = Calendar.getInstance();
                    sb.append(now.get(Calendar.YEAR)+"/");
                    sb.append(now.get(Calendar.MONTH)+"/");
                    sb.append(now.get(Calendar.DAY_OF_MONTH)+"/");
                    String path =  sb.toString();
                    String savename = saveFile(file,filePath+path);
                    attach.setSaveName(savename);
                    attach.setSavePath(path);
                    baseService.insertObj(attach);
                    map.put("code", 200);
                    map.put("attachId", attach.getAttachId());
                    map.put("msg", "success");
                    //list.add(map);
                    response.setContentType("application/json; charset=UTF-8");
                    response.getWriter().print(JsonUtils.toJSONString(map));
                }

            }
        }catch (Exception e) {
            e.printStackTrace();
            map.put("code", 500);
            map.put("imageurl", "");
            map.put("attachId", "");
            map.put("msg", "error");
            map.put("prefix", "");
            map.put("name","");
            response.setContentType("application/json; charset=UTF-8");
            response.getWriter().print(JsonUtils.toJSONString(map));
        }

    }


    /**
     * 保存文件
     * @param file
     * @param filePath
     * @return
     * @throws IOException
     */
    public static String saveFile(MultipartFile file, String filePath)throws IOException {
        String originalFileName = file.getOriginalFilename();
        String prefix=originalFileName.substring(originalFileName.lastIndexOf(".")+1);
        String fileFullName =  IDBuilder.getTableId()+"."+prefix;
        //保存的地址
        File newFile=new File(filePath);
        if(!newFile.exists()){
            newFile.mkdirs();
        }
        file.transferTo(new File(filePath + fileFullName));
        return fileFullName;
    }


    /**
     * 通过角色的id  获取下面所有的用户集合
     * @param id
     * @return
     * @throws Exception
     */
    @GetMapping(value="getUserListByRoleId")
    @ResponseBody
    public List<AuthMember>  getUserListByRoleId(String id) throws Exception{
        Map<String, String> search = new HashMap<>();
        /**
         * 查询所有国家 集合
         */
        if(StringUtils.isNotEmpty(id)){
            search.put("groupId", id);
            List<AuthMember> userList = authMemberService.getUserListBySearch(search);
            return userList;
        }else{
            List<AuthMember> userList = authMemberService.getUserListBySearch(search);
            return userList;
        }

    }


}
