package com.muju.note.launcher.service.entity;


import com.muju.note.launcher.service.db.PadConfigSubDao;

import java.util.List;

public class PadCinfigNewEntity {


    /**
     * padConfigs : [{"updateDate":1552562583000,"hospitalId":4,"deptId":1,"pageSize":20,"id":10,"sort":"v","type":"openVideo1","pageNum":1,"content":"http://qiniuimage.zgzkys.com/openVideo0315.mp4","createDate":1552562583000},{"actionTime":"10:30","updateDate":1551699055000,"hospitalId":3,"deptId":1,"pageSize":20,"id":4,"sort":"v","type":"audio","pageNum":1,"content":"http://qiniuimage.zgzkys.com/中科云上早午音（音乐）.mp3","createDate":1551699055000},{"actionTime":"10:30","updateDate":1552296846000,"hospitalId":4,"deptId":1,"pageSize":20,"id":8,"sort":"v","type":"audio","pageNum":1,"content":"http://qiniuimage.zgzkys.com/中科云上早上音（音乐）.mp3","createDate":1552296846000},{"actionTime":"11:30","updateDate":1552296813000,"hospitalId":4,"deptId":1,"pageSize":20,"id":7,"sort":"v","type":"audio","pageNum":1,"content":"http://qiniuimage.zgzkys.com/中科云上中午音（音乐）.mp3","createDate":1552296813000},{"actionTime":"12:30","updateDate":1551699004000,"hospitalId":2,"deptId":1,"pageSize":20,"id":3,"sort":"v","type":"audio","pageNum":1,"content":"http://qiniuimage.zgzkys.com/中科云上中上音（音乐）.mp3","createDate":1551699004000},{"actionTime":"21:00","updateDate":1552296868000,"hospitalId":4,"deptId":1,"pageSize":20,"id":9,"sort":"v","type":"audio","pageNum":1,"content":"http://qiniuimage.zgzkys.com/中科云上晚上音（音乐）.mp3","createDate":1552296868000}]
     * sort : v
     */

    private String sort;
    private List<PadConfigSubDao> padConfigs;

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public List<PadConfigSubDao> getPadConfigs() {
        return padConfigs;
    }

    public void setPadConfigs(List<PadConfigSubDao> padConfigs) {
        this.padConfigs = padConfigs;
    }
}