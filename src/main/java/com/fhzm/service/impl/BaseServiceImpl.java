package com.fhzm.service.impl;
import com.fhzm.common.SpringUtils;
import com.fhzm.service.BaseService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Method;

@Service("baseService")
@Transactional
public class BaseServiceImpl implements BaseService {

    @Override
    public Object getObjById(String id,Class clazz) throws Exception {
        String className = clazz.getName();
        String mapperName = className+"Mapper";
        mapperName = mapperName.replace("entity", "dao");
        Class<?> mapperClass = Class.forName(mapperName);
        Object bean = SpringUtils.getBean(mapperClass);
        Class<?> classN = bean.getClass();
        Method method = classN.getMethod("selectByPrimaryKey", String.class);
        Object invoke = method.invoke(bean, id);
        return invoke;
    }

    @Override
    public void insertObj(Object obj) throws Exception {
        Class clazz = obj.getClass();
        String className = clazz.getName();
        String mapperName = className+"Mapper";
        mapperName = mapperName.replace("entity", "dao");
        Class<?> mapperClass = Class.forName(mapperName);
        Object bean = SpringUtils.getBean(mapperClass);
        Class<?> classN = bean.getClass();
        Method method = classN.getMethod("insertSelective", clazz);
        method.invoke(bean, obj);
    }

    @Override
    public void delObjById(String id, Class clazz) throws Exception {
        String className = clazz.getName();
        String mapperName = className+"Mapper";
        mapperName = mapperName.replace("entity", "dao");
        Class<?> mapperClass = Class.forName(mapperName);
        Object bean = SpringUtils.getBean(mapperClass);
        Class<?> classN = bean.getClass();
        Method method = classN.getMethod("deleteByPrimaryKey",  String.class);
        method.invoke(bean, id);
    }

    @Override
    public void updateObj(Object obj) throws Exception {
        Class clazz = obj.getClass();
        String className = clazz.getName();
        String mapperName = className+"Mapper";
        mapperName = mapperName.replace("entity", "dao");
        Class<?> mapperClass = Class.forName(mapperName);
        Object bean = SpringUtils.getBean(mapperClass);
        Class classN = bean.getClass();
        Method method = classN.getMethod("updateByPrimaryKeySelective",  clazz);
        method.invoke(bean, obj);
    }



}
