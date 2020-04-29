package com.chenjim.andlibs.utils;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.widget.Toast;

import com.chenjim.andlibs.BaseApplication;


public class ToastUtil {
    private static Toast mToast;
    private static Handler mUiHandler = new Handler(Looper.getMainLooper());

    public static void show(Context context, String msg, int duration) {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            showUi(context, msg, duration);
        } else {
            mUiHandler.post(() -> {
                showUi(context, msg, duration);
            });
        }
    }

    private static void showUi(Context context, String msg, int duration) {
        try {
            if (context == null) {
                context = BaseApplication.sApplication;
            }
            if (!TextUtils.isEmpty(msg)) {
                if (mToast != null) {
                    mToast.cancel();
                }
                mToast = Toast.makeText(context, "", duration);
                mToast.setText(msg);
                mToast.show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void showShort(Context context, String msg) {
        show(context, msg, Toast.LENGTH_SHORT);
    }

    public static void showShort(Context context, int resource) {
        show(context, context.getString(resource), Toast.LENGTH_SHORT);
    }

    public static void showLong(Context context, String msg) {
        show(context, msg, Toast.LENGTH_LONG);
    }

    public static void showLong(Context context, int resource) {
        show(context, context.getString(resource), Toast.LENGTH_LONG);
    }

    public static void showShort(String msg) {
        show(BaseApplication.sApplication, msg, Toast.LENGTH_SHORT);
    }

    public static void showShort(int resource) {
        show(BaseApplication.sApplication, BaseApplication.sApplication.getString(resource), Toast.LENGTH_SHORT);
    }

    public static void showLong(String msg) {
        show(BaseApplication.sApplication, msg, Toast.LENGTH_LONG);
    }

    public static void showLong(int resource) {
        show(BaseApplication.sApplication, BaseApplication.sApplication.getString(resource), Toast.LENGTH_LONG);
    }
}
