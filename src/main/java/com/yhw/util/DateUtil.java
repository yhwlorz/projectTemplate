package com.yhw.util;

import org.apache.commons.lang3.time.DateUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil extends DateUtils {

    /**
     * 根据日期和转换格式得到字符串
     * @param date 日期
     * @param resultFormat 转换格式 例如  yyyy-MM-dd
     * @return
     */
    public static String date2DateStr(Date date, String resultFormat){
        return new SimpleDateFormat (resultFormat).format(null==date?new Date():date);
    }

    /**
     * 根据日期字符串和转换格式得到日期
     * @param date 日期字符串 例如 20120506
     * @param paramFormat 转换格式 例如 yyyyMMdd
     * @return
     */
    public static Date dateStr2Date(String date, String paramFormat){
        try{
            SimpleDateFormat sdf = new SimpleDateFormat(paramFormat);
            return sdf.parse(date);
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 根据传入的日期获取指定类型的日期对象
     * @param date 传入的日期
     * @param resultFormat 转换格式
     * @return
     */
    public static Date date2Date(Date date, String resultFormat){
        try{
            SimpleDateFormat sdf = new SimpleDateFormat(resultFormat);
            String dateString = sdf.format(null==date?new Date():date);
            return sdf.parse(dateString);
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 时间戳转换成日期格式字符串
     * @param timeStamp 10/13位
     * @param resultFormat 要转成的样式。默认"yyyy-MM-dd HH:mm:ss"
     * @return
     */
    public static String timeStamp2DateStr(long timeStamp,String resultFormat) {
        if ((timeStamp + "").length () == 13) {
            timeStamp /= 1000;
        }
        if(resultFormat == null || resultFormat.isEmpty()){
            resultFormat = "yyyy-MM-dd HH:mm:ss";
        }
        SimpleDateFormat sdf = new SimpleDateFormat(resultFormat);
        return sdf.format(new Date(Long.valueOf(timeStamp+"000")));//new Date() 里的时间戳必须是13位
    }

    public static Date timeStamp2Date(long timeStamp) {
        if ((timeStamp + "").length () == 10) {
            timeStamp *= 1000;
        }
        return new Date (timeStamp);
    }

    /**
     * 日期格式字符串转换成10位时间戳
     * @param dateStr "2018-06-30 20:00:00"
     * @param paramFormat 如：yyyy-MM-dd HH:mm:ss
     * @return 1530360000
     */
    public static long dateStr210TimeStamp(String dateStr, String paramFormat){
       return dateStr213TimeStamp (dateStr, paramFormat)/1000;
    }
    public static long dateStr213TimeStamp(String dateStr, String format){
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            return sdf.parse(dateStr).getTime();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    public static long date210TimeStamp(Date date){
        return date213TimeStamp (date)/1000;
    }
    public static long date213TimeStamp(Date date){
        return date.getTime ();
    }

    /**
     * 取得当前时间戳（精确到秒）
     * @return
     */
    public static long current10TimeStamp(){
        return current13TimeStamp () / 1000;
    }
    public static long current13TimeStamp(){
        long time = System.currentTimeMillis();
        String t = String.valueOf(time);
        return time;
    }


    /**
     * 得到当月第一天的日期
     * @return
     */
    public static Date getFirstDayOfCurrMonth(){
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DATE, 1);
        return calendar.getTime();
    }
    /**
     * 得到当月最后一天的日期
     * @return
     */
    public static Date getEndDayOfCurrMonth(Date date){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MONTH, 1);    //加一个月
        calendar.set(Calendar.DATE, 1);        //设置为该月第一天
        calendar.add(Calendar.DATE, -1);    //再减一天即为上个月最后一天
        return calendar.getTime();
    }
    /**
     * 得到当年第一天的日期
     * @return
     */
    public static Date getFirstDayOfCurrYear(){
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_YEAR, 1);
        return calendar.getTime();
    }

    /**
     * 得到指定年份第一天的日期
     * @return
     */
    public static Date getFirstDayOfCurrYear(Date date){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.DAY_OF_YEAR, 1);
        return calendar.getTime();
    }

    /**
     * 根据传入的日期得到天
     * @param 
     */
    public static int getDay(Date date){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.DATE);
    }

    /**
     * 根据传入的日期得到月
     * @param 
     */
    public static int getMonth(Date date){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.MONTH)+1;
    }

    /**
     * 根据传入的日期得到年
     * @param 
     */
    public static int getYear(Date date){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.YEAR);

    }

    /**
     * 根据传入的日期和类型 得到指定时间  (例如 getDateBefore（new Date(),"day",-1,5） 为得到当前日期之前5天的日期)
     * @param date 指定日期
     * @param type$day_month_year 日期类型  year month day
     * @param before0After1  之前还是之后  0为之前 1为之后
     * @param howlong  指定的数字   前5个月  即为5
     * @return 结果日期
     */
    public static Date getDateBefore(Date date,String type$day_month_year,int before0After1,int howlong){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        if(before0After1==1){
            if(type$day_month_year.equals("day")){
                calendar.add(Calendar.DATE, howlong);
            }else if(type$day_month_year.equals("month")){
                calendar.add(Calendar.MONTH, howlong);
            }else if(type$day_month_year.equals("year")){
                calendar.add(Calendar.YEAR, howlong);
            }
        }else{
            if(type$day_month_year.equals("day")){
                calendar.add(Calendar.DATE, -howlong);
            }else if(type$day_month_year.equals("month")){
                calendar.add(Calendar.MONTH, -howlong);
            }else if(type$day_month_year.equals("year")){
                calendar.add(Calendar.YEAR, -howlong);
            }
        }

        return calendar.getTime();
    }

    public static long get10TimeStampAfter(long currentTimeStamp,int afterMinutes){
        return get13TimeStampAfter (currentTimeStamp, afterMinutes) / 1000;
    }
    public static long get13TimeStampAfter(long currentTimeStamp,int afterMinutes){
        Date date = addMinutes (timeStamp2Date (currentTimeStamp), afterMinutes);
        return date213TimeStamp (date);
    }



}
