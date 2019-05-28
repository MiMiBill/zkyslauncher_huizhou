package com.muju.note.launcher.app.video.db;

import org.litepal.crud.LitePalSupport;

public class VideoHisDao extends LitePalSupport {

    private int id;
    private int videoId;
    private String cid;
    private String name;
    private String description;
    private String imgUrl;
    private int duration;
    private int videoType;
    private String createTime;
    private String customTag;
    private String playType;

    public String getPlayType() {
        return playType;
    }

    public void setPlayType(String playType) {
        this.playType = playType;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getVideoId() {
        return videoId;
    }

    public void setVideoId(int videoId) {
        this.videoId = videoId;
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getVideoType() {
        return videoType;
    }

    public void setVideoType(int videoType) {
        this.videoType = videoType;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getCustomTag() {
        return customTag;
    }

    public void setCustomTag(String customTag) {
        this.customTag = customTag;
    }
}
