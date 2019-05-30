package com.muju.note.launcher.app.video.db;

import org.litepal.crud.LitePalSupport;

public class VideoPlayerCountDao extends LitePalSupport {

    private int id;

    /**
     *  影视播放ID
     */
    private String cid;

    /**
     *  imei
     */
    private String imei;

    /**
     *  影视ID
     */
    private String videoId;

    /**
     *  影视名称
     */
    private String videoName;

    /**
     *  医院ID
     */
    private int hosId;

    /**
     *  科室ID
     */
    private int depId;

    /**
     *   日期
     */
    private String date;

    /**
     *  观看次数
     */
    private int playCount;

    /**
     *  观看时长
     */
    private long playTime;

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public String getVideoId() {
        return videoId;
    }

    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }

    public String getVideoName() {
        return videoName;
    }

    public void setVideoName(String videoName) {
        this.videoName = videoName;
    }

    public int getHosId() {
        return hosId;
    }

    public void setHosId(int hosId) {
        this.hosId = hosId;
    }

    public int getDepId() {
        return depId;
    }

    public void setDepId(int depId) {
        this.depId = depId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getPlayCount() {
        return playCount;
    }

    public void setPlayCount(int playCount) {
        this.playCount = playCount;
    }

    public long getPlayTime() {
        return playTime;
    }

    public void setPlayTime(long playTime) {
        this.playTime = playTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
