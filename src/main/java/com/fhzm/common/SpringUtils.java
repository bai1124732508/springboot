
package com.fhzm.common;

import org.springframework.beans.BeansException;

import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
/**
 * 
 * 版权所有：风华正茂（北京）科技有限公司
 * 作　　者：陈金晓
 * 版　　本：1.0
 * 创建日期：2017年5月23日
 * 功能描述：_________Spring工具类______________
 * 
 * 修改历史记录
 * 	作者				时间					版本					备注
 *  陈金晓			2017年5月23日		1.0					创建
 */
@Component
public class SpringUtils implements ApplicationContextAware  {

    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        if(SpringUtils.applicationContext == null) {
        	SpringUtils.applicationContext = applicationContext;
        }
    }
    /**
     * 获取applicationContext
     * @return
     */
    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }
    /**
     * 通过name获取 Bean.
     * @param name
     * @return
     */
    public static Object getBean(String name){
        return getApplicationContext().getBean(name);
    }
    /**
     * 通过class获取Bean
     * @param clazz
     * @return
     */
    public static <T> T getBean(Class<T> clazz){
        return getApplicationContext().getBean(clazz);
    }
    /**
     * 通过name,以及Clazz返回指定的Bean
     * @param name
     * @param clazz
     * @return
     */
    public static <T> T getBean(String name,Class<T> clazz){
        return getApplicationContext().getBean(name, clazz);
    }



}
