package com.muju.note.launcher.app.hostipal.db;

import org.litepal.crud.LitePalSupport;

import java.util.ArrayList;

public class DepartmentInfoDao extends LitePalSupport{

    private String deptName;
    private int  hospitalId;
    private int  deptId;

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
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

    //    "deptName": "CCU",
//            "hospitalId": 4,
//            "deptId": 50


}
