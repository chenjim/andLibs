package com.chenjim.andlibs.utils;

import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;


public class ToastUtil {
    private static Toast mToast;

    public static void show(Context context, String msg) {
        try {
            if (context != null && !TextUtils.isEmpty(msg)) {
                if (mToast != null) {
                    mToast.cancel();
                }
                mToast = Toast.makeText(context, "", Toast.LENGTH_LONG);
                mToast.setText(msg);
                mToast.show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
