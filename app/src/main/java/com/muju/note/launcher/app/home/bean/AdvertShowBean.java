package com.muju.note.launcher.app.home.bean;

public class AdvertShowBean {

    private int day;
    private String code;

    public AdvertShowBean(int day, String code) {
        this.day = day;
        this.code = code;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
