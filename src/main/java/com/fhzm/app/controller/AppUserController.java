package com.fhzm.app.controller;

import com.fhzm.common.JsonUtils;
import com.fhzm.common.Jwt.JwtUtils;
import com.fhzm.common.ReturnResult;
import com.fhzm.entity.generator.AuthMember;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;

/**
 * 用于用户登录之后的完善信息、更新用户的操作
 * * All rights Reserved, Designed By http://www.fenghuaapp.com
 * @Description: AboutAndServiceMapper
 * @date: 2019-03-29 14:14:20
 * @version: V1.0
 * @Copyright: 2019 http://www.fenghuaapp.com/ Inc. All rights reserved.
 * 注意：本内容仅限于风华正茂科技(北京)有限公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@Controller
@Slf4j
@Data
@Api(tags = "APP用户信息控制器",description="APP用户信息控制器",hidden=true)
@RequestMapping("app/user")
public class AppUserController {


    @GetMapping("get_user")
    @ResponseBody
    @ApiOperation(value = "获取用户信息" ,  notes="获取用户信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "accessToken", value = "accessToken", required = true, paramType = "query", dataType = "String")
    })
    public ReturnResult getUser(String accessToken) throws IOException {
        try{
            String str = JwtUtils.getUserStrByJWT(accessToken);
            AuthMember user = JsonUtils.toBean(str,AuthMember.class);
            System.out.println(user.getUid());
            return ReturnResult.ok("1", "操作成功", user);
        }catch (Exception e) {
            e.printStackTrace();
            return ReturnResult.error("操作失败");
        }
    }


}
