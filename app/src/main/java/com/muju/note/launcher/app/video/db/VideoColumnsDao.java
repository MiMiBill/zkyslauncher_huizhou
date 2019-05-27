package com.muju.note.launcher.app.video.db;

import org.litepal.crud.LitePalSupport;

import java.util.List;

public class VideoColumnsDao extends LitePalSupport {

    private int id;
    private String name;
    private int columnsId;
    private boolean isSelete;
    private List<VideoTagsDao> videoTags;

    public List<VideoTagsDao> getVideoTags() {
        return videoTags;
    }

    public void setVideoTags(List<VideoTagsDao> videoTags) {
        this.videoTags = videoTags;
    }

    public boolean isSelete() {
        return isSelete;
    }

    public void setSelete(boolean selete) {
        isSelete = selete;
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

    public int getColumnsId() {
        return columnsId;
    }

    public void setColumnsId(int columnsId) {
        this.columnsId = columnsId;
    }
}
