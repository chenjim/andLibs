package com.chenjim.andlibs.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * @description：
 * @fileName: CalendarUtils
 * @author: jim.chen
 * @date: 2020/1/19
 */
public class CalendarUtils {


    /**
     * @Description: 获取某个月的某一天的日期
     * 例如：下个月的第一天：MONTH = 1，DAY = 1
     */
    public static String getMonthFirstDay(int MONTH, int DAY) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        //获取当前月第一天：
        Calendar c = Calendar.getInstance();
        c.add(Calendar.MONTH, MONTH);

        //设置为1号,当前日期既为本月第一天
        c.set(Calendar.DAY_OF_MONTH, DAY);
        String first = format.format(c.getTime());
        return first;
    }

}
