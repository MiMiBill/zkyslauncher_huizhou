package com.muju.note.launcher.app.video.event;

public class VideoCodeFailEvent {
    public boolean isFail;
    public VideoCodeFailEvent(boolean isFail) {
        this.isFail = isFail;
    }

    public boolean isPause() {
        return isFail;
    }

    public void setPause(boolean isFail) {
        isFail = isFail;
    }
}
