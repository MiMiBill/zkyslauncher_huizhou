package com.muju.note.launcher.app.msg.db;

import org.litepal.crud.LitePalSupport;

public class CustomMessageDao extends LitePalSupport {
    private int id;
    private int msgId;
    private int xjId;
    private String title;
    private String type;
    private String author;
    private String url;
    private String time;
    private long createTime;

    public int getMsgId() {
        return msgId;
    }

    public void setMsgId(int msgId) {
        this.msgId = msgId;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getXjId() {
        return xjId;
    }

    public void setXjId(int xjId) {
        this.xjId = xjId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
