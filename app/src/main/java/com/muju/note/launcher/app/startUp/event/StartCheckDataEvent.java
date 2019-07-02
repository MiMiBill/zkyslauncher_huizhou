package com.muju.note.launcher.app.startUp.event;

public class StartCheckDataEvent {

    public enum Status{
        // TODO: 2019/6/14  以下为所有下载类型的回调 ，用于界面改变状态使用

        /**
         *  影视分类相关
         */
        VIDEO_COLUMN_START,
        VIDEO_COLUMN_SUCCESS,
        VIDEO_COLUMN_HTTP_START,
        VIDEO_COLUMN_HTTP_FAIL,
        VIDEO_COLUMN_HTTP_DATA_NULL,
        VIDEO_COLUMN_DB_START,

        /**
         *  影视首页相关
         *
         */
        VIDEO_TOP_START,
        VIDEO_TOP_SUCCESS,
        VIDEO_TOP_HTTP_START,
        VIDEO_TOP_HTTP_FAIL,
        VIDEO_TOP_HTTP_DATA_NULL,
        VIDEO_TOP_DB_START,

        /**
         *  影视数据相关
         */
        VIDEO_INFO_START,
        VIDEO_INFO_SUCCESS,
        VIDEO_INFO_HTTP_START,
        VIDEO_INFO_HTTP_FAIL,
        VIDEO_INFO_HTTP_DATA_NULL,
        VIDEO_INFO_DB_START,
        VIDEO_INFO_DB_PROGRESS,
        VIDEO_INFO_CARSH,
        VIDEO_INFO_DOWNLOAD_START,
        VIDEO_INFO_DOWNLOAD_PROGRESS,
        VIDEO_INFO_DOWNLOAD_FAIL,

        /**
         *  医院风采相关
         */
        HOSPITAL_MIEN_START,
        HOSPITAL_MIEN_SUCCESS,
        HOSPITAL_MIEN_HTTP_START,
        HOSPITAL_MIEN_HTTP_FAIL,
        HOSPITAL_MIEN_DB_START,

        /**
         *  医疗百科相关
         */
        HOSPITAL_ENCY_START,
        HOSPITAL_ENCY_SUCCESS,
        HOSPITAL_ENCY_HTTP_START,
        HOSPITAL_ENCY_HTTP_FAIL,
        HOSPITAL_ENCY_DOWNLOAD_START,
        HOSPITAL_ENCY_DOWNLOAD_PROGRESS,
        HOSPITAL_ENCY_DOWNLOAD_FAIL,
        HOSPITAL_ENCY_UNZIP_START,
        HOSPITAL_ENCY_FIRST_DB_START,
        HOSPITAL_ENCY_FIRST_DB_END,
        HOSPITAL_ENCY_TWO_DB_START,
        HOSPITAL_ENCY_TWO_DB_PROGRESS,

        /**
         *  医院风采相关
         */
        HOSPITAL_MISS_START,
        HOSPITAL_MISS_SUCCESS,
        HOSPITAL_MISS_HTTP_START,
        HOSPITAL_MISS_HTTP_FAIL,
        HOSPITAL_MISS_DB_START,

        /**
         * 首页菜单相关
         */
        HOME_MENU_START,
        HOME_MENU_SUCCESS,
        HOME_MENU_HTTP_START,
        HOME_MENU_HTTP_FAIL,
        HOME_MENU_HTTP_DATA_NULL,
        HOME_MENU_DB_START,

        /**
         * 首页菜单相关
         */
        HOME_MENU_REBOOT_START,
        HOME_MENU_REBOOT_SUCCESS,
        HOME_MENU_REBOOT_HTTP_START,
        HOME_MENU_REBOOT_HTTP_FAIL,
        HOME_MENU_REBOOT_HTTP_DATA_NULL,
        HOME_MENU_REBOOT_DB_START,
    }

    private Status status;
    private int progress;
    private String dbProgress;
    private Exception carsh;
    private boolean isAdd=true;

    public boolean isAdd() {
        return isAdd;
    }

    public void setAdd(boolean add) {
        isAdd = add;
    }

    public Exception getCarsh() {
        return carsh;
    }

    public void setCarsh(Exception carsh) {
        this.carsh = carsh;
    }

    public StartCheckDataEvent(Status status, Exception carsh) {
        this.status = status;
        this.carsh = carsh;
    }

    public String getDbProgress() {
        return dbProgress;
    }

    public void setDbProgress(String dbProgress) {
        this.dbProgress = dbProgress;
    }

    public StartCheckDataEvent(Status status, String dbProgress) {
        this.status = status;
        this.dbProgress = dbProgress;
    }

    public StartCheckDataEvent(Status status, int progress) {
        this.status = status;
        this.progress = progress;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public StartCheckDataEvent(Status status) {
        this.status = status;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
