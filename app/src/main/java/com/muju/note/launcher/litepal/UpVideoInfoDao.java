package com.muju.note.launcher.litepal;

import org.litepal.crud.LitePalSupport;

public class UpVideoInfoDao extends LitePalSupport {

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

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    /**
     *  开始时间
     */
    private long startTime;

    /**
     *  结束时间
     */
    private long endTime;

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

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

}
