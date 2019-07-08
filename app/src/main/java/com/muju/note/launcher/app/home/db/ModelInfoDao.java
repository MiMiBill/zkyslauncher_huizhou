package com.muju.note.launcher.app.home.db;

import org.litepal.crud.LitePalSupport;

public class ModelInfoDao extends LitePalSupport {

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
     *  开始时间
     */
    private long startTime;

    /**
     *  结束时间
     */
    private long endTime;

    /**
     *  时长
     */
    private long time;

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
