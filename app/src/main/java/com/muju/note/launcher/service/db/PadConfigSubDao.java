package com.muju.note.launcher.service.db;

import org.litepal.LitePal;
import org.litepal.crud.LitePalSupport;

public class PadConfigSubDao extends LitePalSupport {

    /**
     * padConfigs : [{"updateDate":1552562583000,"hospitalId":4,"deptId":1,"pageSize":20,"id":10,"sort":"v","type":"openVideo1","pageNum":1,"content":"http://qiniuimage.zgzkys.com/openVideo0315.mp4","createDate":1552562583000},{"actionTime":"10:30","updateDate":1551699055000,"hospitalId":3,"deptId":1,"pageSize":20,"id":4,"sort":"v","type":"audio","pageNum":1,"content":"http://qiniuimage.zgzkys.com/中科云上早午音（音乐）.mp3","createDate":1551699055000},{"actionTime":"10:30","updateDate":1552296846000,"hospitalId":4,"deptId":1,"pageSize":20,"id":8,"sort":"v","type":"audio","pageNum":1,"content":"http://qiniuimage.zgzkys.com/中科云上早上音（音乐）.mp3","createDate":1552296846000},{"actionTime":"11:30","updateDate":1552296813000,"hospitalId":4,"deptId":1,"pageSize":20,"id":7,"sort":"v","type":"audio","pageNum":1,"content":"http://qiniuimage.zgzkys.com/中科云上中午音（音乐）.mp3","createDate":1552296813000},{"actionTime":"12:30","updateDate":1551699004000,"hospitalId":2,"deptId":1,"pageSize":20,"id":3,"sort":"v","type":"audio","pageNum":1,"content":"http://qiniuimage.zgzkys.com/中科云上中上音（音乐）.mp3","createDate":1551699004000},{"actionTime":"21:00","updateDate":1552296868000,"hospitalId":4,"deptId":1,"pageSize":20,"id":9,"sort":"v","type":"audio","pageNum":1,"content":"http://qiniuimage.zgzkys.com/中科云上晚上音（音乐）.mp3","createDate":1552296868000}]
     * sort : v
     */

    private String sort;
    private long updateDate;
    private int hospitalId;
    private int deptId;
    private int pageSize;
    private int id;
    private String type;
    private int pageNum;
    private String content;
    private long createDate;
    private String actionTime;

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public long getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(long updateDate) {
        this.updateDate = updateDate;
    }

    public int getHospitalId() {
        return hospitalId;
    }

    public void setHospitalId(int hospitalId) {
        this.hospitalId = hospitalId;
    }

    public int getDeptId() {
        return deptId;
    }

    public void setDeptId(int deptId) {
        this.deptId = deptId;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getPageNum() {
        return pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getCreateDate() {
        return createDate;
    }

    public void setCreateDate(long createDate) {
        this.createDate = createDate;
    }

    public String getActionTime() {
        return actionTime;
    }

    public void setActionTime(String actionTime) {
        this.actionTime = actionTime;
    }

    public static void deleteAll(){
        LitePal.useDefault();
        LitePal.deleteAll(PadConfigDao.class);
        LitePal.deleteAll(PadConfigSubDao.class);
    }

}
