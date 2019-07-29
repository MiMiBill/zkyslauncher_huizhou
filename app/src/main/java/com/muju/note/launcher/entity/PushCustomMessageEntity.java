package com.muju.note.launcher.entity;

/**
 *  宣教推送
 */
public class PushCustomMessageEntity {

    private String id;
    private String pushId;
    private int pushType;  //1表示定时事件

    public int getPushType() {
        return pushType;
    }

    public void setPushType(int pushType) {
        this.pushType = pushType;
    }

    public PushCustomMessageEntity(String id, int pushType) {
        this.id = id;
        this.pushType = pushType;
    }

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
