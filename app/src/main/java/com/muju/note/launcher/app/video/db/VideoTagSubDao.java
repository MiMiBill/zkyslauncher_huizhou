package com.muju.note.launcher.app.video.db;

import org.litepal.crud.LitePalSupport;

public class VideoTagSubDao extends LitePalSupport {

    private int id;
    private int subId;
    private String name;
    private boolean choice;

    public VideoTagSubDao() {
    }

    public VideoTagSubDao(String name, boolean choice) {
        this.name = name;
        this.choice = choice;
    }

    public boolean isChoice() {
        return choice;
    }

    public void setChoice(boolean choice) {
        this.choice = choice;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSubId() {
        return subId;
    }

    public void setSubId(int subId) {
        this.subId = subId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
