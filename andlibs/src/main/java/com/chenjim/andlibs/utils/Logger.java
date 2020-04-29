package com.chenjim.andlibs.utils;


import android.app.Application;
import android.text.TextUtils;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

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

    public static File getLogDir() {
        String basePath = application.getFilesDir().getAbsolutePath();
        File file = new File(basePath + File.separator + "log");
        if (!file.exists()) {
            file.mkdirs();
        }
        return file;
    }

    private static RandomAccessFile getRandomAccessFile() {
        if (mLogRandomAccessFile == null) {
            File file = new File(getLogDir(), curFileName);
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
                File file1 = new File(getLogDir(), curFileName);
                File file2 = new File(getLogDir(), curFileName);
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
     * 压缩文件
     *
     * @param resFile  需要压缩的文件（夹）
     * @param zipOut   压缩的目的文件
     * @param rootPath 压缩的文件路径
     * @throws FileNotFoundException 找不到文件时抛出
     * @throws IOException           当压缩过程出错时抛出
     */
    private static void zipFile(File resFile, ZipOutputStream zipOut, String rootPath)
            throws FileNotFoundException, IOException {
        rootPath = rootPath + (rootPath.trim().length() == 0 ? "" : File.separator)
                + resFile.getName();
        rootPath = new String(rootPath.getBytes("8859_1"), "GB2312");
        if (resFile.isDirectory()) {
            File[] fileList = resFile.listFiles();
            for (File file : fileList) {
                zipFile(file, zipOut, rootPath);
            }
        } else {
            byte buffer[] = new byte[1024];
            BufferedInputStream in = new BufferedInputStream(new FileInputStream(resFile), 1024);
            zipOut.putNextEntry(new ZipEntry(rootPath));
            int realLength;
            while ((realLength = in.read(buffer)) != -1) {
                zipOut.write(buffer, 0, realLength);
            }
            in.close();
            zipOut.flush();
            zipOut.closeEntry();
        }
    }

    /**
     * 批量压缩文件（夹）
     *
     * @param resFileList 要压缩的文件（夹）列表
     * @param zipFile     生成的压缩文件
     * @param comment     压缩文件的注释
     * @throws IOException 当压缩过程出错时抛出
     */
    public static void zipFiles(Collection<File> resFileList, File zipFile, String comment)
            throws IOException {
        ZipOutputStream zipOut = new ZipOutputStream(
                new BufferedOutputStream(new FileOutputStream(zipFile), 1024));
        for (File resFile : resFileList) {
            zipFile(resFile, zipOut, "");
        }
        zipOut.setComment(comment);
        zipOut.close();
    }

    public static File zipLoggerFiles() {
        try {
            File logDir = getLogDir();
            if (logDir != null && logDir.exists() && logDir.isDirectory()) {
                File zipFile = new File(application.getFilesDir().getAbsolutePath() + File.separator + "log.zip");
                if (zipFile.exists()) {
                    zipFile.delete();
                }
                ArrayList<File> files = new ArrayList<>();

                File firstLogFile = new File(logDir.getAbsolutePath() + File.separator + curFileName);
                if (firstLogFile.exists()) {
                    files.add(firstLogFile);
                }

                File secondLogFile = new File(logDir.getAbsolutePath() + File.separator + lastFileName);
                if (secondLogFile.exists()) {
                    files.add(secondLogFile);
                }

                String spFilePath = application.getFilesDir().getParent() + "/shared_prefs/common_data.xml";
                File spFile = new File(spFilePath);
                if (spFile != null && spFile.exists()) {
                    files.add(spFile);
//                    d("add spFile file to zipfile list");
                }

                zipFiles(files, zipFile, "");
                return zipFile;
            }
        } catch (Exception e) {
            Log.d("zipFile error:", e.getMessage());
        }
        return null;
    }

    /**
     * 读取最后一段logger
     */
    public static String getLastLogger(long maxSize) {
        StringBuffer data = new StringBuffer();
        RandomAccessFile curAccessFile = null, lastAccessFile = null;
        try {
            File curLogFile = new File(getLogDir() + File.separator + curFileName);
            if (!curLogFile.exists()) {
                return data.toString();
            }
            curAccessFile = new RandomAccessFile(curLogFile, "rw");
            //是否需要读取上一个文件
            if (curAccessFile.length() < maxSize) {
                File lastLogFile = new File(getLogDir() + File.separator + lastFileName);
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
