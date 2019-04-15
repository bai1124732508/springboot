package com.fhzm.common;
/**
 * 
 * 版权所有：风华正茂（北京）科技有限公司
 * 作　　者：陈金晓
 * 版　　本：1.0
 * 创建日期：2017年6月20日
 * 功能描述：______fastdf裁剪图片_________________
 * 
 * 修改历史记录
 * 	作者				时间					版本					备注
 *  陈金晓			2017年6月20日		1.0					创建
 */
public class ImageUtils {
	/**
	 * 根据尺寸裁剪图片（仅使用fastdfs上传的图片地址）
	 * @param size 大小数字的字符串
	 * @param url
	 * @return
	 */
	public static String getSizeImge(String size,String url){
		String img=url.substring(0, url.indexOf("."));
		String prefix=url.substring(url.indexOf(".")+1);
		String acturl=img+"_"+size+"x"+size+"."+prefix;
		return acturl;
	}
	
	 public static void main(String[] args) {
	   System.out.println(getSizeImge("30", "/group1/M00/00/01/CgoKB1lArHyAadvtAABe8-kc7gM196.jpg"));
	 }
	

}
