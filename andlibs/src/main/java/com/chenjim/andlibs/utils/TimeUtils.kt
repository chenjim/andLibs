package com.chenjim.andlibs.utils

import java.text.SimpleDateFormat
import java.util.*

/**
 * @description：
 * @fileName: DateUtils
 * @author: jim.chen
 * @date: 2020/1/19
 */

object TimeUtils {

    /**
     * 时间戳转换成字符窜
     *
     * @param milSecond
     * @param pattern
     * @return
     */
    @JvmStatic
    fun getCurDate(milSecond: Long, pattern: String?): String = SimpleDateFormat(pattern).format(Date(milSecond))

    /**
     * @return yyyy-MM-dd HH:mm:ss
     */
    @JvmStatic
    fun getCurDate(): String = SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Date())

    @JvmStatic
    fun getCurDate(format: String?): String = SimpleDateFormat(format).format(Date())

    /**
     * @return "yyyy_MM_dd_HHmmss_SSS"
     */
    @JvmStatic
    fun getCurDate2(): String = SimpleDateFormat("yyyyMMdd_HHmmss_SSS").format(Date())

    /**
     * @return "HH:mm:ss"
     */
    @JvmStatic
    fun getCurTime(): String = SimpleDateFormat("HH:mm:ss").format(Date())

    @JvmStatic
    fun getCurTime(format: String?): String = SimpleDateFormat(format).format(Date())


    /**
     * 下个月1号
     *
     * @return [java.util.Date]
     */
    @JvmStatic
    fun getNextMonthDate(): Date {
        val calendar = Calendar.getInstance()
        calendar[Calendar.DAY_OF_MONTH] = 1
        calendar.add(Calendar.MONTH, 1)
        return calendar.time
    }

    /**
     * 上个月1号
     *
     * @return [java.util.Date]
     */
    @JvmStatic
    fun getLastMonthDate(): Date {
        val calendar = Calendar.getInstance()
        calendar[Calendar.DAY_OF_MONTH] = 1
        calendar.add(Calendar.MONTH, -1)
        return calendar.time
    }

    @JvmStatic
    fun getMonthMaxDay(year: String, mouth: String): Int {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.YEAR, year.toInt())
        calendar[Calendar.MONTH] = mouth.toInt()
        return calendar.getActualMaximum(Calendar.DATE)
    }

    @JvmStatic
    fun getMonthMaxDay(year: Int, mouth: Int): Int {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.YEAR, year)
        calendar[Calendar.MONTH] = mouth
        return calendar.getActualMaximum(Calendar.DATE)
    }

}

