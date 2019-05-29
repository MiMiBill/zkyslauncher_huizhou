package com.muju.note.launcher.app.video.event;

public class VideoReStartEvent {
    public boolean isStart;
    public VideoReStartEvent(boolean isStart) {
        this.isStart = isStart;
    }

    public boolean isPause() {
        return isStart;
    }

    public void setPause(boolean pause) {
        isStart = pause;
    }
}
