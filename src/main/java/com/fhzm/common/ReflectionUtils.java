package com.fhzm.common;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class ReflectionUtils {

	private static String getNewFiledName(String[] dests, String fieldName) {
		for (String field : dests) {
			String[] fields = field.split(":");
			if (fieldName.equals(fields[0])) {
				fieldName = fields[1];
				break;
			}
		}
		return fieldName;
	}

	/**
	 * 筛选dests里存在的属性名称;dests里面不存在的不放如map
	 * 
	 * @param dests
	 * @param obj
	 * @return
	 * @throws SecurityException
	 * @throws NoSuchMethodException
	 */
	public static Map<String, Object> getDestJson(String[] dests, Object obj)
			throws NoSuchMethodException, SecurityException {
		Class<? extends Object> clazz = obj.getClass();
		Class<? extends Object> superclass = clazz.getSuperclass();
		Field[] fields1 = null;
		Field[] fields2 = null;

		if (superclass.getName().toLowerCase().equals("object")) {
			fields1 = clazz.getDeclaredFields();
		} else {
			fields1 = superclass.getDeclaredFields();
			fields2 = clazz.getDeclaredFields();
		}
		Map<String, Object> map = new HashMap<>();
		for (int i = 0; i < fields1.length; i++) {
			Field field = fields1[i];
			String fieldName = field.getName();
			if (judge(dests, fieldName)) {
				map.put(getNewFiledName(dests, fieldName), getFieldValue(obj, fieldName));
			}
		}
		for (int i = 0; i < fields2.length; i++) {
			Field field = fields2[i];
			String fieldName = field.getName();
			if (judge(dests, fieldName)) {
				map.put(getNewFiledName(dests, fieldName), getFieldValue(obj, fieldName));
			}
		}
		return map;
	}

	/**
	 * 通过反射获得obj里的属性,判断该属性是否和dests里的相等;
	 * 
	 * @param dests
	 * @param field
	 * @return
	 */
	private static boolean judge(String[] dests, String field) {
		boolean isFind = false;
		if (dests != null && dests.length > 0) {
			for (int i = 0; i < dests.length; i++) {
				String[] splits = dests[i].split(":");
				if (splits[0].equals(field)) {
					isFind = true;
					break;
				}
			}
		}
		return isFind;
	}

	/**
	 * 通过反射，用属性名称获得属性值
	 */
	public static Object getFieldValue(Object thisClass, String fieldName) {
		Object value = new Object();
		Method method = null;
		try {
			String methodName = fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
			method = thisClass.getClass().getMethod("get" + methodName);
			Type type = method.getGenericReturnType();
			String returnType = type.getClass().getSimpleName();
			value = method.invoke(thisClass);
			if ("Long".equalsIgnoreCase(returnType) || "BigDecimal".equalsIgnoreCase(returnType)
					|| "Integer".equalsIgnoreCase(returnType) || "Double".equalsIgnoreCase(returnType)) {
				if(value == null || "".equals(value)) {
					value = 0;
				}
			} else {
				if(value == null || "".equals(value)) {
					value = "";
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return value;
	}

	public static void main(String[] args) {
		Integer i = new Integer(0);
		System.out.println(i.getClass().getSimpleName());

	}
}
