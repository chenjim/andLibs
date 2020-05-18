package com.chenjim.andlibs.preference;

/**
 * replace with {@link com.tencent.mmkv.MMKV}  {@link com.chenjim.andlibs.mmkv.DataMap}
 */
@Deprecated
public class SPNetDataUtil extends BasePreferences {
    private static final String BASIC_DATA_PREFERENCE_FILE_NAME = "network_api_module_basic_data_preference";
    private static SPNetDataUtil sInstance;

    public static SPNetDataUtil getInstance() {
        if (sInstance == null) {
            synchronized (SPNetDataUtil.class) {
                if (sInstance == null) {
                    sInstance = new SPNetDataUtil();
                }
            }
        }
        return sInstance;
    }

    @Override
    protected String getSPFileName() {
        return BASIC_DATA_PREFERENCE_FILE_NAME;
    }
}
