package com.muju.note.launcher.app.video.event;

public class VideoStartEvent {
    public boolean isStart;
    public VideoStartEvent(boolean isStart) {
        this.isStart = isStart;
    }

    public boolean isPause() {
        return isStart;
    }

    public void setPause(boolean pause) {
        isStart = pause;
    }
}
