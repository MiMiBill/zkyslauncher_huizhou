package com.muju.note.launcher.app.hostipal.bean;

import com.muju.note.launcher.app.hostipal.db.InfoDao;
import com.muju.note.launcher.app.hostipal.db.InfomationDao;
import com.muju.note.launcher.okgo.BaseBean;

import java.util.ArrayList;

public class EncyUpdateBean extends BaseBean {
    private ArrayList<InfoDao> columns;
    private ArrayList<InfomationDao> mes;

    public ArrayList<InfoDao> getColumns() {
        return columns;
    }

    public void setColumns(ArrayList<InfoDao> columns) {
        this.columns = columns;
    }

    public ArrayList<InfomationDao> getMes() {
        return mes;
    }

    public void setMes(ArrayList<InfomationDao> mes) {
        this.mes = mes;
    }
}
