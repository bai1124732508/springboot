package com.fhzm.common;

import java.text.NumberFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NumberUtil {
	
	/**
	 * 计算两个数值的百分比
	 * @param molecule
	 * @param denominator
	 */
	public static String  calculatedPercentage(int molecule,int denominator) {
		// 创建一个数值格式化对象   
		NumberFormat numberFormat = NumberFormat.getInstance();   
		// 设置精确到小数点后2位   
		numberFormat.setMaximumFractionDigits(2);   
		String result = numberFormat.format((float)molecule/(float)denominator*100);
		if(molecule ==  0 ) {
			result ="-";
		}
		return result;  
	}
	
	
	/**
     * 利用正则表达式判断字符串是否是数字
     * @param str
     * @return
     */
    public static boolean isNumeric(String str){
           Pattern pattern = Pattern.compile("[0-9]+.*[0-9]*");
           Matcher isNum = pattern.matcher(str);
           if( !isNum.matches() ){
               return false;
           }
           return true;
    }

}
