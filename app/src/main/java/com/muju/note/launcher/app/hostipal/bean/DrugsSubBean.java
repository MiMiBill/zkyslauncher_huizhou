package com.muju.note.launcher.app.hostipal.bean;

public class DrugsSubBean {

    private int pos;
    private String name;
    private String content;
    private int tag;

    public DrugsSubBean() {
    }

    public DrugsSubBean(int pos, String name, String content, int tag) {
        this.pos = pos;
        this.name = name;
        this.content = content;
        this.tag = tag;
    }

    public int getPos() {
        return pos;
    }

    public void setPos(int pos) {
        this.pos = pos;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getTag() {
        return tag;
    }

    public void setTag(int tag) {
        this.tag = tag;
    }
}
