package com.chenjim.andlibs.utils;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.Context;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.os.SystemClock;
import android.os.storage.StorageManager;
import android.os.storage.StorageVolume;
import android.text.format.Formatter;
import android.util.Log;

import androidx.annotation.RequiresApi;

import java.io.File;
import java.lang.reflect.Array;
import java.lang.reflect.Method;

/**
 * @description：
 * @fileName: StorageUtils
 * @author: jim.chen
 * @date: 2020/1/19
 */
public class StorageUtils {
    private static final String TAG = "StorageUtils";

    /**
     * 得到内置存储空间的可用容量
     *
     * @param context
     * @return
     */
    public static String getAvailableSpace(Context context) {
        String path = Environment.getDataDirectory().getPath();
        StatFs statFs = new StatFs(path);
        long blockSize = statFs.getBlockSize();
        long totalBlocks = statFs.getBlockCount();
        long availableBlocks = statFs.getAvailableBlocks();
        long useBlocks = totalBlocks - availableBlocks;
        long rom_length = totalBlocks * blockSize;
        return Formatter.formatFileSize(context, availableBlocks * blockSize);
    }

    /**
     * 获取系统可用ram
     */
    public static long getAvailableRAM(Context context) {
        //获得ActivityManager服务的对象
        ActivityManager mActivityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        //获得MemoryInfo对象
        ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
        //获得系统可用内存，保存在MemoryInfo对象上
        mActivityManager.getMemoryInfo(memoryInfo);
        return memoryInfo.availMem;
    }


    /**
     * @describe 获取系统总ram
     */
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public static long getTotalRam(Context context) {
        //获得ActivityManager服务的对象
        ActivityManager mActivityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        //获得MemoryInfo对象
        ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
        //获得系统可用内存，保存在MemoryInfo对象上
        mActivityManager.getMemoryInfo(memoryInfo);
        return memoryInfo.totalMem;
    }

    /**
     * @Description: 获取系统总rom(单位MB)
     */
    public static long getTotalRom() {
        Long TotalRom = Environment.getDataDirectory().getTotalSpace();
        return TotalRom / 1048576;
    }

    /**
     * @Description:获取系统已使用的rom(单位MB)
     */
    public static long getUsedRom() {
        Long UsedRom = getTotalRom() - Environment.getDataDirectory().getFreeSpace() / 1048576;
        return UsedRom;
    }

    /**
     * @Description:判断sd卡是否存在
     */
    @SuppressLint("NewApi")
    public static boolean isSDCardEnable(Context context) {
        boolean removable = false;
        StorageManager storageManager = (StorageManager) context.getSystemService(Context.STORAGE_SERVICE);
        //Object invokeVolumeList = storageManager.getVolumeList();
        try {
            Method invokeVolumeList = storageManager.getClass().getMethod("getVolumeList");
            Object result = invokeVolumeList.invoke(storageManager);
            int length = Array.getLength(result);
            for (int i = 0; i < length; i++) {
                StorageVolume storageVolumeElement = (StorageVolume) Array.get(invokeVolumeList, i);
                removable = storageVolumeElement.isRemovable();
                if (removable) {
                    break;
                }
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
        return removable;
    }

    private static String getSDPatch(Context context, boolean sdcard) {
        long time0 = SystemClock.elapsedRealtime();
        StorageManager mStorageManager = (StorageManager) context.getSystemService(Context.STORAGE_SERVICE);
        Class<?> storageVolumeClazz = null;
        String finalPath = Environment.getExternalStorageDirectory().getPath();
        try {
            storageVolumeClazz = Class.forName("android.os.storage.StorageVolume");
            Method getVolumeList = mStorageManager.getClass().getMethod("getVolumeList");
            Method getPath = storageVolumeClazz.getMethod("getPath");
            Method isRemovable = storageVolumeClazz.getMethod("isRemovable");
            Object result = getVolumeList.invoke(mStorageManager);
            final int length = Array.getLength(result);
            for (int i = 0; i < length; i++) {
                Object storageVolumeElement = Array.get(result, i);
                String path = (String) getPath.invoke(storageVolumeElement);
                boolean removable = (Boolean) isRemovable.invoke(storageVolumeElement);
                if (sdcard == removable) {
                    finalPath = path;
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.d(TAG, "getStoragePath:" + finalPath + ", time cost :" + (SystemClock.elapsedRealtime() - time0));
        return finalPath;
    }

    /**
     * @Description:获取外部SD卡的总内存 M
     */
    public static long getStorageTotalSpace(String storagePath) {
        File file = new File(storagePath);

        if (false) {
            StatFs stat = new StatFs(file.getPath());
            long blockSize = stat.getBlockSize();
            long totalBlocks = stat.getBlockCount();
            return blockSize * totalBlocks / 1048576;
        }

        if (file.exists()) {
            return file.getTotalSpace() / 1048576;
        } else {
            return -1;
        }

    }

    /**
     * @Description:获取外部SD开已使用内存 M
     */
    public static long getSDUsedSpace(String storagePath) {
        File file = new File(storagePath);

        if (false) {
            //other method
            StatFs stat = new StatFs(file.getPath());
            long blockSize = stat.getBlockSize();
            long availableBlocks = stat.getAvailableBlocks();
            long usedBlocks = stat.getBlockCount() - availableBlocks;
            return blockSize * usedBlocks / 1048576;
        }

        if (file.exists()) {
            return file.getUsableSpace() / 1048576;
        } else {
            return -1;
        }

    }

    /**
     * @describe 获取总Rom 内置 + 外置 MB
     */
    public static long getTotalFlashSpace(Context context) {
        long rom = getTotalRom();
        String sdPath = getSDPatch(context,true);
        long sdRom = getStorageTotalSpace(sdPath);
        if (sdRom == -1) {
            return rom;
        } else {
            return rom + sdRom;
        }
    }

    /**
     * @describe 获取已用rom 内置 + 外置 M
     */
    public static long getTotalUsedFlashSpace(Context context) {
        long usedRom = getUsedRom();
        String sdPath = getSDPatch(context,true);
        long usedSdSpace = getSDUsedSpace(sdPath);
        if (usedSdSpace == -1) {
            return usedRom;
        } else {
            return usedSdSpace + usedRom;
        }
    }
}
