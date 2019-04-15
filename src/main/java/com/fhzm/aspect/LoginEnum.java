package com.fhzm.aspect;

import java.io.Serializable;

/**
 * 版权所有：风华正茂（北京）科技有限公司
 * 作　　者：陈金晓
 * 版　　本：1.0
 * 创建日期：2017年5月23日
 * 功能描述：__________登录状态枚举_____________
 * 
 * 修改历史记录
 * 	作者				时间					版本					备注
 *  陈金晓			2017年5月23日			1.0					创建
 */
public enum LoginEnum {
	//通过
	SUCCESS,
	//cookie不存在登录信息
	COOKIE_NULL,
	//cookie过期
	COOKIE_TIME_OUT,
	//恶意攻击
	MALICE,
	NEED_LOGIN
}
