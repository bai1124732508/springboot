package com.fhzm.controller;

import com.fhzm.common.POIUtils;
import com.fhzm.common.ReturnResult;
import com.fhzm.entity.generator.BaseQueryVo;
import com.fhzm.entity.generator.SysLog;
import com.fhzm.service.SysLogService;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@Slf4j
@Api(tags = "权限管理-日志管理控制器",description="权限管理-日志管理控制器",hidden=true)
@RequestMapping("permission_log")
@Data
public class PermissionLogController {

    @Autowired
    private SysLogService sysLogService;


    /**
     *
     * @param vo
     * @param model
     * @return
     * @throws Exception
     */
    @GetMapping("index")
    public String index(BaseQueryVo vo, Model model) throws Exception{
        if(vo==null){
            vo = new BaseQueryVo();
        }
        Map<String, String> search = vo.getSearch();
        if(search==null){
            search = new HashMap<String,String>();
        }
        search.put("logType","2");
        vo.setSearch(search);
        PageInfo<SysLog> page= sysLogService.getList(vo);
        model.addAttribute("page", page);
        model.addAttribute("search", search);
        return "permission_log/index";
    }

    /**
     * 清空日志
     * @return
     */
    @PostMapping("delete")
    @ResponseBody
    public ReturnResult delete(){
        try{
            Map<String, String> search = new HashMap<String,String>();
            search.put("logType","2");
            sysLogService.deleteBySearch(search);
            return ReturnResult.ok("操作成功");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ReturnResult.error("操作失败");
    }

    /**
     * 导出日志excel
     * @return
     */
    @RequestMapping(value="export",method = RequestMethod.GET)
    public void exportExcel(BaseQueryVo vo, HttpServletResponse response, String logType){
        try {
            Map<String,String> search = new HashMap<String,String>();
            search.put("logType", logType);
            List<SysLog> list= sysLogService.getSyslogList(search);
            String[] titleNames = new String[] {  "日志时间","终端设备", "操作类型","操作人员", "IP地址","角色名称", "内容",
                    };
            String[] titleColumns = new String[] {  "ctimeStr","equipmentTypeStr", "logTypeStr", "createName", "ip", "authGroupName", "description"};
            int titleSize[] = {20, 10, 10, 10,10,10,30};
            POIUtils poi = new POIUtils(response, "操作日志", "操作日志");
            poi.wirteExcel(titleColumns, titleNames, titleSize, list);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
