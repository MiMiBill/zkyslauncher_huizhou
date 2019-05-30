package com.muju.note.launcher.app.userinfo.bean;

import java.io.Serializable;

public class PriseItemBean implements Serializable {
    private String imgRul;
    private int priseNum;
    private String title;
    private int status;
    private String createTime;

    public String getImgRul() {
        return imgRul;
    }

    public void setImgRul(String imgRul) {
        this.imgRul = imgRul;
    }

    public int getPriseNum() {
        return priseNum;
    }

    public void setPriseNum(int priseNum) {
        this.priseNum = priseNum;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
}
