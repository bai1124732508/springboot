package com.fhzm.common;

/**
 * 
 * 版权所有：风华正茂（北京）科技有限公司
 * 作　　者：陈金晓
 * 版　　本：1.0
 * 创建日期：2017年5月16日
 * 功能描述：______返回结果集_________________
 * 
 * 修改历史记录
 * 	作者				时间					版本					备注
 *  陈金晓			2017年5月16日		1.0					创建
 */
public class ReturnResult {
	/**
	 * 返回值编码
	 */
	private String code;
	/**
	 * 返回值内容
	 */
	private String msg;
	/**
	 * 等待时间
	 */
	private Integer wait;
	/**
	 * 重定向地址
	 */
	private String url;
	
	
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public Integer getWait() {
		return wait;
	}
	public void setWait(Integer wait) {
		this.wait = wait;
	}
	/**
	 * 成功
	 * @param msg 消息
	 * @return
	 */
	public static ReturnResult ok(String msg,String url) {
		return new ReturnResult(msg,"1",url);
	}
	
	
	public static ReturnResult ok(String code,String msg,Object data) {
		return new ReturnResult(code, msg,data);
	}
	
	private Object data;
	
	public ReturnResult(String code,String msg,Object data) {
		this.code = code;
		this.msg = msg;
		this.wait=3;
		this.data=data;
	}
	
	
	public Object getData() {
		return data;
	}
	public void setData(Object data) {
		this.data = data;
	}
	/**
	 * 失败列表
	 * @param msg 消息
	 * @return
	 */
	public static ReturnResult error(String msg) {
		return new ReturnResult(msg,"0");
	}



	public static ReturnResult error(String code,String msg) {
		return new ReturnResult(msg,code);
	}

	/**
	 * 成功列表
	 * @param msg 消息
	 * @return
	 */
	public static ReturnResult list(String msg) {
		return new ReturnResult(msg,"1");
	}
	public ReturnResult(String msg,String code,String url) {
		this.code = code;
		this.msg = msg;
		this.wait=3;
		this.url=url;
	}
	
	public ReturnResult(String msg,String code){
		this.code = code;
		this.msg = msg;
		this.wait=3;
	}
	public ReturnResult(String code, String msg, Integer wait, String url) {
		super();
		this.code = code;
		this.msg = msg;
		this.wait = wait;
		this.url = url;
	}
	public ReturnResult( String msg, Integer wait, String url) {
		super();
		this.code = "1";
		this.msg = msg;
		this.wait = wait;
		this.url = url;
	}
	
	/**
	 * 成功 
	 * @param msg消息
	 * @return
	 */
	public static ReturnResult ok(String msg){
		return new ReturnResult(msg,"1","");
	} 
	/**
	 * 
	 * @param msg
	 * @param wait
	 * @param url
	 * @return
	 */
	public static ReturnResult ok(String msg, Integer wait, String url){
		return new ReturnResult(msg,wait,url);
	}
	
	
	
	
	
}
