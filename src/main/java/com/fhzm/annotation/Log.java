package com.fhzm.annotation;

import java.lang.annotation.*;

/**
 * 日志注解
 */
@Target({ElementType.PARAMETER, ElementType.METHOD})  
@Retention(RetentionPolicy.RUNTIME)  
@Documented  
public @interface Log {
    /** 要执行的具体操作比如：添加用户 **/
    public String operationName() default "";
    /**
     * 操作名称英文
     * @return
     */
    public String operationNameEn() default "";
    /**
     * 日志操作  0未知 在term配置
     * @return
     */
    public int logType() default 0;
}
