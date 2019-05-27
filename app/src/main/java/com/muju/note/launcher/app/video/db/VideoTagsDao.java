package com.muju.note.launcher.app.video.db;

import org.litepal.crud.LitePalSupport;

import java.util.List;

public class VideoTagsDao extends LitePalSupport {

    private int id;
    private String name;
    private int tagId;
    private int coulmnsId;
    private List<VideoTagSubDao> list;

    public int getCoulmnsId() {
        return coulmnsId;
    }

    public void setCoulmnsId(int coulmnsId) {
        this.coulmnsId = coulmnsId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getTagId() {
        return tagId;
    }

    public void setTagId(int tagId) {
        this.tagId = tagId;
    }

    public List<VideoTagSubDao> getList() {
        return list;
    }

    public void setList(List<VideoTagSubDao> list) {
        this.list = list;
    }
}
