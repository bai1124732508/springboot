package com.fhzm.common;

import com.fhzm.aspect.LoginEnum;
import com.fhzm.entity.generator.AuthMember;
import com.fhzm.service.AuthMemberService;
import com.fhzm.service.LoginService;
import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Map;
/**
 * 版权所有：风华正茂（北京）科技有限公司
 * 作　　者：陈金晓
 * 版　　本：1.0
 * 创建日期：2019年3月21日
 * 功能描述：_______________________
 * 
 * 修改历史记录
 * 	作者				时间					版本					备注
 *  陈金晓			2019年3月21日			1.0					创建
 */
public class LoginCookieUtil {
   //保存cookie时的cookieName
   private final static String cookieDomainName = "SYSTEM_LOGIN_COOKIE_CHUANGKE";
   //加密cookie时的网站自定码
   private final static String webKey = "com.fhzm.java2019-03-21";
   //设置cookie有效期2周
   private final static long cookieMaxAge = 60 * 60 * 24 * 7 * 2;
   
   //保存Cookie到客户端-------------------------------------------------------------------------
   //传递进来的user对象中封装了在登录时填写的用户名与密码
   public static void saveCookie(AuthMember user, HttpServletResponse response) {
          //cookie的有效期
          long validTime = System.currentTimeMillis() + (cookieMaxAge * 5000);
          //MD5加密用户详细信息
          String cookieValueWithMd5 =MD5Util.getMD5Str(user.getUsername() + ":" + user.getPassword() + ":" + validTime + ":" + webKey);
          //将要被保存的完整的Cookie值
          String cookieValue = user.getUsername() + ":" + validTime + ":" + cookieValueWithMd5;
          //再一次对Cookie的值进行BASE64编码
          String cookieValueBase64 = new String(Base64.encode(cookieValue.getBytes()));
          //开始保存Cookie
          Cookie cookie = new Cookie(cookieDomainName, cookieValueBase64);
          //存一个月(这个值应该大于或等于validTime)
          cookie.setMaxAge(60 * 60 * 24 * 8 * 2);
          //cookie有效路径是网站根目录
          cookie.setPath("/");
          //向客户端写入
          response.addCookie(cookie);
   }
   
   
   
	//读取Cookie,自动完成登录操作----------------------------------------------------------------
	//在拦截器中调用此代码，见LoginInterceptor
   public static LoginEnum readCookieAndLogon(HttpServletRequest request, HttpServletResponse response) throws Exception{

	   String contextPath = request.getContextPath();    
	   		//根据cookieName取cookieValue
	       Cookie cookies[] = request.getCookies();
                 String cookieValue = null;
                     if(cookies!=null){
                        for(int i=0;i < cookies.length ;i++){
                           if (cookieDomainName.equals(cookies[i].getName())) {
                                  cookieValue = cookies[i].getValue();
                                  break;
                           }
                        }
                     }
                     //如果cookieValue为空,返回,
				 if(cookieValue==null){
				        return LoginEnum.COOKIE_NULL;
				 }
	              //如果cookieValue不为空,才执行下面的代码
	              //先得到的CookieValue进行Base64解码
	              String cookieValueAfterDecode = new String (Base64.decode(cookieValue),"utf-8");
	              //对解码后的值进行分拆,得到一个数组,如果数组长度不为3,就是非法登录
	              String cookieValues[] = cookieValueAfterDecode.split(":");
	              if(cookieValues.length!=3){
	                     return LoginEnum.MALICE;
	              }
	              //判断是否在有效期内,过期就删除Cookie
	              long validTimeInCookie = new Long(cookieValues[1]);
	              if(validTimeInCookie < System.currentTimeMillis()){
	                     //删除Cookie
	                     clearCookie(response);
	                     return LoginEnum.COOKIE_TIME_OUT;
	              }
	              //取出cookie中的用户名,并到数据库中检查这个用户名,
	              String username = cookieValues[0];
	              //根据用户名到数据库中检查用户是否存在
	     		  AuthMemberService authMemberService =  (AuthMemberService) SpringUtils.getBean("authMemberService");
	              AuthMember user = authMemberService.getCkAuthMemberCustomByUserName(username);
	              //如果user返回不为空,就取出密码,使用用户名+密码+有效时间+ webSiteKey进行MD5加密
	              if(user!=null){
	                     String md5ValueInCookie = cookieValues[2];
	                     String md5ValueFromUser =MD5Util.getMD5Str(user.getUsername() + ":" + user.getPassword()
	                                   + ":" + validTimeInCookie + ":" + webKey);
	                     //将结果与Cookie中的MD5码相比较,如果相同,写入Session,自动登录成功,并继续用户请求
	                     if(md5ValueFromUser.equals(md5ValueInCookie)){
	                     	HttpSession session = request.getSession(true);
	                        LoginService  loginService=  (LoginService) SpringUtils.getBean("loginService");
	                        Map<String, Object> sessionMapByMember = loginService.getSessionMapByMember(user);
	                        session.setAttribute("user", user);
	               			session.setAttribute("menulist", sessionMapByMember.get("menulist"));
	               			session.setAttribute("urlList", sessionMapByMember.get("urlList"));
	               			session.setAttribute("menulistJson", JsonUtils.toJSONString(sessionMapByMember.get("menulist")));
	                        return LoginEnum.SUCCESS;
	                     }
	             }
	             return LoginEnum.NEED_LOGIN;
	       }
	 
       //用户注销时,清除Cookie,在需要时可随时调用-----------------------------------------------------
       public static void clearCookie( HttpServletResponse response){
              Cookie cookie = new Cookie(cookieDomainName, null);
              cookie.setMaxAge(0);
              cookie.setPath("/");
              response.addCookie(cookie);
       }
}
