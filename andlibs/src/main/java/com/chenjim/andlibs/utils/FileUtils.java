package com.chenjim.andlibs.utils;

import android.graphics.Bitmap;

import com.blankj.utilcode.util.ConvertUtils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * @description：
 * @fileName: FileUtil
 * @author: jim.chen
 * @date: 2020/4/16
 */
public class FileUtils {
    public static final SimpleDateFormat CUR_TIME_FILE_FORMAT = new SimpleDateFormat("yyyyMMdd_HHmmss_SSS");

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
            byte[] buffer = new byte[1024];
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
    public static void zipFiles(Collection<File> resFileList, File zipFile, String comment) throws IOException {
        ZipOutputStream zipout = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(
                zipFile), 1024));
        for (File resFile : resFileList) {
            zipFile(resFile, zipout, "");
        }
        zipout.setComment(comment);
        zipout.close();
    }


    public static String getCurTimeFileName() {
        return CUR_TIME_FILE_FORMAT.format(new Date());
    }

    public static byte[] readToByteArray(String filePath) {
        File file = new File(filePath);
        long fileSize = file.length();
        if (fileSize > Integer.MAX_VALUE) {
            System.out.println("file too big...");
            return null;
        }
        try {
            FileInputStream fi = new FileInputStream(file);
            byte[] buffer = new byte[(int) fileSize];
            int offset = 0;
            int numRead = 0;
            while (offset < buffer.length
                    && (numRead = fi.read(buffer, offset, buffer.length - offset)) >= 0) {
                offset += numRead;
            }
            // 确保所有数据均被读取
            if (offset != buffer.length) {
                throw new IOException("Could not completely read file:" + file.getName());
            }
            fi.close();
            return buffer;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static boolean saveBitmap(Bitmap bitmap, String imagePath) {
        File file = new File(imagePath);
        File parentFile = file.getParentFile();
        if (!parentFile.exists()) {
            parentFile.mkdirs();
        }
        try {
            FileOutputStream fos = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, fos);
            fos.flush();
            fos.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean saveBytes(byte[] bytes, String filePath) {
        File file = new File(filePath);
        File parentFile = file.getParentFile();
        if (!parentFile.exists()) {
            parentFile.mkdirs();
        }
        try {
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(bytes);
            fos.flush();
            fos.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private synchronized static void write(File file, String message, boolean append) {
        if (file == null) {
            Logger.e("file is null,Could not write:" + message);
            return;
        }
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
            if (file.canWrite()) {
                BufferedWriter bufferedwriter = new BufferedWriter(new FileWriter(file, append));
                bufferedwriter.write((new StringBuffer()).append(message).toString());
                bufferedwriter.close();
            }
        } catch (IOException ioexception) {
            Logger.e("Could not write:'" + message + "'to file:" + file);
        }
    }

    /**
     * Return the MD5 of file.
     *
     * @param file The file.
     * @return the md5 of file
     */
    public static byte[] getFileMD5(final File file) {
        if (file == null) {
            return null;
        }
        DigestInputStream dis = null;
        try {
            FileInputStream fis = new FileInputStream(file);
            MessageDigest md = MessageDigest.getInstance("MD5");
            dis = new DigestInputStream(fis, md);
            byte[] buffer = new byte[1024 * 256];
            while (true) {
                if (!(dis.read(buffer) > 0)) {
                    break;
                }
            }
            md = dis.getMessageDigest();
            return md.digest();
        } catch (NoSuchAlgorithmException | IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (dis != null) {
                    dis.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static String getFileMD5Sting(final File file) {
        return ConvertUtils.bytes2String(getFileMD5(file));
    }

}
