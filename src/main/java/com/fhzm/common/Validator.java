package com.fhzm.common;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import com.fhzm.common.file.TermManager;
import org.springframework.expression.ParseException;


public class Validator {
    public static void main(String[] args) throws Exception {  
//       String content = "[ { 'code': 1, 'page': 1, 'type': 0, 'content': '今天早上都吃了什么？',  'image': '1111,333,5555', 'child': [ { 'content': '馒头' }, { 'content': '包子' }, { 'content':'油条' }, { 'content': '稀粥' } ] } ]";

        String[] array = {"1","2","3","4"};  
        int index = Arrays.binarySearch(array,"4"); 
        System.out.println(TermManager.getIdByName("basic_info_child","大专"));
        
        System.out.println(isEmail("ZHUANGLI.JNXYJ/HAIERNET"));
    }
    /**
     * 验证金额
     * @param str
     * @return
     */
    public static boolean isNumber(String str){   
        Pattern pattern=Pattern.compile("^(([1-9]{1}\\d*)|([0]{1}))(\\.(\\d){0,2})?$"); // 判断小数点后2位的数字的正则表达式  
        Matcher match=pattern.matcher(str);   
        if(match.matches()==false){   
           return false;   
        }else{   
           return true;   
        }   
    } 
	/**
	 * 验证执业证书
	 * @param number
	 * @return
	 */
	public static boolean isPracticeNumber(String number){
		String str_pattern = "^[0-9]{27}$";
		String str_patternone = "^[0-9]{24}$";
		Pattern pattern = Pattern.compile(str_pattern);
		   Matcher matcher = pattern.matcher(number);
		   Pattern patternOne = Pattern.compile(str_patternone);
		   Matcher matcherOne = patternOne.matcher(number);
		  	return matcher.matches()||matcherOne.matches();
	}
	
	/**
	 * 验证邮箱
	 * @param email
	 * @return
	 */
	public static boolean isEmail(String email)
	  {
	  boolean tag = true;
	  String str_pattern = "^([\\w-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([\\w-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
	   Pattern pattern = Pattern.compile(str_pattern);
	   Matcher matcher = pattern.matcher(email);
	  if (!matcher.find()) {
		  tag = false;
	  }
	  	return tag;
	  }
	
	/**
	 * 校验手机号码
	 * @param str
	 * @return
	 * @throws PatternSyntaxException
	 */
	 public static boolean isPhone(String str) throws PatternSyntaxException {  
	        String regExp = "^((13[0-9])|(15[^4])|(18[0,2,3,5-9])|(17[0-8])|(147))\\d{8}$";  
	        Pattern p = Pattern.compile(regExp);  
	        Matcher m = p.matcher(str);  
	        return m.matches();  
	    }  
	
	/** 
     * <pre> 
     * 省、直辖市代码表： 
     *     11 : 北京  12 : 天津  13 : 河北       14 : 山西  15 : 内蒙古   
     *     21 : 辽宁  22 : 吉林  23 : 黑龙江  31 : 上海  32 : 江苏   
     *     33 : 浙江  34 : 安徽  35 : 福建       36 : 江西  37 : 山东   
     *     41 : 河南  42 : 湖北  43 : 湖南       44 : 广东  45 : 广西      46 : 海南   
     *     50 : 重庆  51 : 四川  52 : 贵州       53 : 云南  54 : 西藏   
     *     61 : 陕西  62 : 甘肃  63 : 青海       64 : 宁夏  65 : 新疆   
     *     71 : 台湾   
     *     81 : 香港  82 : 澳门   
     *     91 : 国外 
     * </pre> 
     */  
    private static String cityCode[] = { "11", "12", "13", "14", "15", "21",  
            "22", "23", "31", "32", "33", "34", "35", "36", "37", "41", "42",  
            "43", "44", "45", "46", "50", "51", "52", "53", "54", "61", "62",  
            "63", "64", "65", "71", "81", "82", "91" };  
  
    /** 
     * 每位加权因子 
     */  
    private static int power[] = { 7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5,  
            8, 4, 2 };  
  
    /** 
     * 验证所有的身份证的合法性 
     *  
     * @param idcard 
     *            身份证 
     * @return 合法返回true，否则返回false 
     * @throws java.text.ParseException 
     */  
    public static boolean isValidatedAllIdcard(String idcard) throws java.text.ParseException {  
        if (idcard == null || "".equals(idcard)) {  
            return false;  
        }  
        if (idcard.length() == 15) {  
            return validate15IDCard(idcard);  
        }  
        return validate18Idcard(idcard);  
    }  
  
    /** 
     * <p> 
     * 判断18位身份证的合法性 
     * </p> 
     * 根据〖中华人民共和国国家标准GB11643-1999〗中有关公民身份号码的规定，公民身份号码是特征组合码，由十七位数字本体码和一位数字校验码组成。 
     * 排列顺序从左至右依次为：六位数字地址码，八位数字出生日期码，三位数字顺序码和一位数字校验码。 
     * <p> 
     * 顺序码: 表示在同一地址码所标识的区域范围内，对同年、同月、同 日出生的人编定的顺序号，顺序码的奇数分配给男性，偶数分配 给女性。 
     * </p> 
     * <p> 
     * 1.前1、2位数字表示：所在省份的代码； 2.第3、4位数字表示：所在城市的代码； 3.第5、6位数字表示：所在区县的代码； 
     * 4.第7~14位数字表示：出生年、月、日； 5.第15、16位数字表示：所在地的派出所的代码； 
     * 6.第17位数字表示性别：奇数表示男性，偶数表示女性； 
     * 7.第18位数字是校检码：也有的说是个人信息码，一般是随计算机的随机产生，用来检验身份证的正确性。校检码可以是0~9的数字，有时也用x表示。 
     * </p> 
     * <p> 
     * 第十八位数字(校验码)的计算方法为： 1.将前面的身份证号码17位数分别乘以不同的系数。从第一位到第十七位的系数分别为：7 9 10 5 8 4 
     * 2 1 6 3 7 9 10 5 8 4 2 
     * </p> 
     * <p> 
     * 2.将这17位数字和系数相乘的结果相加。 
     * </p> 
     * <p> 
     * 3.用加出来和除以11，看余数是多少 
     * </p> 
     * 4.余数只可能有0 1 2 3 4 5 6 7 8 9 10这11个数字。其分别对应的最后一位身份证的号码为1 0 X 9 8 7 6 5 4 3 
     * 2。 
     * <p> 
     * 5.通过上面得知如果余数是2，就会在身份证的第18位数字上出现罗马数字的Ⅹ。如果余数是10，身份证的最后一位号码就是2。 
     * </p> 
     *  
     * @param idcard 
     * @return 
     * @throws java.text.ParseException 
     */  
    public static boolean validate18Idcard(String idcard) throws java.text.ParseException {  
        if (idcard == null) {  
            return false;  
        }  
  
        // 非18位为假  
        if (idcard.length() != 18) {  
            return false;  
        }  
        // 获取前17位  
        String idcard17 = idcard.substring(0, 17);  
  
        // 前17位全部为数字  
        if (!isDigital(idcard17)) {  
            return false;  
        }  
  
        String provinceid = idcard.substring(0, 2);  
        // 校验省份  
        if (!checkProvinceid(provinceid)) {  
            return false;  
        }  
  
        // 校验出生日期  
        String birthday = idcard.substring(6, 14);  
  
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");  
  
        try {  
            Date birthDate = sdf.parse(birthday);  
            String tmpDate = sdf.format(birthDate);  
            if (!tmpDate.equals(birthday)) {// 出生年月日不正确  
                return false;  
            }  
  
        } catch (ParseException e1) {  
  
            return false;  
        }  
  
        // 获取第18位  
        String idcard18Code = idcard.substring(17, 18);  
  
        char c[] = idcard17.toCharArray();  
  
        int bit[] = converCharToInt(c);  
  
        int sum17 = 0;  
  
        sum17 = getPowerSum(bit);  
  
        // 将和值与11取模得到余数进行校验码判断  
        String checkCode = getCheckCodeBySum(sum17);  
        if (null == checkCode) {  
            return false;  
        }  
        // 将身份证的第18位与算出来的校码进行匹配，不相等就为假  
        if (!idcard18Code.equalsIgnoreCase(checkCode)) {  
            return false;  
        }  
  
        return true;  
    }  
  
    /** 
     * 校验15位身份证 
     *  
     * <pre> 
     * 只校验省份和出生年月日 
     * </pre> 
     *  
     * @param idcard 
     * @return 
     * @throws java.text.ParseException 
     */  
    public static boolean validate15IDCard(String idcard) throws java.text.ParseException {  
        if (idcard == null) {  
            return false;  
        }  
        // 非15位为假  
        if (idcard.length() != 15) {  
            return false;  
        }  
  
        // 15全部为数字  
        if (!isDigital(idcard)) {  
            return false;  
        }  
  
        String provinceid = idcard.substring(0, 2);  
        // 校验省份  
        if (!checkProvinceid(provinceid)) {  
            return false;  
        }  
  
        String birthday = idcard.substring(6, 12);  
  
        SimpleDateFormat sdf = new SimpleDateFormat("yyMMdd");  
  
        try {  
            Date birthDate = sdf.parse(birthday);  
            String tmpDate = sdf.format(birthDate);  
            if (!tmpDate.equals(birthday)) {// 身份证日期错误  
                return false;  
            }  
  
        } catch (ParseException e1) {  
  
            return false;  
        }  
  
        return true;  
    }  
  
    /** 
     * 将15位的身份证转成18位身份证 
     *  
     * @param idcard 
     * @return 
     * @throws java.text.ParseException 
     */  
    public static String convertIdcarBy15bit(String idcard) throws java.text.ParseException {  
        if (idcard == null) {  
            return null;  
        }  
  
        // 非15位身份证  
        if (idcard.length() != 15) {  
            return null;  
        }  
  
        // 15全部为数字  
        if (!isDigital(idcard)) {  
            return null;  
        }  
  
        String provinceid = idcard.substring(0, 2);  
        // 校验省份  
        if (!checkProvinceid(provinceid)) {  
            return null;  
        }  
  
        String birthday = idcard.substring(6, 12);  
  
        SimpleDateFormat sdf = new SimpleDateFormat("yyMMdd");  
  
        Date birthdate = null;  
        try {  
            birthdate = sdf.parse(birthday);  
            String tmpDate = sdf.format(birthdate);  
            if (!tmpDate.equals(birthday)) {// 身份证日期错误  
                return null;  
            }  
  
        } catch (ParseException e1) {  
            return null;  
        }  
  
        Calendar cday = Calendar.getInstance();  
        cday.setTime(birthdate);  
        String year = String.valueOf(cday.get(Calendar.YEAR));  
  
        String idcard17 = idcard.substring(0, 6) + year + idcard.substring(8);  
  
        char c[] = idcard17.toCharArray();  
        String checkCode = "";  
  
        // 将字符数组转为整型数组  
        int bit[] = converCharToInt(c);  
  
        int sum17 = 0;  
        sum17 = getPowerSum(bit);  
  
        // 获取和值与11取模得到余数进行校验码  
        checkCode = getCheckCodeBySum(sum17);  
  
        // 获取不到校验位  
        if (null == checkCode) {  
            return null;  
        }  
        // 将前17位与第18位校验码拼接  
        idcard17 += checkCode;  
        return idcard17;  
    }  
  
    /** 
     * 校验省份 
     *  
     * @param provinceid 
     * @return 合法返回TRUE，否则返回FALSE 
     */  
    private static boolean checkProvinceid(String provinceid) {  
        for (String id : cityCode) {  
            if (id.equals(provinceid)) {  
                return true;  
            }  
        }  
        return false;  
    }  
  
    /** 
     * 数字验证 
     *  
     * @param str 
     * @return 
     */  
    private static boolean isDigital(String str) {  
        return str.matches("^[0-9]*$");  
    }  
  
    /** 
     * 将身份证的每位和对应位的加权因子相乘之后，再得到和值 
     *  
     * @param bit 
     * @return 
     */  
    private static int getPowerSum(int[] bit) {  
  
        int sum = 0;  
  
        if (power.length != bit.length) {  
            return sum;  
        }  
  
        for (int i = 0; i < bit.length; i++) {  
            for (int j = 0; j < power.length; j++) {  
                if (i == j) {  
                    sum = sum + bit[i] * power[j];  
                }  
            }  
        }  
        return sum;  
    }  
  
    /** 
     * 将和值与11取模得到余数进行校验码判断 
     *  
     * @param checkCode 
     * @param sum17 
     * @return 校验位 
     */  
    private static String getCheckCodeBySum(int sum17) {  
        String checkCode = null;  
        switch (sum17 % 11) {  
        case 10:  
            checkCode = "2";  
            break;  
        case 9:  
            checkCode = "3";  
            break;  
        case 8:  
            checkCode = "4";  
            break;  
        case 7:  
            checkCode = "5";  
            break;  
        case 6:  
            checkCode = "6";  
            break;  
        case 5:  
            checkCode = "7";  
            break;  
        case 4:  
            checkCode = "8";  
            break;  
        case 3:  
            checkCode = "9";  
            break;  
        case 2:  
            checkCode = "x";  
            break;  
        case 1:  
            checkCode = "0";  
            break;  
        case 0:  
            checkCode = "1";  
            break;  
        }  
        return checkCode;  
    }  
  
    /** 
     * 将字符数组转为整型数组 
     *  
     * @param c 
     * @return 
     * @throws NumberFormatException 
     */  
    private static int[] converCharToInt(char[] c) throws NumberFormatException {  
        int[] a = new int[c.length];  
        int k = 0;  
        for (char temp : c) {  
            a[k++] = Integer.parseInt(String.valueOf(temp));  
        }  
        return a;  
    }  
    
    /**
     * 根据身份证号计算出生日期
     * @param idcard
     * @return
     */
    public static String getBirthday (String idcard){
    	  String birthday = idcard.substring(6, 14);  
          SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");  
          SimpleDateFormat sdfs = new SimpleDateFormat("yyyy-MM-dd");  
          Date birthdate = null;  
          String birth = "";
          try {  
              birthdate = sdf.parse(birthday);  
              birth  = sdfs.format(birthdate);
             return birth;
          } catch (Exception e1) {  
              return "";  
          }  
    }
 

//出生日期字符串转化成Date对象  
public static Date parse(String strDate) throws ParseException, java.text.ParseException {  
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");  
   
    return sdf.parse(strDate);  
}  
//出生日期字符串转化成Date对象  
public static String parseTimestr() throws ParseException, java.text.ParseException {  
	  Date date = new Date();  
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");  
	return sdf.format(date);  
}  
public static Boolean compare_date(String DATE1, String DATE2) {
    DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
    try {
        Date dt1 = df.parse(DATE1);
        Date dt2 = df.parse(DATE2);
        if (dt1.getTime() > dt2.getTime()) {
            return true;
        } 
    } catch (Exception exception) {
        exception.printStackTrace();
    }
    return false;
}

//由出生日期获得年龄  
public static String getAge(String strDate) throws Exception {  
	 SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");  
	 Date birthDay = sdf.parse(strDate);
	String agestr = "";
    Calendar cal = Calendar.getInstance();  

    if (cal.before(birthDay)) {  
        throw new IllegalArgumentException(  
                "The birthDay is before Now.It's unbelievable!");  
    }  
    int yearNow = cal.get(Calendar.YEAR);  
    int monthNow = cal.get(Calendar.MONTH);  
    int dayOfMonthNow = cal.get(Calendar.DAY_OF_MONTH);  
    cal.setTime(birthDay);  

    int yearBirth = cal.get(Calendar.YEAR);  
    int monthBirth = cal.get(Calendar.MONTH);  
    int dayOfMonthBirth = cal.get(Calendar.DAY_OF_MONTH);  

    int age = yearNow - yearBirth;  

    if (monthNow <= monthBirth) {  
        if (monthNow == monthBirth) {  
            if (dayOfMonthNow < dayOfMonthBirth) age--;  
        }else{  
            age--;  
        }  
    }  
    agestr = String.valueOf(age);
    if(age>=0 && age<3){
    	long day=getDays(birthDay);
        long year=day/365;
        long month=(day-365L*year)/30+1;
        long week=day/7+1;
        if(age==0){
        	agestr = month+"月"+week+"周";
        }else{
        	agestr = year+"岁"+month+"月";
        }
    }
 
    return agestr;  
}  
public static long getDays(Date date) throws Exception {
	Date currentTime = new Date();
	 SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");  
    String mydate = formatter.format(currentTime);
    currentTime = formatter.parse(mydate);
    // 转换为标准时间
    SimpleDateFormat myFormatter = new SimpleDateFormat("yyyy-MM-dd");
    long day = (date.getTime() - currentTime.getTime()) / (24 * 60 * 60 * 1000);
    return Math.abs(day);
}
    
    public static String bytes2HexString(byte[] b) {
    	  String ret = "";
    	  for (int i = 0; i < b.length; i++) {
    	   String hex = Integer.toHexString(b[ i ] & 0xFF);
    	   if (hex.length() == 1) {
    	    hex = '0' + hex;
    	   }
    	   ret += hex.toUpperCase();
    	  }
    	  return ret;
    	}
    public static String encode(String str) 
    { 
    String hexString="0123456789ABCDEF"; 
    //根据默认编码获取字节数组 
    byte[] bytes=str.getBytes(); 
    StringBuilder sb=new StringBuilder(bytes.length*2); 
    //将字节数组中每个字节拆解成2位16进制整数 
    for(int i=0;i<bytes.length;i++) 
    { 
    sb.append(hexString.charAt((bytes[i]&0xf0)>>4)); 
    sb.append(hexString.charAt((bytes[i]&0x0f)>>0)); 
    } 
    return sb.toString(); 
    } 
    public static String toHexString(String s) 
    { 
    String str=""; 
    for (int i=0;i<s.length();i++) 
    { 
    int ch = (int)s.charAt(i); 
    String s4 = Integer.toHexString(ch); 
    str = str + s4; 
    } 
    return str; 
    } 
    public static String toStringHex(String s) 
    { 
    byte[] baKeyword = new byte[s.length()/2]; 
    for(int i = 0; i < baKeyword.length; i++) 
    { 
    try 
    { 
    baKeyword[i] = (byte)(0xff & Integer.parseInt(s.substring(i*2, i*2+2),16)); 
    } 
    catch(Exception e) 
    { 
    e.printStackTrace(); 
    } 
    } 
    try 
    { 
    s = new String(baKeyword, "utf-8");//UTF-16le:Not 
    } 
    catch (Exception e1) 
    { 
    e1.printStackTrace(); 
    } 
    return s; 
    } 
	public static String verdictTime(){
		String dayTime = "0";//1代表上午，2代表下午，3代表晚上
		int i =	Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
		if(i>=8 && i<12){//上午
			dayTime = "1";
		}else if(i>=12 && i<18){// 下午
			dayTime = "2";	
		}else if(i>=18 && i<22){// 晚上
			dayTime = "3";	
		}
		return dayTime;
	}
    /**
     * 根据身份证号获取年龄
     * @param identity
     * @return
     * @throws Exception 
     */
	public static String getAgeByIdentity(String identity) throws Exception {
		String birthday = getBirthday(identity);
		String age = getAge(birthday);
		return age;
	} 
}
