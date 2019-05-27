package com.muju.note.launcher.util.log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * log工厂类用于创建一个log控制命令
 */
public class LogFactory {
    private static final String TAG = "zkpad";
    private static CommonLog log = null;

    public static void e(String tag) {
        createLog().e(tag);
    }

    public static CommonLog createLog() {
        if (log == null) {
            log = new CommonLog();
        }
        log.setTag(TAG);
        return log;
    }

    public static CommonLog l() {
        if (log == null) {
            log = new CommonLog();
        }
        log.setTag(TAG);
        return log;
    }

    /**
     * 创建log时，筛选log的字符。例如：Log.e(Log筛选时用到的过滤值, "要打印的内容");
     *
     * @param tag
     * @return
     */
    public static CommonLog createLog(String tag) {
        if (log == null) {
            log = new CommonLog();
        }

        if (tag == null || tag.length() < 1) {
            log.setTag(TAG);
        } else {
            log.setTag(tag);
        }
        return log;
    }

    public static String getLog() {
        String str = null;
        try {
            ArrayList<String> cmdLine = new ArrayList<String>(); // 设置命令 logcat
            // -d 读取日志
            cmdLine.add("logcat");
            cmdLine.add("-e");
            ArrayList<String> clearLog = new ArrayList<String>(); // 设置命令 logcat
            // -c 清除日志
            clearLog.add("logcat");
            clearLog.add("-c");
            Process process = Runtime.getRuntime().exec(
                    cmdLine.toArray(new String[cmdLine.size()])); // 捕获日志
            BufferedReader bufferedReader = new BufferedReader(
                    new InputStreamReader(process.getInputStream())); // 将捕获内容转换为BufferedReader
            while ((str = bufferedReader.readLine()) != null) // 开始读取日志，每次读取一行
            {
                Runtime.getRuntime().exec(
                        clearLog.toArray(new String[clearLog.size()])); // 清理日志....这里至关重要，不清理的话，任何操作都将产生新的日志，代码进入死循环，直到bufferreader满
                // 输出，在logcat中查看效果，也可以是其他操作，比如发送给服务器..
            }
            if (str == null) {
                str = "log is null";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return str;
    }
}
