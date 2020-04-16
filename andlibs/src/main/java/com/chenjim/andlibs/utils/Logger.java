package com.chenjim.andlibs.utils;


import android.app.Application;
import android.text.TextUtils;
import android.util.Log;

import java.io.File;
import java.io.RandomAccessFile;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by chenjim on 2016/9/26.
 * 可以打印当前的线程名，所在代码中的位置等信息
 * modify from {@link "https://github.com/orhanobut/logger"}
 */
public class Logger {
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
    private static String TAG = null;

    /**
     * true 输出日志
     * false 不输出日志
     */
    private static boolean isDebug = true;

    /**
     * 不对Loger进一步封装，为4
     * 若进一步封装，此值需要改变
     */
    private static int stackIndex = 4;

    /**
     * 单个文件限制大小
     */
    private static final long MAX_FILE_LENGTH = 1 * 1024 * 1024;

    private static boolean isToFile = true;

    private static Application application;

    private static SimpleDateFormat timeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");

    private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    private static String curFileName = "log1.txt";
    private static String lastFileName = "log2.txt";

    private static LinkedBlockingQueue<String> cacheList = new LinkedBlockingQueue<>();
    private static RandomAccessFile mLogRandomAccessFile;
    private static LogWriteThread logWriteThread;

    public static void init(Application app, boolean debug, boolean write) {
        application = app;
        isDebug = debug;
        isToFile = write;
        logWriteThread = new LogWriteThread();
        logWriteThread.setName("Logger");
        logWriteThread.start();
    }

    public static void d() {
        log(Log.DEBUG, TAG, " ");
    }

    public static void d(Object object) {
        String message;
        if (object == null) {
            message = "null";
        } else if (object.getClass().isArray()) {
            message = Arrays.deepToString((Object[]) object);
        } else {
            message = object.toString();
        }
        log(Log.DEBUG, TAG, message);
    }

    public static void d(String tag, String message) {
        log(Log.DEBUG, tag, message);
    }


    public static void e(Object object) {
        String message;
        if (object == null) {
            message = "null";
        } else if (object.getClass().isArray()) {
            message = Arrays.deepToString((Object[]) object);
        } else {
            message = object.toString();
        }
        log(Log.ERROR, TAG, message);
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

        if (e != null && message != null) {
            message += " : " + e.toString();
        }
        if (e != null && message == null) {
            message = e.toString();
        }
        if (message == null) {
            message = "No message/exception is set";
        }
        log(Log.ERROR, tag, message);
    }

    public static void w(Object object) {
        String message;
        if (object == null) {
            message = "null";
        } else if (object.getClass().isArray()) {
            message = Arrays.deepToString((Object[]) object);
        } else {
            message = object.toString();
        }
        log(Log.WARN, TAG, message);
    }

    public static void w(String tag, String message) {
        log(Log.WARN, tag, message);
    }

    public static void i() {
        log(Log.INFO, TAG, " ");
    }

    public static void i(Object object) {
        String message;
        if (object == null) {
            message = "null";
        } else if (object.getClass().isArray()) {
            message = Arrays.deepToString((Object[]) object);
        } else {
            message = object.toString();
        }
        log(Log.INFO, TAG, message);
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
        if (!isDebug) {
            return;
        }

        StackTraceElement[] trace = Thread.currentThread().getStackTrace();

        StackTraceElement element = trace[stackIndex];

        if (TextUtils.isEmpty(tag)) {
            tag = element.getFileName();
        }

        StringBuilder builder = new StringBuilder();
        builder.append(Thread.currentThread().getName())
                .append(",")
                .append("(")
                .append(element.getFileName())
                .append(":")
                .append(element.getLineNumber())
                .append("),")
                .append(element.getMethodName())
                .append("():")
        ;

        if (message == null) {
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


    private static void logChunk(int logType, String tag, String chunk) {
        switch (logType) {
            case Log.ERROR:
                Log.e(tag, chunk);
                break;
            case Log.INFO:
                Log.i(tag, chunk);
                break;
            case Log.VERBOSE:
                Log.v(tag, chunk);
                break;
            case Log.WARN:
                Log.w(tag, chunk);
                break;
            case Log.DEBUG:
                // Fall through, log debug by default
            default:
                Log.d(tag, chunk);
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
            case Log.INFO:
                type = "I";
                break;
            case Log.VERBOSE:
                type = "V";
                break;
            case Log.WARN:
                type = "W";
                break;
            default:
                type = "D";
                break;
        }
        return type;
    }


    private synchronized static void writeLogToFile(int logType, String tag, String msg) {
        if (!isToFile) {
            return;
        }
        if (application == null) {
            throw new RuntimeException("Loger NOT init");
        }
        String data = new StringBuilder().append("[pid:").append(android.os.Process.myPid()).append("][")
                .append(timeFormat.format(new Date())).append(": ")
                .append(getTypeString(logType)).append("/")
                .append(tag).append("]")
                .append(msg).toString();
        cacheList.offer(data);
        Logger.class.notifyAll();
    }

    public static File getDirFile() {
        String basePath = application.getFilesDir().getAbsolutePath();
        File file = new File(basePath + File.separator + "log");
        if (!file.exists()) {
            file.mkdirs();
        }
        return file;
    }

    private static RandomAccessFile getRandomAccessFile() {
        if (mLogRandomAccessFile == null) {
            File file = new File(getDirFile(), curFileName);
            try {
                if (!file.exists()) {
                    file.createNewFile();
                }
                mLogRandomAccessFile = new RandomAccessFile(file, "rw");
                mLogRandomAccessFile.seek(mLogRandomAccessFile.length());
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
        return mLogRandomAccessFile;
    }

    private static void doWriteDisk(String msg) {
        try {
            byte[] msgByte = msg.getBytes("UTF-8");
            if (mLogRandomAccessFile != null
                    && mLogRandomAccessFile.length() + msgByte.length > MAX_FILE_LENGTH) {
                File file1 = new File(getDirFile(), curFileName);
                File file2 = new File(getDirFile(), curFileName);
                file1.renameTo(file2);
                //重命名后，重新创建文件
                mLogRandomAccessFile = null;
            }

            if (getRandomAccessFile() == null) {
                return;
            }

            mLogRandomAccessFile.write(msgByte);
            mLogRandomAccessFile.writeBytes("\r\n");
        } catch (Exception e) {
            e.printStackTrace();
            mLogRandomAccessFile = null;
        }
    }

    /**
     * 读取最后一段logger
     */
    public static String getLastLogger(long maxSize) {
        StringBuffer data = new StringBuffer();
        RandomAccessFile curAccessFile = null, lastAccessFile = null;
        try {
            File curLogFile = new File(getDirFile() + File.separator + curFileName);
            if (!curLogFile.exists()) {
                return data.toString();
            }
            curAccessFile = new RandomAccessFile(curLogFile, "rw");
            //是否需要读取上一个文件
            if (curAccessFile.length() < maxSize) {
                File lastLogFile = new File(getDirFile() + File.separator + lastFileName);
                if (lastLogFile.exists()) {
                    long lastFileReadSize = maxSize - curAccessFile.length();
                    lastAccessFile = new RandomAccessFile(lastLogFile, "rw");
                    lastAccessFile.seek(Math.max(0, lastAccessFile.length() - lastFileReadSize));
                    while (lastAccessFile.getFilePointer() < lastAccessFile.length()) {
                        data.append(lastAccessFile.readLine()).append("\r\n");
                    }
                }
            }

            curAccessFile.seek(Math.max(0, curAccessFile.length() - maxSize));
            while (curAccessFile.getFilePointer() < curAccessFile.length()) {
                data.append(curAccessFile.readLine()).append("\r\n");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (curAccessFile != null) {
                    curAccessFile.close();
                }
                if (lastAccessFile != null) {
                    lastAccessFile.close();
                }
            } catch (Exception e) {

            }
        }
        return data.toString();
    }

    private static class LogWriteThread extends Thread {
        @Override
        public void run() {

            synchronized (Logger.class) {
                while (true) {
                    try {
                        if (!cacheList.isEmpty()) {
                            doWriteDisk(cacheList.take());
                        } else {
                            Logger.class.wait();
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        break;
                    }
                }
            }

        }
    }

}
