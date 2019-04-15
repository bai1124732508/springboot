package com.fhzm.interceptor;

import com.fhzm.aspect.LoginEnum;
import com.fhzm.common.JsonUtils;
import com.fhzm.common.Jwt.JwtResult;
import com.fhzm.common.Jwt.JwtUtils;
import com.fhzm.common.LoginCookieUtil;
import com.fhzm.common.ReturnResult;
import com.fhzm.common.StringUtil;
import com.fhzm.entity.generator.AuthMember;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;

@Slf4j
public class LoginInterceptor  implements HandlerInterceptor {

    //执行Handler方法完成后执行
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse arg1, Object arg2, Exception arg3)
            throws Exception {
        String url = request.getRequestURI();

    }

    //进入Handler方法之后，返回ModelAndView前执行
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse arg1, Object arg2, ModelAndView arg3)
            throws Exception {
    }

    //进入Handler方法之前执行
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object object) throws Exception {
        String contextPath = request.getContextPath();
        String lang = request.getParameter("lang");
        HttpSession session = request.getSession();
        String url = request.getRequestURI();
        if("/error".equals(url)){
            response.sendRedirect(contextPath+"/redirect/error");
            return false;
        }
        if( url.indexOf("menu/breadcrumb") > 0 //面包屑
                || url.indexOf("/swagger")>=0
                || url.indexOf("/websocket")>=0  //websocket连接
                || url.indexOf("/common")>=0 //工具类空值器
                || url.indexOf("/app/login")>=0 //app 注册登录控制器 方法
                || url.indexOf("/redirect/error")>=0 || url.indexOf("/login/logout")>=0 || url.indexOf("/v2")>=0 //生成接口文档
                ){
            return true;
        }

        if(url.indexOf("/app")>=0){ //app 请求 判断有无登录
            response.setHeader("Content-type", "text/html;charset=UTF-8");
            //获取用户凭证
            String token = request.getParameter("accessToken");
            if( StringUtils.isEmpty(token) ){
                response.getWriter().println(JsonUtils.toJSONString(ReturnResult.error("0","accessToken不能为空")));
                return false;
            }
            /**
             * 判断有无用户信息 有无失效等
             */
            JwtResult result = JwtUtils.parseJWT(token);
            if("0".equals(result.getCode())){ //成功
                Claims claims = result.getClaims();
                if( claims == null ||  claims.getSubject() == null  ) {
                    response.getWriter().println(JsonUtils.toJSONString(ReturnResult.error("0","失效，请重新登录")));
                    return false;
                }
                return true;
            }
            if("1".equals(result.getCode())){ //超时
                response.getWriter().println(JsonUtils.toJSONString(ReturnResult.error("0","失效，请重新登录")));
                return false;
            }
            if("2".equals(result.getCode())){ //报错
                response.getWriter().println(JsonUtils.toJSONString(ReturnResult.error("0","验证码错误")));
                return false;
            }
        }
        AuthMember authMember=(AuthMember) session.getAttribute("user");
        LoginEnum loginEnum = LoginEnum.COOKIE_NULL ;
        if(authMember != null){
            loginEnum = LoginEnum.SUCCESS;
        }else{
            loginEnum = LoginCookieUtil.readCookieAndLogon(request, response);
        }
        if(url.indexOf("/user/login") >= 0){
            boolean result = true;
            switch (loginEnum) {
                case SUCCESS:
                    response.sendRedirect(contextPath+"/");
                    result = false;
                    break;
                case MALICE:
                    response.sendRedirect(contextPath+"/redirect/error");
                    result = false;
                    break;
                case COOKIE_TIME_OUT:
                    request.setAttribute("login_error", "俩周期限到了，登录信息已过期，请重新登录!");
                    break;
                default:
                    result = true;
                    break;
            }
            return result;
        }
        if(authMember != null){//如果存在放行
            if(authMember.getUid().equals("1")){
                return true;
            }
            if(url.indexOf("/home/index")>=0 || url.indexOf("/home/public")>=0 || url.indexOf("/")>=0){
                return true;
            }
            List<String> urlList= (List<String>) session.getAttribute("urlList");
            url = url.substring(1, url.length());
            if(urlList.contains(url)){
                return true;
            }else{
                log.info("权限不含有页面---------------------------------"+url);
                response.sendRedirect(contextPath+"/redirect/error");
                return false;
            }
        } else{
            response.sendRedirect(contextPath+"/user/login");
            return false;
        }
    }
}
