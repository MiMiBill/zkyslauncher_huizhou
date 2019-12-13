package com.muju.note.launcher.callkey.observer;

import android.os.FileObserver;
import android.support.annotation.Nullable;


import com.muju.note.launcher.callkey.bean.CallKeyInfo;
import com.muju.note.launcher.callkey.event.CallKeyDownEvent;
import com.muju.note.launcher.util.log.LogUtil;
import com.muju.note.launcher.util.system.SystemUtils;

import org.greenrobot.eventbus.EventBus;


/**
 * 用于监听/mnt/sdcard/skys/callkeylog.txt
 */
public class KeyCodeFileObserver extends FileObserver {


    public KeyCodeFileObserver(String path, int mask) {
        super(path, mask);
    }

    @Override
    public void onEvent(int event, @Nullable String path) {
        switch (event)
        {
            case FileObserver.MODIFY:
            {
                LogUtil.d("CallKey 文件被修改：" + path);
                //白天  关屏时才有效
                if (!SystemUtils.isScreenOn() && CallKeyInfo.getsInstance().isDaytimeMode() && CallKeyInfo.getsInstance().isEnableSwitchScreen())
                {
                    EventBus.getDefault().post(new CallKeyDownEvent(true,"Call按钮来自文件检测"));
                }
                break;
            }
            case FileObserver.CREATE:
            {
                LogUtil.d("CallKey 文件被创建：" + path);
                break;
            }
            case FileObserver.DELETE:
            {
                LogUtil.d("CallKey 文件被删除：" + path);
                break;
            }
        }

    }
}
