package com.muju.note.launcher.app.hostipal.db;

import org.litepal.crud.LitePalSupport;

public class MissionInfoDao extends LitePalSupport {


    /**
     * deptName : 外科
     * img : http://qiniuhospital.zgzkys.com/Fl6IVYmikMQ8O1vSv1iAx51TWE9u
     * hospitalId : 4
     * deptId : 2
     * pageSize : 20
     * disabled : 1
     * updateTime : 2019-05-07 18:16:54.0
     * hospitalName : 东莞大朗医院
     * id : 63
     * video : http://qiniuhospital.zgzkys.com/lilhx4SfB1kQjCIHKCPNCg5-uNb3
     * title : 康复宣教
     * pageNum : 1
     */

    private String deptName;
    private String img;
    private int hospitalId;
    private int deptId;
    private int pageSize;
    private int disabled;
    private String updateTime;
    private String hospitalName;
    private int id;
    private String video;
    private String title;
    private int pageNum;
    private int missionId;
    private String frontCover;

    public String getFrontCover() {
        return frontCover;
    }

    public void setFrontCover(String frontCover) {
        this.frontCover = frontCover;
    }

    public int getMissionId() {
        return missionId;
    }

    public void setMissionId(int missionId) {
        this.missionId = missionId;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public int getHospitalId() {
        return hospitalId;
    }

    public void setHospitalId(int hospitalId) {
        this.hospitalId = hospitalId;
    }

    public int getDeptId() {
        return deptId;
    }

    public void setDeptId(int deptId) {
        this.deptId = deptId;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getDisabled() {
        return disabled;
    }

    public void setDisabled(int disabled) {
        this.disabled = disabled;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getHospitalName() {
        return hospitalName;
    }

    public void setHospitalName(String hospitalName) {
        this.hospitalName = hospitalName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getPageNum() {
        return pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }
}
