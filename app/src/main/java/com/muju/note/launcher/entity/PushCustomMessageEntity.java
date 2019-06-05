package com.muju.note.launcher.entity;

/**
 *  宣教推送
 */
public class PushCustomMessageEntity {

    private String title;
    private String url;
    private int customId;

    public PushCustomMessageEntity(String title, String url, int customId) {
        this.title = title;
        this.url = url;
        this.customId = customId;
    }

    public int getCustomId() {
        return customId;
    }

    public void setCustomId(int customId) {
        this.customId = customId;
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
