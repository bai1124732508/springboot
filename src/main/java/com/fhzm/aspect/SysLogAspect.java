package com.fhzm.aspect;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fhzm.annotation.Log;
import com.fhzm.common.HttpContextUtils;
import com.fhzm.common.IDBuilder;
import com.fhzm.common.JsonUtils;
import com.fhzm.entity.generator.AuthMember;
import com.fhzm.entity.generator.SysLog;
import com.fhzm.service.BaseService;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.fhzm.common.IpgetUtil.*;

@Aspect
@Component
public class SysLogAspect {
    private static final Logger logger = LoggerFactory.getLogger(SysLogAspect. class);
    @Autowired
    private BaseService baseService;

	@Pointcut("@annotation(com.fhzm.annotation.Log)")
	public void logPointCut() { 
		
	}
    /**
     * 
     * @param joinPoint 切点 
     */  
    @After("logPointCut()")  
    public  void after(JoinPoint joinPoint) {
    	HttpServletRequest request = HttpContextUtils.getHttpServletRequest();
        HttpSession session = request.getSession();
        String targetName = joinPoint.getTarget().getClass().getName();
        String methodName = joinPoint.getSignature().getName();
        Object[] arguments = joinPoint.getArgs();
        try {
            Class targetClass = Class.forName(targetName);
            Method[] methods = targetClass.getMethods();
            String operationName = "";
            String operationNameEn = "";
            int logType = 0;
            GetLogMsg getLogMsg = new GetLogMsg(methodName, arguments, methods, operationName, operationNameEn, logType).invoke();
            operationName = getLogMsg.getOperationName();
            operationNameEn = getLogMsg.getOperationNameEn();
            logType = getLogMsg.getLogType();
            logger.debug("==operationName=="+operationName);
            logger.debug("==operationNameEn=="+operationNameEn);
            logger.debug("==logType=="+logType);
            AuthMember user = (AuthMember) session.getAttribute("user");
            if(user==null){
                user = new AuthMember();
            }
            String ip = getIp(request);
            String address = "";
            //String address = getAddressByIp(ip);
            logger.debug("==ip=="+ip);
            logger.debug("==address=="+address);
            String params = "";
            params = getParam(joinPoint, request, logType, params);
            logger.debug("==params=="+params);
            SysLog log = new SysLog();
            if(logType == 2 ){ //更改角色权限
                String[] id = request.getParameterValues("id");
                log.setObjId(id[0]);
            }
            log.setId(IDBuilder.getTableId());
            log.setDescription(operationName);
            log.setDescriptionEn(operationNameEn);
            log.setMethod((joinPoint.getTarget().getClass().getName() + "." + joinPoint.getSignature().getName() + "()"));
            log.setLogType(logType);
            log.setIp(ip);
            log.setIpAddress(address);
            log.setExceptionCode("");
            log.setExceptionDetail("");
            log.setParams(params);
            log.setCreateId(user.getUid());
            log.setCreateName(user.getNickname());
            log.setCtime(new Date());
            String requestHeader = request.getHeader("user-agent");
            int equipmentType = isMobileDevice(requestHeader);
            log.setEquipmentType(equipmentType);
            //保存数据库
            baseService.insertObj(log);
        }  catch (Exception e) {
        	e.printStackTrace();
            logger.error("==后置通知异常==");
            logger.error("异常信息:{}", e.getMessage());
        }
    }

    private String getParam(JoinPoint joinPoint, HttpServletRequest request, int logType, String params) {
        if(logType != 2 ){
            Object[] args = joinPoint.getArgs();
            if (joinPoint.getArgs() !=  null && joinPoint.getArgs().length > 0) {
                for ( int i = 0; i < args.length; i++) {
                    params += joinPoint.getArgs()[i]+",";
                }
            }
        }else{
            String[] id = request.getParameterValues("id");
            String[] rules = request.getParameterValues("rules");
            Map<String,Object> map = new HashMap<>();
            map.put("id",id);
            map.put("rules",rules);
            params = JsonUtils.toJSONString(map);
        }
        return params;
    }

    /** 
     * 异常通知 用于拦截记录异常日志 
     * 
     * @param joinPoint 
     * @param e 
     */  
     @AfterThrowing(pointcut = "logPointCut()", throwing="e")  
     public  void doAfterThrowing(JoinPoint joinPoint, Throwable e) {
         HttpServletRequest request = HttpContextUtils.getHttpServletRequest();
         HttpSession session = request.getSession();
         String targetName = joinPoint.getTarget().getClass().getName();
         String methodName = joinPoint.getSignature().getName();
         Object[] arguments = joinPoint.getArgs();
         try {
             Class targetClass = Class.forName(targetName);
             Method[] methods = targetClass.getMethods();
             String operationName = "";
             String operationNameEn = "";
             int logType = 0;
             GetLogMsg getLogMsg = new GetLogMsg(methodName, arguments, methods, operationName, operationNameEn, logType).invoke();
             operationName = getLogMsg.getOperationName();
             operationNameEn = getLogMsg.getOperationNameEn();
             logType = getLogMsg.getLogType();
             logger.error("==operationName=="+operationName);
             logger.error("==operationNameEn=="+operationNameEn);
             logger.error("==logType=="+logType);
             AuthMember user = (AuthMember) session.getAttribute("user");
             if(user==null){
                 user = new AuthMember();
             }
             String ip = getIp(request);
             String address = "";
            // String address = getAddressByIp(ip);
             logger.error("==ip=="+ip);
             logger.error("==address=="+address);
             String params = "";
             params = getParam(joinPoint, request, logType, params);
             logger.error("==params=="+params);
             SysLog log = new SysLog();
             log.setId(IDBuilder.getTableId());
             if(logType == 2 ){ //更改角色权限
                 String[] id = request.getParameterValues("id");
                 log.setObjId(id[0]);
             }
             log.setDescription(operationName);
             log.setDescriptionEn(operationNameEn);
             log.setMethod((joinPoint.getTarget().getClass().getName() + "." + joinPoint.getSignature().getName() + "()"));
             log.setLogType(logType);
             log.setIp(ip);
             log.setIpAddress(address);
             log.setExceptionCode(e.getClass().getName());
             log.setExceptionDetail(e.getMessage());
             log.setParams(params);
             log.setCreateId(user.getUid());
             log.setCreateName(user.getNickname());
             log.setCtime(new Date());
             String requestHeader = request.getHeader("user-agent");
             int equipmentType = isMobileDevice(requestHeader);
             log.setEquipmentType(equipmentType);
             //保存数据库
             baseService.insertObj(log);
             /*==========记录本地异常日志==========*/
             logger.error("异常方法:{}异常代码:{}异常信息:{}参数:{}", joinPoint.getTarget().getClass().getName() + joinPoint.getSignature().getName(), e.getClass().getName(), e.getMessage(), params);
        }  catch (Exception ex) {
            //记录本地异常日志
            logger.error("==异常通知异常==");
            logger.error("异常信息:{}", ex.getMessage());
        }

    }


    /**
     * 判断当前是 手机访问还是pc 访问
     * @param requestHeader
     * @return
     */
    public static int  isMobileDevice(String requestHeader){
        /**
         * android : 所有android设备
         * mac os : iphone ipad
         * windows phone:Nokia等windows系统的手机
         */
        String[] deviceArray = new String[]{"android","mac os","windows phone"};
        if(requestHeader == null){
            logger.debug("使用web浏览器");
            return 0;
        }
        requestHeader = requestHeader.toLowerCase();
        for(int i=0;i<deviceArray.length;i++){
            if(requestHeader.indexOf(deviceArray[i])>0){ //使用手机浏览器
                logger.debug("使用手机浏览器");
                return 1;
            }
        }
        logger.debug("使用web浏览器");
        return 0;
    }


    private class GetLogMsg {
        private String methodName;
        private Object[] arguments;
        private Method[] methods;
        private String operationName;
        private String operationNameEn;
        private int logType;

        public GetLogMsg(String methodName, Object[] arguments, Method[] methods, String operationName, String operationNameEn, int logType) {
            this.methodName = methodName;
            this.arguments = arguments;
            this.methods = methods;
            this.operationName = operationName;
            this.operationNameEn = operationNameEn;
            this.logType = logType;
        }

        public String getOperationName() {
            return operationName;
        }

        public String getOperationNameEn() {
            return operationNameEn;
        }

        public int getLogType() {
            return logType;
        }

        public GetLogMsg invoke() {
            for (Method method : methods) {
                if (method.getName().equals(methodName)) {
                    Class[] clazzs = method.getParameterTypes();
                    if (clazzs.length == arguments.length) {
                        operationName = method.getAnnotation(Log.class).operationName();
                        operationNameEn = method.getAnnotation(Log.class).operationNameEn();
                        logType = method.getAnnotation(Log.class).logType();
                        break;
                    }
                }
            }
            return this;
        }
    }
}
