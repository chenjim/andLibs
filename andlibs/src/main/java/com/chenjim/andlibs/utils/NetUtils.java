package com.chenjim.andlibs.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Build;
import android.telephony.TelephonyManager;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresPermission;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URL;
import java.util.Enumeration;

import static android.Manifest.permission.ACCESS_NETWORK_STATE;

/**
 * @description：get from https://www.jianshu.com/p/83725a3125f2
 * @fileName: NetUtils
 * @author: jim.chen
 * @date: 2020/1/19
 */
public class NetUtils {
    public static int NET_CNNT_BAIDU_OK = 1; // NetworkAvailable
    public static int NET_CNNT_BAIDU_TIMEOUT = 2; // no NetworkAvailable
    public static int NET_NOT_PREPARE = 3; // Net no ready
    public static int NET_ERROR = 4; //net error
    private static int TIMEOUT = 3000; // TIMEOUT

    /**
     * 网络是否已连接
     *
     * @return true:已连接 false:未连接
     */
    @SuppressWarnings("deprecation")
    @RequiresPermission(ACCESS_NETWORK_STATE)
    public static boolean iConnected(@NonNull Context context) {
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (manager != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                NetworkCapabilities networkCapabilities = manager.getNetworkCapabilities(manager.getActiveNetwork());
                if (networkCapabilities != null) {
                    return networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
                            || networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)
                            || networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET);
                }
            } else {
                NetworkInfo networkInfo = manager.getActiveNetworkInfo();
                return networkInfo != null && networkInfo.isConnected();
            }
        }
        return false;
    }

    /**
     * Wifi是否已连接
     *
     * @return true:已连接 false:未连接
     */
    @SuppressWarnings("deprecation")
    public static boolean isWifiConnected(@NonNull Context context) {
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (manager != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                NetworkCapabilities networkCapabilities = manager.getNetworkCapabilities(manager.getActiveNetwork());
                if (networkCapabilities != null) {
                    return networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI);
                }
            } else {
                NetworkInfo networkInfo = manager.getActiveNetworkInfo();
                return networkInfo != null && networkInfo.isConnected() && networkInfo.getType() == ConnectivityManager.TYPE_WIFI;
            }
        }
        return false;
    }

    /**
     * 是否为流量
     */
    @SuppressWarnings("deprecation")
    public static boolean isMobileData(@NonNull Context context) {
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (manager != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                NetworkCapabilities networkCapabilities = manager.getNetworkCapabilities(manager.getActiveNetwork());
                if (networkCapabilities != null) {
                    return networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR);
                }
            } else {
                NetworkInfo networkInfo = manager.getActiveNetworkInfo();
                return networkInfo != null && networkInfo.isConnected() && networkInfo.getType() == ConnectivityManager.TYPE_MOBILE;
            }
        }
        return false;
    }

    /**
     * getLocalIpAddress
     *
     * @return
     */
    public static String getLocalIpAddress() {
        String ret = "";
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        ret = inetAddress.getHostAddress().toString();
                    }
                }
            }
        } catch (SocketException ex) {
            ex.printStackTrace();
        }
        return ret;
    }

    /**
     * 返回当前网络状态
     *
     * @param context
     * @return
     */
    public static int getNetState(Context context) {
        try {
            ConnectivityManager connectivity = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connectivity != null) {
                NetworkInfo networkinfo = connectivity.getActiveNetworkInfo();
                if (networkinfo != null) {
                    if (networkinfo.isAvailable() && networkinfo.isConnected()) {
                        if (!connectionNetwork())
                            return NET_CNNT_BAIDU_TIMEOUT;
                        else
                            return NET_CNNT_BAIDU_OK;
                    } else {
                        return NET_NOT_PREPARE;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return NET_ERROR;
    }

    /**
     * ping "http://www.baidu.com"
     *
     * @return
     */
    static private boolean connectionNetwork() {
        boolean result = false;
        HttpURLConnection httpUrl = null;
        try {
            httpUrl = (HttpURLConnection) new URL("http://www.baidu.com").openConnection();
            httpUrl.setConnectTimeout(TIMEOUT);
            httpUrl.connect();
            result = true;
        } catch (IOException e) {
        } finally {
            if (null != httpUrl) {
                httpUrl.disconnect();
            }
            httpUrl = null;
        }
        return result;
    }

    /**
     * check is3G
     *
     * @param context
     * @return boolean
     */
    public static boolean is3G(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
        if (activeNetInfo != null
                && activeNetInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
            return true;
        }
        return false;
    }

    /**
     * isWifi
     *
     * @param context
     * @return boolean
     */
    public static boolean isWifi(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
        if (activeNetInfo != null
                && activeNetInfo.getType() == ConnectivityManager.TYPE_WIFI) {
            return true;
        }
        return false;
    }

    /**
     * is2G
     *
     * @param context
     * @return boolean
     */
    public static boolean is2G(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
        if (activeNetInfo != null
                && (activeNetInfo.getSubtype() == TelephonyManager.NETWORK_TYPE_EDGE
                || activeNetInfo.getSubtype() == TelephonyManager.NETWORK_TYPE_GPRS || activeNetInfo
                .getSubtype() == TelephonyManager.NETWORK_TYPE_CDMA)) {
            return true;
        }
        return false;
    }

    /**
     * is wifi on
     */
    public static boolean isWifiEnabled(Context context) {
        ConnectivityManager mgrConn = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        TelephonyManager mgrTel = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        return ((mgrConn.getActiveNetworkInfo() != null && mgrConn
                .getActiveNetworkInfo().getState() == NetworkInfo.State.CONNECTED) || mgrTel
                .getNetworkType() == TelephonyManager.NETWORK_TYPE_UMTS);
    }
}
