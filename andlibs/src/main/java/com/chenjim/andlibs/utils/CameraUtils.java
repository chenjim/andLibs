package com.chenjim.andlibs.utils;

import android.hardware.Camera;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * @description：
 * @fileName: CameraUtils
 * @author: jim.chen
 * @date: 2020/1/19
 */
public class CameraUtils {

    private static final String TAG = "CameraUtils";


    /**
     * 预览支持的分辨率
     * @return
     */
    public static List<Camera.Size> getSupportedPreviewSizes() {
        android.hardware.Camera camera = null;
        try {
            camera = android.hardware.Camera.open();
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
        if (camera == null) {
            return new ArrayList<>();
        }
        android.hardware.Camera.Parameters parameters = camera.getParameters();
        List<Camera.Size> supportedVideoSizes = parameters.getSupportedVideoSizes();
        camera.release();
        return supportedVideoSizes;
    }

}
