package com.muju.note.launcher.app.timetask.event;

public class TimeTaskEvent {
    private int code;

    public TimeTaskEvent(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
