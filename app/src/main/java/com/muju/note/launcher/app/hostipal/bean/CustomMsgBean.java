package com.muju.note.launcher.app.hostipal.bean;

public class CustomMsgBean {

    private int pos;
    private String title;
    private String content;
    private String date;

    public CustomMsgBean() {
    }

    public CustomMsgBean(int pos, String title, String content) {
        this.pos = pos;
        this.title = title;
        this.content = content;
    }

    public CustomMsgBean(int pos, String title, String content, String date) {
        this.pos = pos;
        this.title = title;
        this.content = content;
        this.date = date;
    }

    public int getPos() {
        return pos;
    }

    public void setPos(int pos) {
        this.pos = pos;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
