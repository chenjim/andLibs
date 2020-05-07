package com.chenjim.andlibs.utils;

/**
 * @description：
 * @fileName: GpsDistanceUtil
 * @author: jim.chen
 * @date: 2020/4/30
 */
public class GpsDistanceUtil {
    // 单位米 radius of earth
    private static final double DEF_R = 6370693.5;
    // PI/180.0
    private static final double DEF_PI180 = 0.01745329252;

    /**
     * @param lat1 经纬度
     * @param lon1
     * @param lat2
     * @param lon2
     * @return 单位米
     */
    public static double getDistance(double lat1, double lon1, double lat2, double lon2) {
        double ew1, ns1, ew2, ns2;
        double distance;
        // 角度转换为弧度
        ew1 = lon1 * DEF_PI180;
        ns1 = lat1 * DEF_PI180;
        ew2 = lon2 * DEF_PI180;
        ns2 = lat2 * DEF_PI180;
        // 求大圆劣弧与球心所夹的角(弧度)
        distance = Math.sin(ns1) * Math.sin(ns2) + Math.cos(ns1) * Math.cos(ns2) * Math.cos(ew1 - ew2);
        // 调整到[-1..1]范围内，避免溢出
        if (distance > 1.0) {
            distance = 1.0;
        } else if (distance < -1.0) {
            distance = -1.0;
        }
        // 求大圆劣弧长度
        distance = DEF_R * Math.acos(distance);
        return distance;
    }
}
