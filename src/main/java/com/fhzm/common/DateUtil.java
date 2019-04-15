package com.fhzm.common;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil
{
	/**
	 * 为一个日期增加对应的时间，减的话amount传入负数即可
	 * 
	 * @param date
	 * @param field
	 * @param amount
	 * @return
	 */
	public static Date add(Date date, int field, int amount)
	{
		Calendar c = getCalendarForDate(date);
		c.add(field, amount);
		return c.getTime();
	}
	
	/**
	 * 将Date转换为Calendar类型
	 * @param date
	 * @return
	 */
	public static Calendar getCalendarForDate(Date date)
	{
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		return c;
	}

	/**
	 * 为一个日期增加对应的年份
	 * 
	 * @param date
	 * @param year
	 * @return
	 */
	public static Date addYear(Date date, int year)
	{
		return add(date, Calendar.YEAR, year);
	}

	/**
	 * 为一个日期增加对应的月份
	 * 
	 * @param date
	 * @param month
	 * @return
	 */
	public static Date addMonth(Date date, int month)
	{
		return add(date, Calendar.MONTH, month);
	}

	/**
	 * 为一个日期增加对应的天数
	 * 
	 * @param date
	 * @param day
	 * @return
	 */
	public static Date addDay(Date date, int day)
	{
		return add(date, Calendar.DAY_OF_MONTH, day);
	}

//	public static boolean isLeapYear(int year)
//	{
//		return (year % 4 == 0 && year % 100 != 0) || (year % 400 == 0);
//	}

//	private static int getLastDayOfMonth(int year, int month)
//	{
//		if(month == 1 || month == 3 || month == 5 || month == 7 || month == 8
//				|| month == 10 || month == 12)
//		{
//			return 31;
//		}
//		if(month == 4 || month == 6 || month == 9 || month == 11)
//		{
//			return 30;
//		}
//		if(month == 2)
//		{
//			if(isLeapYear(year))
//			{
//				return 29;
//			}
//			else
//			{
//				return 28;
//			}
//		}
//		return 0;
//	}

	/**
	 * 返回month月所属季度的最后一天日期yyyy-MM-dd
	 * 
	 * @param month
	 * @return
	 * @throws ParseException
	 */
	public static Date getThisSeasonTime(Date date) throws ParseException
	{
//		int month = date.getMonth();
//		int array[][] = {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}, {10, 11, 12}};
//		int season = 1;
//		if(month >= 1 && month <= 3)
//		{
//			season = 1;
//		}
//		if(month >= 4 && month <= 6)
//		{
//			season = 2;
//		}
//		if(month >= 7 && month <= 9)
//		{
//			season = 3;
//		}
//		if(month >= 10 && month <= 12)
//		{
//			season = 4;
//		}
//		int end_month = array[season - 1][2];
//		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy");
//		int years_value = Integer.parseInt(dateFormat.format(date));
//		int end_days = getLastDayOfMonth(years_value, end_month);
//		String seasonDate = years_value + "-" + end_month + "-" + end_days;
//		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
//		Date datea = df.parse(seasonDate);
//		return datea;
		return getLastDayOfQuarter(date);
	}
	
	/**
	 * 获得date所处季度的最后一个月的最后一天
	 * @param date
	 * @return
	 */
	public static Date getLastDayOfQuarter(Date date)
	{
		Calendar c = getCalendarForDate(date);
		int quarter = getQuarter(date);
		int lastMonthOfQuarter = getLastMonthOfQuarter(quarter);
		c.set(Calendar.MONTH, lastMonthOfQuarter);
		c.set(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DAY_OF_MONTH));
		return c.getTime();
	}
	/**
	 * 获得date所处季度的最后一个月的第一天
	 * @param date
	 * @return
	 */
	public static Date getLastDayOfQuarterF(Date date)
	{
		Calendar c = getCalendarForDate(date);
		int quarter = getQuarter(date);
		int lastMonthOfQuarter = getLastMonthOfQuarter(quarter);
		c.set(Calendar.MONTH, lastMonthOfQuarter);
		c.set(Calendar.DAY_OF_MONTH, c.getActualMinimum(Calendar.DAY_OF_MONTH));
		return c.getTime();
	}
	
	/**
	 * 获取quarter季度的最后一个月，0表示1月、1表示2月，以此类推
	 * @param quarter
	 * @return
	 */
	public static int getLastMonthOfQuarter(int quarter)
	{
		return quarter * 3 - 1;
	}
	
	/**
	 * 获取date所属的季度
	 * @param date
	 * @return
	 */
	public static int getQuarter(Date date)
	{
		Calendar c = getCalendarForDate(date);
		int month = c.get(Calendar.MONTH);
		return month / 3 + 1;
	}
	
	/**
	 * 获取系统当前时间
	 */
	public static Date timeDate() {
		Date currentTime = new Date();
		Date currentTime1 = new Timestamp(currentTime.getTime());
		Date ndate = (Date) (currentTime1);
		return ndate;
	}

	/**
	 * 将Date转换为String类型
	 * 
	 * @param d
	 * @param format
	 * @return
	 */
	public static String date2String(Date d, String format)
	{
		if(d == null)
			return "";
		DateFormat df = new SimpleDateFormat(format);
		return df.format(d);
	}

	/**
	 * 将Date转换为String类型 采用默认格式转换yyyy-MM-dd HH:mm:ss
	 * 
	 * @param d
	 * @return
	 */
	public static String date2String(Date d)
	{
		return date2String(d, "yyyy-MM-dd HH:mm:ss");
	}
	
	/**
	 * 将Date转换为String类型 采用默认格式转换yyyy-MM-dd 
	 * 
	 * @param d
	 * @return
	 */
	public static String getDateStrYMD(Date d)
	{
		return date2String(d, "yyyy-MM-dd");
	}

	/**
	 * 将String转换为Date类型
	 * 
	 * @param str
	 * @param format
	 * @return
	 */
	public static Date string2Date(String str, String format)
	{
		DateFormat df = new SimpleDateFormat(format);
		try
		{
			return df.parse(str);
		}
		catch(ParseException e)
		{
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 将String转换为Date类型 采用默认格式转换yyyy-MM-dd HH:mm:ss
	 * 
	 * @param str
	 * @return
	 */
	public static Date string2Date(String str)
	{
		return string2Date(str, "yyyy-MM-dd HH:mm:ss");
	}
	
	public static Date string2DateNoTime(String str)
	{
		return string2Date(str, "yyyy-MM-dd");
	}

	public static String getNowStr()
	{
		return date2String(new Date(), "yyyyMMddHHmmssS");
	}
	
	/**
	 * 将Timestamp转换为Date类型
	 * @param timeStamp
	 * @return
	 */
	public static Date timestamp2Date(Timestamp timeStamp)
	{
		if(timeStamp == null)
		{
			return null;
		}
		else
		{
			long time = timeStamp.getTime();
			return new Date(time);
		}
	}
	
	/**
	 * 判断当前时间是否处于两个时间之间
	 * @param begin
	 * @param end
	 * @return
	 */
	public static boolean isBetweenDates(Date begin, Date end)
	{
		Date now = new Date();
		return (now.after(begin) || now.equals(begin)) && (now.before(addDay(end, 1)) || now.equals(end));
	}
	
	/**
	 * 判断时间是否处于两个时间之间
	 * @param begin
	 * @param end
	 * @return
	 */
	public static boolean isBetweenNow(Date begin, Date end,Date now)
	{
		return (now.after(begin) || now.equals(begin)) && (now.before(addDay(end, 1)) || now.equals(end));
	}
	
	
	  /** *//**   
	    *   返回月份   
	    *     
	    *   @param   date   
	    *                         日期   
	    *   @return   返回月份   
	    */   
	  public   static   int   getMonth(Date   date){
	  Calendar   c   =   Calendar.getInstance();
	  c.setTime(date);   
	  return   c.get(Calendar.MONTH);
	  }   
	  
	  /** *//**   
	    *   返回日份   
	    *     
	    *   @param   date   
	    *                         日期   
	    *   @return   返回日份   
	    */   
	  public   static   int   getDay(Date   date) {
	  Calendar   c   =   Calendar.getInstance();
	  c.setTime(date);   
	  return   c.get(Calendar.DAY_OF_MONTH);
	  }   

	  /** *//**   
	    *   返回年份   
	    *     
	    *   @param   date   
	    *                         日期   
	    *   @return   返回年份   
	    */   
	  public   static   int   getYear(Date   date) {
	  Calendar   c   =   Calendar.getInstance();
	  c.setTime(date);   
	  return   c.get(Calendar.YEAR);
	  } 

	  /**
	   * 返回当前时间是否在有效期的范围内
	   * @param time
	   * @return
	 * @throws ParseException 
	   */
	  public static boolean checkValidate(Date time) throws ParseException{
		  boolean flag = false;
		  Date nowDate = new  Date();
		  
		  SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		  String snowDate=sdf.format(nowDate);
		  Date tnowDate=sdf.parse(snowDate);
		  
		  Calendar cal1 = Calendar.getInstance();
		  cal1.setTime(time);
		  cal1.add(Calendar.MONTH,-3);
		  Date beforeDate =	cal1.getTime();
		 
		  Calendar cal2 = Calendar.getInstance();
		  cal2.setTime(time);
		  cal2.add(Calendar.MONTH, +6);
		  Date afterDate =	cal2.getTime();
		  
		  if(tnowDate.after(beforeDate) && tnowDate.before(afterDate)){
			  flag = true;
		  }
		  
		  return flag;
	  }
	  /**
	   * 将时间格式化为   "0 18 17 08 07 ? 2016"
	   * 自坐向右，为年，月，日，分,秒
	   * @param date
	   * @return
	   */
	  public static String getJobDataString(Date date){
			Calendar cld = Calendar.getInstance();
			cld.setTime(date);
			int year = cld.get(Calendar.YEAR);
			int month = cld.get(Calendar.MONTH)+1;
			int day = +cld.get(Calendar.DAY_OF_MONTH);
			int hour =cld.get(Calendar.HOUR_OF_DAY);
			int minute = cld.get(Calendar.MINUTE);
			int second = cld.get(Calendar.SECOND);
			String dataString = second+" "+minute+" "+hour+" "+day+" "+month+" ? "+year;
		  return dataString;
	  }
	  
	  /**
	  * 通过时间秒毫秒数判断两个时间的间隔计算两个日期的之间的天数
	  * @param fromDate 开始时间
	  * @param toDate 结束时间
	  * @return
	  */
	  public static int getDaysByMillisecond(Date fromDate,Date toDate)
	  {
	  int days = (int) ((toDate.getTime() - fromDate.getTime()) / (1000*3600*24));
	  return days;
	  }


}
