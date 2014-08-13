package com.meiliwan.emall.commons.util;


import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang.StringUtils;
/**
 * 日期转换工具
 */
public class DateUtil
{
    public static final String FORMAT_DATETIME = "yyyy-MM-dd HH:mm:ss";
    public static final String FORMAT_DATE = "yyyy-MM-dd";
    public static final String FORMAT_DATE_INT ="yyyyMMdd";
    
    public static long SECOND_TIME = 1000L;
    public static long MINUTE_TIME = 60 * SECOND_TIME;
    public static long HOUR_TIME   = 60 * MINUTE_TIME;
    public static long DAY    = 24 * HOUR_TIME;
    
    private final static SimpleDateFormat MONTH_FORMAT = new SimpleDateFormat(
    "yyyyMM");
    private final static SimpleDateFormat TIME_IDENTITY_FORMAT = new SimpleDateFormat(
    "yyyyMMddHHmmss");

    
    public static final SimpleDateFormat DATETIME_FORMAT = new SimpleDateFormat(FORMAT_DATETIME);

    
    public static String timeAdd(Date date, long diffTime, TimeUnit unit){
    	long resultTime = date.getTime() + unit.toMillis(diffTime);
    	
    	return getDatetimeStr(resultTime);
    }

    public static String timeSub(Date date, long diffTime, TimeUnit unit){
        long resultTime = date.getTime() - unit.toMillis(diffTime);

        return getDatetimeStr(resultTime);
    }


    /**
     * 
     * @return 返回标示性的时间戳，格式“yyyyMMddHHmmss”
     */
    public static String getIdentityTime() {
        return TIME_IDENTITY_FORMAT.format(new Date());
    }
    
    /**
     * 
     * @param date 日期
     * @return 返回月份字符串
     */
    public static String getMonthStr(Date date){
    	return MONTH_FORMAT.format(date);
    }
    
    public static boolean isDate(String date){
    	return date.matches("\\d{4}-\\d{2}-\\d{2}");
    }
    public static String getCurrentDateStr(){
    	return getCurrentDateStr(FORMAT_DATETIME);
    }
    public static String getDatetimeStr(long datetime){
    	return format(new Date(datetime),FORMAT_DATETIME);
    }
    /**
     * 
     * @param datetime
     * @return 返回格式yyyy-MM-dd HH:mm:ss
     */
    public static String getDatetimeStr(Date datetime){
    	return format(datetime,FORMAT_DATETIME);
    }
    public static String getDateStr(long datetime){
    	return format(new Date(datetime),FORMAT_DATE);
    }
	
    public static Date getCurrentDate(){
	     Calendar cal = Calendar.getInstance();
	     Date currDate = cal.getTime();
	     return currDate;
    }
    public static Date datetimeStrToDate(String datetimeStr){
	    Date datetime = null;
    	try {
    		datetime = DATETIME_FORMAT.parse(datetimeStr);
		} catch (ParseException e) {
			e.printStackTrace();
		}
    	return datetime;
   }
    public static String getNoSplitDateStr(long datetime){
    	return format(new Date(datetime),"yyyyMMdd");
    }
    
    public static Timestamp getCurrentTimestamp() {
        return new Timestamp(new Date().getTime());
    }

    public static Timestamp getTimestampByDate(Date date) {
        return new Timestamp(date.getTime());
    }

    public static Date getDateByTimestamp(Timestamp timestamp) {
        return new Date(timestamp.getTime());
    }

    /**
     * 传入 yyyy-MM-dd HH:mm:ss 字符串 获取 Timestamp
     * @param yyyyMMddHHmmss
     * @return
     */
    public static Timestamp getTimestampByStringYYYYMMddHHmmss(String yyyyMMddHHmmss) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        try {
            date = df.parse(yyyyMMddHHmmss);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
        return new Timestamp(date.getTime());
    }

    /**
     * 传入 yyyy-MM-dd HH:mm:ss 是否是规定的时间格式，防止sql注入
     * @param yyyyMMddHHmmss
     * @return
     */
    public static boolean isFixTimestamp(String yyyyMMddHHmmss) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            df.parse(yyyyMMddHHmmss);
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * 传入 Timestamp  获取 yyyy-MM-dd HH:mm:ss 字符串
     * @param timestamp
     * @return
     */
    public static String getStringYYYYMMddHHmmssByTimestamp(Timestamp timestamp) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return df.format(new Date(timestamp.getTime()));
    }

    /**
     * 对比开始时间是否比结束时间晚
     * @param start
     * @param end
     * @return
     */
    public static boolean isPast(Timestamp start, Timestamp end) {
        if (ObjectUtils.isExistNullObj(start, end)) {
            return true;
        }
        return end.getTime() - start.getTime() < 0;
    }

    /**
     * 对比开始时间是否比结束时间晚
     * @param start
     * @param end
     * @return
     */
    public static boolean isNotPast(Date start, Date end) {
        if (ObjectUtils.isExistNullObj(start, end)) {
            return false;
        }
        return end.getTime() - start.getTime() > 0;
    }

    /**
	 * param   2010-09-01
	 */
    public static Date parseStringToDate(String date_str){
		SimpleDateFormat sdf = new SimpleDateFormat(FORMAT_DATE);
		Date date = null;
		try {
			date = sdf.parse(date_str);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}


    
    public static Date parseStringToDate(String date_str,String format){
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		Date date = null;
		try {
			date = sdf.parse(date_str);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}
    
    public static String parseLoadTime(long beginTime) {
        long during = System.currentTimeMillis() - beginTime;
        if (during < SECOND_TIME) return during + " millis";

        String loadTimeString = "";
        long minutes = during / MINUTE_TIME;
        long seconds = during / SECOND_TIME;

        if (minutes > 0) loadTimeString += minutes + "minutes ";
        if (seconds > 0) loadTimeString += seconds + "seconds ";

        return loadTimeString += (during % SECOND_TIME) + "millis";
    }
	
    private static String getCurrentDateStr(String strFormat){
	     Calendar cal = Calendar.getInstance();
	     Date currDate = cal.getTime();
	     return format(currDate, strFormat);
	 }

    private static String format(Date aTs_Datetime, String as_Pattern){
      if (aTs_Datetime == null || as_Pattern == null){
    	  return null;
      }
      SimpleDateFormat dateFromat = new SimpleDateFormat(as_Pattern);
      return dateFromat.format(aTs_Datetime);
    }
    /**
     * 得到从今天开始，此刻的总秒数
     * @return
     */
    public static int todayNowTime(){
    	Calendar today = Calendar.getInstance();
        today.setTimeInMillis(System.currentTimeMillis());
        int hour = today.get(Calendar.HOUR_OF_DAY);
        int minute = today.get(Calendar.MINUTE);
        int second = today.get(Calendar.SECOND);
        today.set(Calendar.HOUR_OF_DAY, 0);
        today.set(Calendar.MINUTE, 0);
        today.set(Calendar.SECOND, 0);
        long beginTime = today.getTimeInMillis() / 1000;
        today.set(Calendar.HOUR_OF_DAY, hour);
        today.set(Calendar.MINUTE, minute);
        today.set(Calendar.SECOND, second);
        long endTime = today.getTimeInMillis() / 1000;
        return (int) (endTime-beginTime + 1);
    }
    
    /**
     * 获取今天的开始时间
     * @return  yyyy-mm-dd 00:00:00
     */
    public static String getCurrentDateStart(){
    	Calendar cal = Calendar.getInstance();
    	Date dt = cal.getTime() ;
    	String dateStr = getDateStr(dt.getTime()) ;
    	String currentDateStart = dateStr + " 00:00:00" ;
    	return currentDateStart ;
    }
    
    /**
     * 获取昨天的开始时间
     * @return  yyyy-mm-dd 00:00:00
     */
    public static String getYesterdayStart(){
    	Calendar cal = Calendar.getInstance();
    	cal.add(Calendar.DATE, -1);
    	Date dt = cal.getTime() ;
    	String dateStr = getDateStr(dt.getTime()) ;
    	String currentDateStart = dateStr + " 00:00:00" ;
    	return currentDateStart ;
    }
    
    /**
     * 获取今天的结束时间
     * @return  yyyy-mm-dd 23:59:59
     */
    public static String getCurrentDateEnd(){
    	Calendar cal = Calendar.getInstance();
    	Date dt = cal.getTime() ;
    	String dateStr = getDateStr(dt.getTime()) ;
    	String currentDateEnd = dateStr + " 23:59:59" ;
    	return currentDateEnd ;
    }

    /**
     * 获取某天的开始或者结束
     * @param theDate
     * @param isStart
     * @return
     */
    public static Date getStartOrEndOf(Date theDate,boolean isStart){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(theDate == null ? new Date() : theDate);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        if(!isStart){
          calendar.add(Calendar.DAY_OF_MONTH,1);
        }
        return calendar.getTime();
    }

    /**
     * 获取距离某天前后多少days的日期
     * @param theDate
     * @param days
     * @return
     */
    public static Date getDateAway(Date theDate,int days){
        Calendar cal = Calendar.getInstance();
        cal.setTime(theDate == null?new Date():theDate);
        cal.add(Calendar.DAY_OF_MONTH, days);
        return cal.getTime();
    }

    /**
     * 获取某月的月头和月尾
     * @param theDate
     * @param isFirst
     * @return
     */
    public static Date getFirstOrLastDateBy(Date theDate,boolean isFirst){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(theDate);
        calendar.set(Calendar.DAY_OF_MONTH,isFirst?1:calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        return calendar.getTime();
    }

    public static Integer parseDate2Integer(Date theDate,String format){
        String stringDate = parseDate2Str(theDate,format);
        if(StringUtils.isEmpty(stringDate)) return null;
        try {
           return Integer.parseInt(stringDate);
        }catch (Exception e){
            return null;
        }
    }

    /**
     * 格式化Date
     * @param theDate
     * @param format
     * @return
     */
    public static String parseDate2Str(Date theDate,String format){
        try {
            SimpleDateFormat sf = new SimpleDateFormat(format);
            return sf.format(theDate);
        }catch (Exception e){
            return null;
        }
    }
    
	public static void main(String[] args){
		System.out.println(getCurrentDateStr());
		System.out.println(getDatetimeStr(System.currentTimeMillis()));
        System.out.println(getCurrentDateStr("yyyy.MM.dd HH:mm:ss"));
        System.out.println(format(new Date(System.currentTimeMillis()),"yyyy.MM.dd HH:mm:ss"));
        System.out.println(timeSub(new Date(),31,TimeUnit.DAYS));
        System.out.println(getStartOrEndOf(getDateAway(null, -1),true));
        System.out.println(getStartOrEndOf(getDateAway(null, -1),false));

	 }
} 


