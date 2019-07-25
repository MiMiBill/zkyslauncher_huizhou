package com.muju.note.launcher.app.hostipal.db;

import org.litepal.crud.LitePalSupport;

public class MissionCountDao extends LitePalSupport {

    private int id;

    /**
     *  imei
     */
    private String imei;

    /**
     *  医院ID
     */
    private int hospId;

    /**
     *  科室ID
     */
    private int deptId;

    /**
     *  宣教ID
     */
    private int missionId;

    /**
     *  宣教名称
     */
    private String missionName;

    /**
     *  日期
     */
    private String date;

    /**
     *  展示次数
     */
    private int showCount;

    /**
     *  浏览时长
     */
    private long showTime;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public int getHospId() {
        return hospId;
    }

    public void setHospId(int hospId) {
        this.hospId = hospId;
    }

    public int getDeptId() {
        return deptId;
    }

    public void setDeptId(int deptId) {
        this.deptId = deptId;
    }

    public int getMissionId() {
        return missionId;
    }

    public void setMissionId(int missionId) {
        this.missionId = missionId;
    }

    public String getMissionName() {
        return missionName;
    }

    public void setMissionName(String missionName) {
        this.missionName = missionName;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getShowCount() {
        return showCount;
    }

    public void setShowCount(int showCount) {
        this.showCount = showCount;
    }

    public long getShowTime() {
        return showTime;
    }

    public void setShowTime(long showTime) {
        this.showTime = showTime;
    }
}
