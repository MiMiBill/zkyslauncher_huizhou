package com.muju.note.launcher.callkey;

import android.os.Environment;

import java.io.File;

public class Constant {
    public static final String KEY_CODE_LOG_FILE_PATH_DIR = Environment.getExternalStorageDirectory() + File.separator + "keycode" + File.separator;
    public static final String KEY_CODE_LOG_FILE_PATH =  KEY_CODE_LOG_FILE_PATH_DIR  + "callkeylog.txt";
}
