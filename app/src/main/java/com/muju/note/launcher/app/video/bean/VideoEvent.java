package com.muju.note.launcher.app.video.bean;

/**
 * 视频操作的事件类
 */
public class VideoEvent {
    public static final int RELOGIN = 0;//重新登录
    public static final int FULLSCREEN = 1;//全屏播放
    public static final int EXIT_FULLSCREEN = 2;//退出全屏
    public static final int PAUSE = 3;//暂停
    public static final int RESUME = 4;//继续播放
    public static final int PREMIUM = 7;//继续播放

    public static final int VOICE_SEACHER = 5;
    public static final int GAME_SEACHER = 6;

    public int code;

    public int getCode() {
        return code;
    }

    public VideoEvent(int code) {
        this.code = code;
    }

}
