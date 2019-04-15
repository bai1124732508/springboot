package com.fhzm.controller;

import com.fhzm.annotation.Log;
import com.fhzm.common.IDBuilder;
import com.fhzm.config.WebSocketServer;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;

@Controller
@Slf4j
@Api(tags = "用于测试接口控制器",description="用于测试接口控制器",hidden=true)
public class TestController {

    @GetMapping("/testLog")
    @ResponseBody
    @ApiOperation(value = "测试日志打印" ,  notes="测试日志打印")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "测试参数传递", required = true, paramType = "query", dataType = "String")
    })
    @Log(logType=0,operationName="测试日志打印")
    public String testLog(@RequestParam("userId") String userId,HttpSession session) throws IOException {
        log .debug("=====测试日志debug级别打印====\n");
        log .info("======测试日志info级别打印=====");
        log .info("======测试日志info级别打印=====\n");
        log .error("=====测试日志error级别打印====\n");
        WebSocketServer.send("测试一波!!!", userId);
        return "6666";
    }

    /**
     * 测试websocket 连接
     * @return
     */
    @RequestMapping(value = "/socket",method = RequestMethod.GET)
    public String socket (HashMap<String, Object> map,HttpSession session){
        String id = IDBuilder.getTableId();
        session.setAttribute("id",id);
        map.put("id",id );
        return "websocket/websocket";
    }

}
