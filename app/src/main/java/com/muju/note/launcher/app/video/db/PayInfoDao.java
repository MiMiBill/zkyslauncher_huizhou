package com.muju.note.launcher.app.video.db;

import org.litepal.crud.LitePalSupport;

import java.io.Serializable;

public class PayInfoDao extends LitePalSupport implements Serializable {
    private String expireTime;


    public String getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(String expireTime) {
        this.expireTime = expireTime;
    }

}
