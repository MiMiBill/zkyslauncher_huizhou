package com.muju.note.launcher.app.home.db;

import org.litepal.crud.LitePalSupport;

public class AdvertsCountDao extends LitePalSupport {

    /**
     *  imei
     */
    private String imei;

    /**
     *  广告ID
     */
    private int advertId;

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
     *  点击次数
     */
    private int clickCount;

    /**
     *  广告展示时长，单位毫秒
     */
    private long showTime;

    /**
     *  广告浏览时长，单位毫秒
     */
    private long browseTime;

    /**
     *  广告展示次数
     */
    private int showCount;


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

    public int getClickCount() {
        return clickCount;
    }

    public void setClickCount(int clickCount) {
        this.clickCount = clickCount;
    }

    public long getShowTime() {
        return showTime;
    }

    public void setShowTime(long showTime) {
        this.showTime = showTime;
    }

    public long getBrowseTime() {
        return browseTime;
    }

    public void setBrowseTime(long browseTime) {
        this.browseTime = browseTime;
    }

    public int getShowCount() {
        return showCount;
    }

    public void setShowCount(int showCount) {
        this.showCount = showCount;
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }
}
