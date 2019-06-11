package com.muju.note.launcher.entity;

/**
 *  广告web
 */
public class AdvertWebEntity {

    private int advertId;
    private String title;
    private String url;
    private int type;

    public AdvertWebEntity(int advertId, String title, String url,int type) {
        this.advertId = advertId;
        this.title = title;
        this.url = url;
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getAdvertId() {
        return advertId;
    }

    public void setAdvertId(int advertId) {
        this.advertId = advertId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
