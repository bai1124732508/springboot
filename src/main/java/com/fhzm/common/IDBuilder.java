package com.fhzm.common;

import java.util.Date;
import java.util.UUID;

public class IDBuilder {
	/**
	 * 创建uuid
	 * 
	 * @return
	 */
	public static UUID newUUID() {
		return UUID.randomUUID();
	}

	/**
	 * 返回uuidString
	 * 
	 * @return
	 */
	public static String getNewUUIDString() {
		return newUUID().toString();
	}
	/**
	 * 项目中生成表的Id 生成规则当前时间的毫秒数+三位的随机数
	 * @return
	 */
	public static String getTableId(){
		try {
			StringBuffer sb=new StringBuffer();
			sb.append(new Date().getTime());
			int random=(int)(Math.random()*1000.0);
			String randomStr = String.format("%03d", random);  
			sb.append(randomStr);
			Thread.sleep(1);
			return sb.toString();
		} catch (InterruptedException e) {
			e.printStackTrace();
			StringBuffer sb=new StringBuffer();
			sb.append(new Date().getTime());
			int random=(int)(Math.random()*1000.0);
			String randomStr = String.format("%03d", random);  
			sb.append(randomStr);
			return sb.toString();
		}
	}

	
}
