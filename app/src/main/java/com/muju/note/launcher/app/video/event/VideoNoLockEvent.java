package com.muju.note.launcher.app.video.event;
//不锁屏事件
public class VideoNoLockEvent {
    public boolean isLock;

    public VideoNoLockEvent(boolean isLock) {
        this.isLock = isLock;
    }
}
