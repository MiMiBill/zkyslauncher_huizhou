package com.muju.note.launcher.app.video.bean;

public class VideoDownLoadBean {


    /**
     * path : http://qiniupaddb.zgzkys.com/video_1558022400.db
     * id : 10
     * createDate : 1557992897000
     * tableName : video
     */

    private String path;
    private int id;
    private long createDate;
    private String tableName;
    private int count;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getCreateDate() {
        return createDate;
    }

    public void setCreateDate(long createDate) {
        this.createDate = createDate;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }
}
