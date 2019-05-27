package com.muju.note.launcher.app.activeApp.entity;

/**
 * 资源下载类
 */
public class ResourceEntity {

    public static final int INEXECUTION = -1;
    //宣教视频资源
    public static final int MISSION_VIDEO = 0;
    //宣教PDF
    public static final int MISSION_PDF = 1;
    //医疗百科
    public static final int ENCY_ZIP = 5;

    //定时播放音频
    public static final int TIMING_AUDIO = 2;

    public static final int TIMING_OPEN_PLAY = 3;

    //APP更新包
    public static final int APP_UPDATE_PACKAGE = 4;




    private int type;
    private String url;
    private String fileName;

    public ResourceEntity(int type, String url, String fileName) {
        this.type = type;
        this.url = url;
        this.fileName = fileName;
    }

    public ResourceEntity(int type, String fileName) {
        this.type = type;
        this.fileName = fileName;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getUrl() {
        return url == null ? "" : url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getFileName() {
        return fileName == null ? "" : fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}

