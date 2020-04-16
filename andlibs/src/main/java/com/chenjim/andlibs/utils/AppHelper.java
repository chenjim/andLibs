package com.chenjim.andlibs.utils;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;

import androidx.annotation.RequiresApi;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import static android.provider.Settings.System.SCREEN_BRIGHTNESS_MODE;
import static android.provider.Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC;
import static android.provider.Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL;
import static android.provider.Settings.System.SCREEN_OFF_TIMEOUT;

/**
 * @description：
 * @fileName: AppHelper
 * @author: jim.chen
 * @date: 2020/1/19
 */
public class AppHelper {

    private static final String TAG = "AppHelper";

    /**
     * 屏幕超时时间
     *
     * @param context
     * @return
     */
    public static int getScreenTimeOut(Context context) {
        int currentValue = Settings.System.getInt(context.getContentResolver()
                , SCREEN_OFF_TIMEOUT, 30000);
        return currentValue;
    }

    public static boolean isTopApp(Context context, String packageName) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> tasks = am.getRunningTasks(1);
        if (!tasks.isEmpty()) {
            ComponentName componentName = tasks.get(0).topActivity;
            if (packageName.equals(componentName.getPackageName())) {
                return true;
            }
        }
        return false;
    }

    public static boolean isAppInstalled(Context context, String packageName) {
        if (packageName == null) {
            return false;
        }
        List<PackageInfo> packages = context.getPackageManager().getInstalledPackages(0);
        for (PackageInfo packageInfo : packages) {
            if (packageName.equals(packageInfo.packageName)) {
                return true;
            }
        }
        return false;
    }

    /**
     * @describe 根据包名获取版本号
     */
    public static String getAppVersionByPackage(Context context, String packageName) {
        if (isAppInstalled(context, packageName)) {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = null;
            try {
                packageInfo = packageManager.getPackageInfo(packageName, 0);
            } catch (PackageManager.NameNotFoundException e) {
                Log.d(TAG,e.getMessage());
                return "";
            }
            String version = packageInfo.versionName;
            return version;
        } else {
            return "";
        }
    }

    public static boolean isServiceWorked(Context context, String serviceName) {
        ActivityManager myManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        ArrayList<ActivityManager.RunningServiceInfo> runningService =
                (ArrayList<ActivityManager.RunningServiceInfo>) myManager.getRunningServices(Integer.MAX_VALUE);
        for (int i = 0; i < runningService.size(); i++) {
            if (runningService.get(i).service.getClassName().toString().equals(serviceName)) {
                return true;
            }
        }
        return false;
    }

    /**
     * convert dp to its equivalent px
     * <p>
     * 将dp转换为与之相等的px
     */
    public static int dp2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    /**
     * convert px to its equivalent dp
     * <p>
     * 将px转换为与之相等的dp
     */
    public static int px2dp(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }



    /**
     * @describe 获取当前本地apk的版本(对应AndroidManifest.xml下android : versionCode)
     */
    public static int getVersionCode(Context context) {
        int versionCode = 0;
        try {
            versionCode = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionCode;
    }

    /**
     * @describe 获取版本号名称(对应AndroidManifest.xml下android : versionName)
     */
    public static String getVersionName(Context context) {
        String versionName = "";
        try {
            versionName = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionName;
    }

    /**
     * @describe hook住webView
     */
    public static void hookWebView() {
        int sdkInt = Build.VERSION.SDK_INT;
        try {
            Class<?> factoryClass = Class.forName("android.webkit.WebViewFactory");
            Field field = factoryClass.getDeclaredField("sProviderInstance");
            field.setAccessible(true);
            Object sProviderInstance = field.get(null);
            if (sProviderInstance != null) {
                Log.e(TAG, "sProviderInstance isn't null");
                return;
            }
            Method getProviderClassMethod;
            if (sdkInt > 22) {
                getProviderClassMethod = factoryClass.getDeclaredMethod("getProviderClass");
            } else if (sdkInt == 22) {
                getProviderClassMethod = factoryClass.getDeclaredMethod("getFactoryClass");
            } else {
                Log.e(TAG, "Don't need to Hook WebView");
                return;
            }
            getProviderClassMethod.setAccessible(true);
            Class<?> providerClass = (Class<?>) getProviderClassMethod.invoke(factoryClass);
            Class<?> delegateClass = Class.forName("android.webkit.WebViewDelegate");
            Constructor<?> providerConstructor = providerClass.getConstructor(delegateClass);
            if (providerConstructor != null) {
                providerConstructor.setAccessible(true);
                Constructor<?> declaredConstructor = delegateClass.getDeclaredConstructor();
                declaredConstructor.setAccessible(true);
                sProviderInstance = providerConstructor.newInstance(declaredConstructor.newInstance());
                field.set("sProviderInstance", sProviderInstance);
            }
            Log.d(TAG, "web view Hook done!");
        } catch (Throwable e) {
            Log.e(TAG, e.getMessage());
        }
    }

    /**
     * @Description: 设置屏幕亮度值
     */
    public static void setScreenBrightness(Context context, int paramInt) {
        try {
            setScreenBrightness(paramInt);
            Settings.System.putInt(context.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS, paramInt);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @Description: 获取屏幕亮度值
     */
    public static int getScreenBrightness(Context context) {
        int value = Settings.System.getInt(context.getContentResolver(),
                Settings.System.SCREEN_BRIGHTNESS, 205);
        return value;
    }


    /**
     * @Description: 设置自动亮度
     */
    public static void setScreenAutoBrightness(Context context, boolean auto) {
        Settings.System.putInt(context.getContentResolver(), SCREEN_BRIGHTNESS_MODE,
                auto ? SCREEN_BRIGHTNESS_MODE_AUTOMATIC : SCREEN_BRIGHTNESS_MODE_MANUAL);
    }


    public static Object getIPowerManager() throws Exception {
        //获得ServiceManager类
        Class<?> ServiceManager = Class.forName("android.os.ServiceManager");

        //获得ServiceManager的getService方法
        Method getService = ServiceManager.getMethod("getService", String.class);

        //调用getService获取RemoteService
        Object oRemoteService = getService.invoke(null, Context.POWER_SERVICE);

        //获得IPowerManager.Stub类
        Class<?> cStub = Class.forName("android.os.IPowerManager$Stub");
        //获得asInterface方法
        Method asInterface = cStub.getMethod("asInterface", android.os.IBinder.class);
        //调用asInterface方法获取IPowerManager对象
        Object oIPowerManager = asInterface.invoke(null, oRemoteService);
        return oIPowerManager;
    }

    /**
     * @Description: 灭屏
     */
    public static void setScreenOff(Context context) {
        setScreenAutoBrightness(context, false);

        setScreenBrightness(0);
    }


    /**
     * @Description: 亮屏
     */
    public static void setScreenOn(Context context) {
        int brightnessValues = getScreenBrightness(context);
        setScreenBrightness(brightnessValues);

        setScreenAutoBrightness(context, true);
    }

    /**
     * @param value 设置屏幕亮度的值
     */
    public static void setScreenBrightness(int value) {
        try {
            //调用asInterface方法获取IPowerManager对象
            Object oIPowerManager = getIPowerManager();
            //获得shutdown()方法
            Method shutdown = oIPowerManager.getClass().getMethod("setTemporaryScreenBrightnessSettingOverride", Integer.class);
            //调用shutdown()方法
            shutdown.invoke(oIPowerManager, value);
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
    }


    /**
     * @Description: 音量设置
     */
    public static void setMediaSound(Context context, int paramInt) {
        AudioManager mAudioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, paramInt, 0);
    }

    /**
     * @Description: 获得当前音量
     */
    public static int getMediaSoundValue(Context context) {
        AudioManager mAudioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        int mSystemVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        return mSystemVolume;
    }

}
