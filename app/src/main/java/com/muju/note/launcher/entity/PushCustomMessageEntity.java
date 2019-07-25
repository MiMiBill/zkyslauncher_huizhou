package com.muju.note.launcher.entity;

/**
 *  宣教推送
 */
public class PushCustomMessageEntity {

    private String id;
    private String pushId;

    public String getPushId() {
        return pushId;
    }

    public void setPushId(String pushId) {
        this.pushId = pushId;
    }

    public PushCustomMessageEntity(String id) {
        this.id = id;
    }

    public PushCustomMessageEntity(String id, String pushId) {
        this.id = id;
        this.pushId = pushId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
