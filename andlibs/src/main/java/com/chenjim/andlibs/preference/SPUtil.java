package com.chenjim.andlibs.preference;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;


public class SPUtil extends BasePreferences {

    private static SPUtil sInstance;

    public static SPUtil get() {
        if (sInstance == null) {
            synchronized (SPUtil.class) {
                if (sInstance == null) {
                    sInstance = new SPUtil();
                }
            }
        }
        return sInstance;
    }

    public static void init(Application application) {
        sApplication = application;
    }


    @Override
    public String getSPFileName() {
        return "common_data";
    }


}
