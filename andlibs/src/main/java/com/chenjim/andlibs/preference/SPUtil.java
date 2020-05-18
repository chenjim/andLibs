package com.chenjim.andlibs.preference;

import android.app.Application;

/**
 * replace with {@link com.tencent.mmkv.MMKV}  {@link com.chenjim.andlibs.mmkv.DataMap}
 */
@Deprecated
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
