package com.muju.note.launcher.app.video.event;

import com.muju.note.launcher.app.video.bean.WeiXinTask;

/**
 * Created by long yun
 * on 2019/11/28
 */
public class WeiXinTaskEvent {
    public WeiXinTask.WeiXinTaskData weiXinTaskData;
    public WeiXinTaskEvent(WeiXinTask.WeiXinTaskData weiXinTaskData) {
        this.weiXinTaskData = weiXinTaskData;
    }

    public WeiXinTaskEvent() {
    }
}
