package com.muju.note.launcher.event;

/**
 * Created by long yun
 * on 2019/12/26
 */
public class UpdateVideoInfoEvent {

    public boolean isStart;
    public String  msg;

    public UpdateVideoInfoEvent(boolean isStart) {
        this.isStart = isStart;
    }

    public UpdateVideoInfoEvent(boolean isStart, String msg) {
        this.isStart = isStart;
        this.msg = msg;
    }
}
