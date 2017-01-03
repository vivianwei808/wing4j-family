package org.wing4j.common.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 日期工具类
 */
public class DateUtils extends org.apache.commons.lang3.time.DateUtils{
    /**
     * 一秒钟对应的毫秒
     */
    public static final int SEC = 1000;
    /**
     * 一分钟对应的毫秒
     */
    public static final int MIN = 60 * SEC;
    /**
     * 一小时对应的毫秒
     */
    public static final int HOUR = 60 * MIN;
    /**
     * 一天对应的毫秒
     */
    public static final int DAY = 24 * HOUR;
    /**
     * 注册的日期掩码
     */
    static Map<String, DateStyle> PATTERNS = new HashMap<String, DateStyle>();

    /**
     * 注册日期掩码
     */
    static {
        for (DateStyle dateStyle : DateStyle.values()){
            PATTERNS.put(dateStyle.mask, dateStyle);
        }
    }

    static boolean isBlank(String s) {
        return s == null || s.isEmpty();
    }

    /**
     * 自动获取匹配的日期掩码
     *
     * @param date 日期字符串
     * @return 日期掩码
     */
    public static DateStyle getPattern(String date) {
        if (isBlank(date)) {
            throw new NullPointerException("input date string is null");
        }
        Set<String> patterns = PATTERNS.keySet();
        for (String pattern : patterns) {
            if (date.matches(pattern)) {
                return PATTERNS.get(pattern);
            }
        }
        throw new IllegalArgumentException("input date string is illegal argument");
    }


    /**
     * 将java日期对象根据指定的格式字符串，格式化为字符串
     *
     * @param date   日期对象
     * @param dateStyle 日期样式
     * @return 日期字符串
     */
    public static String formatJavaDate2String(java.util.Date date, DateStyle dateStyle) {
        if(date == null){
            return "";
        }

        SimpleDateFormat dateForamt = new SimpleDateFormat(dateStyle.format);
        return dateForamt.format(date);
    }

    /**
     * 将日期字符串转换成日期对象
     *
     * @param s
     * @return
     */
    public static Date toDate(String s) {
        DateStyle dateStyle = validate(s);
        return toDate(s, dateStyle);
    }

    /**
     * 将日期字符串转换成日期对象
     *
     * @param s
     * @return
     */
    static Date toDate(String s, DateStyle dateStyle) {
        SimpleDateFormat dateForamt = new SimpleDateFormat(dateStyle.format);
        try {
            return dateForamt.parse(s);
        } catch (ParseException e) {
            throw new IllegalArgumentException("Illegal date[" + s + "]");
        }
    }

    /**
     * 将日起对象格式化为指定格式的字符串
     * @param date 日期对象
     * @param dateStyle 日期样式
     * @return
     */
    public static String toString(java.util.Date date, DateStyle dateStyle) {
        return formatJavaDate2String(date, dateStyle);
    }
    /**
     * 将日起对象格式化为yyyyMMdd格式的字符串
     * @param date 日期对象
     * @return 字符串
     */
    public static String toString(java.util.Date date) {
        return formatJavaDate2String(date, DateStyle.FILE_FORMAT4);
    }

    /**
     * 将日起对象格式化为yyyyMMddHHmmss格式的字符串
     * @param date 日期对象
     * @return 字符串
     */
    public static String toFullString(java.util.Date date) {
        return formatJavaDate2String(date, DateStyle.FILE_FORMAT2);
    }

    //-------------------------------功能函数---------------------------------------------------------------

    /**
     * 得到当前日期yyyyMMdd
     *
     * @return
     */
    public static String getCurrDate() {
        return formatJavaDate2String(new java.util.Date(), DateStyle.FILE_FORMAT3);
    }

    /**
     * 得到当前时间HHmmss
     *
     * @return
     */
    public static String getCurrTime() {
        return formatJavaDate2String(new java.util.Date(), DateStyle.FILE_FORMAT4);
    }

    /**
     * 得到当前日期和时间 "yyyy-MM-dd HH:mm:ss";
     *
     * @return
     */
    public static String getCurrFullTime() {
        return formatJavaDate2String(new java.util.Date(), DateStyle.SECOND_FORMAT1);
    }

    /**
     * 获取当前系统时间戳，否则获取参数的时间戳 格式"yyyyMMddHHmmss";
     */
    public static String getTimestamp() {
        return formatJavaDate2String(new java.util.Date(), DateStyle.FILE_FORMAT2);
    }

    /**
     * 计算输入日期n秒之后
     * @param date 输入日期对象
     * @param n_sec 秒数
     * @return
     */
    public static Date getNextSec(Date date, long n_sec){
        long dateL = date.getTime();
        long n_dateL = dateL + SEC * n_sec;
        Date n_date = new Date(n_dateL);
        return n_date;
    }
    /**
     * 计算输入日期n秒之后
     * @param date 输入日期对象
     * @param n_sec 秒数
     * @return
     */
    public static String getNextSec(String date, long n_sec){
        DateStyle dateStyle = validate(date);
        java.util.Date oldDate = toDate(date);
        java.util.Date newDate = getNextSec(oldDate, n_sec);
        return formatJavaDate2String(newDate, dateStyle);
    }
    /**
     * 计算输入日期n天之后
     *
     * @param date 输入日期对象
     * @param n    n天
     * @return
     */
    public static Date getNextDay(Date date, int n) {
        // 当前日期的毫秒数增加n天对应的毫秒数
        long n_sec = 24 * 60 * 60 * n;
        return getNextSec(date, n_sec);
    }

    /**
     * 计算输入日期n天之后
     *
     * @param date 输入日期对象
     * @param n    n天
     * @return
     */
    public static String getNextDay(String date, int n) {
        DateStyle dateStyle = validate(date);
        java.util.Date oldDate = toDate(date);
        java.util.Date newDate = getNextDay(oldDate, n);
        return formatJavaDate2String(newDate, dateStyle);
    }

    /**
     * 检验日期字符串，如果没有匹配的格式抛出异常
     *
     * @param date
     * @return
     */
    public static DateStyle validate(String date) {
        DateStyle dateStyle = getPattern(date);
        if (dateStyle == null) {
            throw new IllegalArgumentException("Illegal date[" + date + "]");
        }
        return dateStyle;
    }

    /**
     * 取两个日期的天数
     *
     * @param startDate 起始日期
     * @param endDate   终点日期
     */
    public static int getDaysBetween(String startDate, String endDate)
            throws IllegalArgumentException {
        DateStyle dateStyle1 = validate(startDate);
        DateStyle dateStyle2 = validate(endDate);
        Date startD = toDate(startDate, dateStyle1);
        Date endD = toDate(endDate, dateStyle2);
        long startTime = startD.getTime();
        long endTime = endD.getTime();
        long offset = endTime - startTime;
        long days = offset / (long) DAY;
        return (int) days;
    }

    /**
     * 获取输入日期的当月第一天
     *
     * @param s 输入日期字符串
     * @return 日期字符串
     */
    public static String getFirstDayOfMonth(String s) {
        java.util.Date date = toDate(s);
        return formatJavaDate2String(getNDayOfMonth(date, 1), DateStyle.FILE_FORMAT3);
    }

    /**
     * 获取输入日期的当月第n天
     *
     * @param date 输入日期
     * @param n    第几天，从1开始
     * @return 当月第n天日期
     */
    static java.util.Date getNDayOfMonth(java.util.Date date, int n) {
        Calendar calendar = new GregorianCalendar();
        calendar.set(Calendar.DATE, n);
        calendar.setTime(date);
        return calendar.getTime();
    }

    /**
     * 获取输入日期的当月最后一天
     *
     * @param date 输入日期
     * @return 当月最后一天日期
     */
    static java.util.Date getLastDayOfMonth(java.util.Date date) {
        Calendar calendar = new GregorianCalendar();
        calendar.set(Calendar.DATE, 1);
        calendar.roll(Calendar.DATE, -1);
        calendar.setTime(date);
        return calendar.getTime();
    }

    /**
     * 获取输入日期的当月最后一天
     *
     * @param s 输入日期字符串
     * @return 日期字符串
     */
    public static String getLastDayOfMonth(String s) {
        java.util.Date date = toDate(s);
        return formatJavaDate2String(getLastDayOfMonth(date), DateStyle.FILE_FORMAT3);
    }
}
