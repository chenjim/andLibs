package com.chenjim.andlibs.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @description：
 * @fileName: DateUtils
 * @author: jim.chen
 * @date: 2020/1/19
 */
public class DateUtils {

    /**
     * 时间戳转换成字符窜
     *
     * @param milSecond
     * @param pattern
     * @return
     */
    public static String getDateToString(long milSecond, String pattern) {
        Date date = new Date(milSecond);
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        return format.format(date);
    }

    /**
     * 将字符串转为时间戳
     *
     * @param dateString
     * @param pattern
     * @return
     */
    public static long getStringToDate(String dateString, String pattern) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
        Date date = new Date();
        try {
            date = dateFormat.parse(dateString);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return date.getTime();
    }


    /**
     * 下个月1号
     *
     * @return {@link java.util.Date}
     */
    public static Date getNextMonthDate() {
        Date date = new Date();
        int curYear = date.getYear();
        int curMonth = date.getMonth();
        int newMoth = curMonth + 1;
        int newYear = curYear;
        if (newMoth > 11) {
            newMoth = 0;
            newYear += 1;
        }
        date = new Date(newYear, newMoth, 1);
        return date;
    }

    /**
     * 上个月1号
     *
     * @return {@link java.util.Date}
     */
    public static Date getLastMonthDate() {
        Date date = new Date();
        int curYear = date.getYear();
        int curMonth = date.getMonth();
        int newMoth = curMonth - 1;
        int newYear = curYear;
        if (newMoth < 0) {
            newMoth = 11;
            newYear -= 1;
        }
        date = new Date(newYear, newMoth, 1);
        return date;
    }

}
