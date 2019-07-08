package com.muju.note.launcher.app.home.db;

import org.litepal.crud.LitePalSupport;

public class ModelCountDao extends LitePalSupport {

    private int id;

    /**
     *  imei
     */
    private String imei;

    /**
     *  模块名称，类名
     */
    private String modelName;

    /**
     *  模块标识
     */
    private String modelTag;

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
     *  展示时长，单位毫秒
     */
    private long showTime;

    /**
     *  展示次数
     */
    private int showCount;

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
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

    public long getShowTime() {
        return showTime;
    }

    public void setShowTime(long showTime) {
        this.showTime = showTime;
    }

    public int getShowCount() {
        return showCount;
    }

    public void setShowCount(int showCount) {
        this.showCount = showCount;
    }

    public String getModelTag() {
        return modelTag;
    }

    public void setModelTag(String modelTag) {
        this.modelTag = modelTag;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
