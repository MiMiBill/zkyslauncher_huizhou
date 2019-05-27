package com.muju.note.launcher.util.zip;

import com.muju.note.launcher.listener.OnZipSuccessListener;
import com.muju.note.launcher.util.log.LogFactory;
import com.muju.note.launcher.util.log.LogUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class ZipUtils {
    private static OnZipSuccessListener listener;
    public static final String TAG = "ZIP";
    private static final int BUFF_SIZE = 1024 * 1024; // 1M Byte
    public static ZipUtils zipUtils = null;

    public static ZipUtils getInstance() {
        if (zipUtils == null) {
            zipUtils = new ZipUtils();
        }
        return zipUtils;
    }


    /**
     * 解压zip到指定的路径
     *
     * @param zipFileString ZIP的名称
     * @param outPathString 要解压缩路径
     * @throws Exception
     */
    public static void UnZipFolder(String zipFileString, String outPathString, OnZipSuccessListener listener)  {
        ZipInputStream inZip = null;
        try {
            inZip = new ZipInputStream(new FileInputStream(zipFileString));
            ZipEntry zipEntry;
            String szName = "";
            while ((zipEntry = inZip.getNextEntry()) != null) {
                szName = zipEntry.getName();
                if (zipEntry.isDirectory()) {
                    //获取部件的文件夹名
                    szName = szName.substring(0, szName.length() - 1);
                    LogUtil.i(szName);
                    File folder = new File(outPathString + File.separator + szName);
                    folder.mkdirs();
                } else {
                    LogUtil.i(outPathString);
                    File file = new File(outPathString);
                    if (!file.exists()) {
                        LogFactory.l().i( "Create the file:" + outPathString);
                        file.getParentFile().mkdirs();
                        file.createNewFile();
                    }
                    // 获取文件的输出流
                    FileOutputStream out = new FileOutputStream(file);
                    int len;
                    byte[] buffer = new byte[1024];
                    // 读取（字节）字节到缓冲区
                    while ((len = inZip.read(buffer)) != -1) {
                        // 从缓冲区（0）位置写入（字节）字节
                        out.write(buffer, 0, len);
                        out.flush();
                    }
                    out.close();
                }
            }
            if (listener != null) {
                listener.OnZipSuccess();
            }
            inZip.close();
        } catch (Exception e) {
            LogFactory.l().i("e==="+e.getMessage());
            e.printStackTrace();
        }
    }
}