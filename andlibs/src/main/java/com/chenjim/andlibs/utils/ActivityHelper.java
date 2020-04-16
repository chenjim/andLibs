package com.chenjim.andlibs.utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.Application;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.RequiresApi;

import java.util.List;

/**
 * Created by chenjim on 2016/9/27.
 */
public class ActivityHelper {

    private static Application application;

    public static void init(Application app) {
        application = app;
    }

    public static void showIme(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.showSoftInput(activity.getCurrentFocus(), InputMethodManager.SHOW_FORCED);
    }

    public static void hideIme(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }


    public static void startActivity(Context context, String pkg, String clz) {
        startActivity(context, pkg, clz, null);
    }

    public static void startActivity(Context context, String pkg, String clz, Bundle bundle) {
        try {
            Intent intent = new Intent();
            intent.setClassName(pkg, clz);
            if (bundle != null) {
                intent.putExtras(bundle);
            }
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            context.startActivity(intent);
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
        }
    }


    public static boolean isTopActivity(String clzName) {
        ActivityManager am = (ActivityManager) application.getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningTaskInfo> tasks = am.getRunningTasks(1);
        if (!tasks.isEmpty()) {
            ComponentName componentName = tasks.get(0).topActivity;
            if (clzName.equals(componentName.getClassName())) {
                return true;
            }
        }
        return false;
    }



    public static String getTopActivity(Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningTaskInfo> tasksInfo = am.getRunningTasks(1);
        if (tasksInfo.size() > 0) {
            return tasksInfo.get(0).topActivity.getClassName();
        }
        return "";
    }
}
