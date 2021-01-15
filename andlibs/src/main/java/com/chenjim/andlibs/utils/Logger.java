package com.chenjim.andlibs.utils;


import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.text.TextUtils;
import android.util.Log;

import org.jetbrains.annotations.Nullable;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * @author chenjim me@h89.cn
 * @description 可以打印当前的线程名，所在代码中的位置等信息
 * modify from {@link "https://github.com/orhanobut/logger"}
 * @date 2016/9/26.
 */
public class Logger {
    private Logger() {
    }

    /**
     * Android's max limit for a log entry is ~4076 bytes,
     * so 4000 bytes is used as chunk size since default charset
     * is UTF-8
     */
    private static final int CHUNK_SIZE = 4000;

    /**
     * TAG is used for the Log, the name is a little different
     * in order to differentiate the logs easily with the filter
     */
    private static final String TAG = null;


    /**
     * 日志输出级别
     * {@link Log#ASSERT}>{@link Log#ERROR}>{@link Log#WARN}
     * >{@link Log#INFO}>{@link Log#DEBUG}>{@link Log#VERBOSE}
     */
    private static int logLevel = Log.VERBOSE;

    /**
     * 不进一步封装，为4
     * 若进一步封装，此值需要改变
     */
    private static final int STACK_INDEX = 4;

    /**
     * 单个文件限制大小
     */
    private static long logFileMaxLen = 5 * 1024 * 1024L;

    private static File slogPath = null;

    private static final String CUR_LOG_NAME = "log1.txt";
    private static final String LAST_LOG_NAME = "log2.txt";

    private static Handler logHandler;

    static {
        HandlerThread handlerThread = new HandlerThread("Logger");
        handlerThread.start();
        logHandler = new Handler(handlerThread.getLooper());
    }

    /**
     * 初始化，不是必须
     * 当需要写入到日志文件时，必需
     * 日志文件位置'/sdcard/Android/data/com.xxx.xxx/files/log/'
     *
     * @param writeFileContext 空，不写入日志
     *                         非空，写入日志，用来获取应用的数据存储目录，
     *                         不需要权限{@link Context#getExternalFilesDir(String)}
     * @param level            默认值 {@link #logLevel}
     */
    @TargetApi(Build.VERSION_CODES.FROYO)
    public static void init(@Nullable Context writeFileContext, int level) {
        if (writeFileContext != null) {
            File path = new File(writeFileContext.getExternalFilesDir(null), "log");
            if (!path.exists()) {
                path.mkdirs();
            }
            slogPath = path;
            d("write log to dir:" + slogPath.getPath());
        }

        logLevel = level;
    }

    /**
     * 配置日志输出级别，默认所有{@link #logLevel}
     *
     * @param level
     */
    public static void init(int level) {
        init(null, level);
    }

    public static void setLogFileMaxLen(long len) {
        logFileMaxLen = len;
    }

    public static void d() {
        log(Log.DEBUG, TAG, " ");
    }

    private static String objectToString(Object object) {
        String message;
        if (object == null) {
            message = "null";
        } else if (object.getClass().isArray()) {
            message = Arrays.deepToString((Object[]) object);
        } else if (object instanceof List) {
            Object[] objects = ((List<?>) object).toArray();
            message = Arrays.deepToString(objects);
        } else {
            message = object.toString();
        }
        return message;

    }

    public static void d(Object object) {
        log(Log.DEBUG, TAG, objectToString(object));
    }

    public static void d(String tag, String message) {
        log(Log.DEBUG, tag, message);
    }


    public static void e(Object object) {
        log(Log.ERROR, TAG, objectToString(object));
    }

    public static void e(String tag, String message) {
        log(Log.ERROR, tag, message);
    }

    public static void e(String tag, Exception e) {
        String message = null;
        if (e != null) {
            message = e.toString();
        }
        if (message == null) {
            message = "No message/exception is set";
        }
        log(Log.ERROR, tag, message);
    }

    public static void e(String tag, String message, Exception e) {
        message += ":" + e.toString();
        log(Log.ERROR, tag, message);
    }

    public static void w(Object object) {
        log(Log.WARN, TAG, objectToString(object));
    }

    public static void w(String tag, String message) {
        log(Log.WARN, tag, message);
    }

    public static void i() {
        log(Log.INFO, TAG, " ");
    }

    public static void i(Object object) {
        log(Log.INFO, TAG, objectToString(object));
    }

    public static void i(String tag, String message) {
        log(Log.INFO, tag, message);
    }


    public static void v() {
        log(Log.VERBOSE, TAG, " ");
    }

    public static void v(String message) {
        log(Log.VERBOSE, TAG, message);
    }


    public static void v(String tag, String message) {
        log(Log.VERBOSE, tag, message);
    }

    private static void log(int logType, String tag, String message) {
        if (logType < logLevel) {
            return;
        }

        Thread curThread = Thread.currentThread();
        StackTraceElement element = curThread.getStackTrace()[STACK_INDEX];
        String threadName = curThread.getName();

        logHandler.post(() -> {
            log(element, threadName, logType, tag, message);
        });
    }

    private static void log(StackTraceElement element, String threadName,
                            int logType, String tag, String message) {
        if (TextUtils.isEmpty(tag)) {
            tag = element.getFileName();
        }

        StringBuilder builder = new StringBuilder();
        builder.append(threadName)
                .append(",")
                .append("(")
                .append(element.getFileName())
                .append(":")
                .append(element.getLineNumber())
                .append("),")
                .append(element.getMethodName())
                .append("():")
        ;

        if (TextUtils.isEmpty(message)) {
            message = "null";
        }

        //get bytes of message with system's default charset (which is UTF-8 for Android)
        byte[] bytes = message.getBytes();
        int length = bytes.length;

        for (int i = 0; i < length; i += CHUNK_SIZE) {
            int count = Math.min(length - i, CHUNK_SIZE);
            //create a new String with system's default charset (which is UTF-8 for Android)
            String content = new String(bytes, i, count);
            String finalContent = builder.toString() + content;
            logChunk(logType, tag, finalContent);
        }
    }


    /**
     * @return 调用处的文件名+行数+函数名
     */
    public static String getStackTrace() {
        StackTraceElement[] trace = Thread.currentThread().getStackTrace();
        StackTraceElement element = trace[3];
        StringBuilder builder = new StringBuilder();
        builder.append("(")
                .append(element.getFileName())
                .append(":")
                .append(element.getLineNumber())
                .append("),")
                .append(element.getMethodName())
                .append("()")
        ;
        return builder.toString();
    }

    private static void logChunk(int logType, String tag, String chunk) {
        switch (logType) {
            case Log.ERROR:
                Log.e(tag, chunk);
                break;
            case Log.WARN:
                Log.w(tag, chunk);
                break;
            case Log.INFO:
                Log.i(tag, chunk);
                break;
            case Log.DEBUG:
                Log.d(tag, chunk);
                break;
            case Log.VERBOSE:
                Log.v(tag, chunk);
                break;
            default:
                break;
        }
        writeLogToFile(logType, tag, chunk);
    }

    private static String getTypeString(int logType) {
        String type = "D";
        switch (logType) {
            case Log.ERROR:
                type = "E";
                break;
            case Log.WARN:
                type = "W";
                break;
            case Log.INFO:
                type = "I";
                break;
            case Log.DEBUG:
                type = "D";
                break;
            case Log.VERBOSE:
                type = "V";
                break;
            default:
                break;
        }
        return type;
    }


    private static synchronized void writeLogToFile(int logType, String tag, String msg) {
        if (slogPath == null) {
            return;
        }
        SimpleDateFormat timeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");

        String data = new StringBuilder()
                .append("[pid:").append(android.os.Process.myPid())
                .append("][").append(timeFormat.format(new Date())).append(": ")
                .append(getTypeString(logType)).append("/").append(tag).append("]")
                .append(msg).append("\r\n")
                .toString();
        logHandler.post(() -> doWriteDisk(data));
    }

    public static File getLogDir() {
        return slogPath;
    }

    private static void doWriteDisk(String msg) {
        File curFile = new File(getLogDir(), CUR_LOG_NAME);
        File oldFile = new File(getLogDir(), LAST_LOG_NAME);
        if (curFile.length() > logFileMaxLen && !curFile.renameTo(oldFile)) {
            return;
        }

        try (FileWriter writer = new FileWriter(curFile, true)) {
            writer.write(msg);
            writer.flush();
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    private static StringBuilder readLogFile(File file, long readLen) {
        StringBuilder data = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            long skip = file.length() > readLen ? file.length() - readLen : 0;
            reader.skip(skip);
            long length = readLen > file.length() ? file.length() : readLen;
            while (data.length() < length) {
                data.append(reader.readLine()).append("\r\n");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }

    /**
     * 读取最后一段logger
     */
    public static String getLastLogger(long maxSize) {
        StringBuilder data = new StringBuilder();
        File curLogFile = new File(getLogDir(), CUR_LOG_NAME);
        if (!curLogFile.exists()) {
            return data.toString();
        }

        //是否需要读取上一个文件
        if (curLogFile.length() < maxSize) {
            File lastLogFile = new File(getLogDir(), LAST_LOG_NAME);
            StringBuilder lastLogData = readLogFile(lastLogFile, maxSize - curLogFile.length());
            data.append(lastLogData);
        }

        StringBuilder curLogData = readLogFile(curLogFile, maxSize);
        data.append(curLogData);
        return data.toString();
    }

}
