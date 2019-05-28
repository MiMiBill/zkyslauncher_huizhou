package com.muju.note.launcher.app.video.event;

public class VideoPauseEvent {
    public boolean isPause;
    public int duration;

    public VideoPauseEvent(boolean isPause) {
        this.isPause = isPause;
    }

    public boolean isPause() {
        return isPause;
    }

    public void setPause(boolean pause) {
        isPause = pause;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }
}
