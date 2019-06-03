package com.muju.note.launcher.app.startUp.entity;

import android.graphics.drawable.Drawable;

/**
 * 益智游戏实体类
 */
public class MyAppInfoEntity {
    private Drawable image;
    private String appName;
    private String pkgName;


    public Drawable getImage() {
        return image;
    }

    public void setImage(Drawable image) {
        this.image = image;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getPkgName() {
        return pkgName;
    }

    public void setPkgName(String pkgName) {
        this.pkgName = pkgName;
    }
}
