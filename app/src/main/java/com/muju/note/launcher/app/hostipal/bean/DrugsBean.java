package com.muju.note.launcher.app.hostipal.bean;

import java.util.List;

public class DrugsBean {

    private String date;
    private String name;
    private List<DrugsSubBean> data;

    public DrugsBean() {
    }

    public DrugsBean(String date, String name, List<DrugsSubBean> data) {
        this.date = date;
        this.name = name;
        this.data = data;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<DrugsSubBean> getData() {
        return data;
    }

    public void setData(List<DrugsSubBean> data) {
        this.data = data;
    }
}
