package com.chenjim.andlibs.utils;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * @description：
 * @fileName: DeviceUtils
 * @author: jim.chen
 * @date: 2020/1/19
 */
public class DeviceUtils {


    private static final String TAG = "DeviceUtils";

    /**
     * 重启设备
     *
     * @param context
     */
    public static void reboot(Context context) {
        //PowerManager pm = (PowerManager)context.getSystemService(Context.POWER_SERVICE);
        //pm.reboot("now by app");

        Intent intent = new Intent(Intent.ACTION_REBOOT);
        intent.setAction(Intent.ACTION_REBOOT);
        intent.putExtra("nowait", 1);
        intent.putExtra("interval", 1);
        intent.putExtra("window", 0);
        context.sendBroadcast(intent);
    }


    /**
     * 关机
     */
    public static void shutdownDevice() {
        try {
            //调用asInterface方法获取IPowerManager对象
            Object oIPowerManager = AppHelper.getIPowerManager();
            //获得shutdown()方法
            Method shutdown = oIPowerManager.getClass().getMethod("shutdown", boolean.class, String.class, boolean.class);
            //调用shutdown()方法
            shutdown.invoke(oIPowerManager, false, true);
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }

    }

    /*
     * 获取手机温度
     */
    public static float getMTKCPUTemperature() {
        Map temperatureMap = new HashMap();
        BufferedReader br = null;
        float CpuTemperature = 0;

        try {
            File dir = new File("/sys/class/thermal/");

            File[] files = dir.listFiles(new FileFilter() {
                @Override
                public boolean accept(File file) {
                    if (Pattern.matches("thermal_zone[0-9]+", file.getName())) {
                        return true;
                    }
                    return false;
                }
            });

            final int SIZE = files.length;
            String line = null;
            String type = null;
            float temp = 0;
            for (int i = 0; i < SIZE; i++) {
                br = new BufferedReader(new FileReader("/sys/class/thermal/thermal_zone" + i + "/type"));
                line = br.readLine();
                if (line != null) {
                    type = line;
                }

                br = new BufferedReader(new FileReader("/sys/class/thermal/thermal_zone" + i + "/temp"));
                line = br.readLine();
                if (line != null) {
                    long temperature = 0;
                    try {
                        temperature = Long.parseLong(line);
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }
                    if (temperature < 0) {
                        temp = 0;
                    } else {
                        temp = (float) temperature / 1000;
                    }
                }
                temperatureMap.put(type, temp);
            }
            br.close();
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        if (temperatureMap.get("mtktscpu") != null) {
            CpuTemperature = (float) temperatureMap.get("mtktscpu");
        }
        return CpuTemperature;
    }

}
