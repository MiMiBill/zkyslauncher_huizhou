package com.muju.note.launcher.app.home.db;

import org.litepal.crud.LitePalSupport;

public class AdvertsInfoDao extends LitePalSupport {

    /**
     * imei
     */
    private String imei;

    /**
     * 广告ID
     */
    private int advertId;

    /**
     * 医院ID
     */
    private int hosId;

    /**
     * 科室ID
     */
    private int depId;

    /**
     * 日期
     */
    private String date;

    /**
     * 类型
     */
    private int type;

    /**
     * 开始时间
     */
    private long startTime;

    /**
     * 结束时间
     */
    private long endTime;

    /**
     * 时长
     */
    private long time;

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public int getAdvertId() {
        return advertId;
    }

    public void setAdvertId(int advertId) {
        this.advertId = advertId;
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

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
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

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}
