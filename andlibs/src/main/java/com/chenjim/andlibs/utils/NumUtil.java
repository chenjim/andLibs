package com.chenjim.andlibs.utils;

/**
 * @author chenjim  me@h89.cn
 * @version 1.0
 * @description 数字处理
 * @date 2020/12/15
 */
public class NumUtil {

    public static float parseFloat(String text) {
        float result = 0F;
        if (text == null || text.length() == 0) {
            return result;
        }
        try {
            result = Float.parseFloat(text);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static int parseInt(String text) {
        int result = 0;
        if (text == null || text.length() == 0) {
            return result;
        }
        try {
            result = Integer.parseInt(text);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return result;
    }
}
