package com.muju.note.launcher.app.hostipal.db;

import com.muju.note.launcher.litepal.LitePalDb;

import org.litepal.LitePal;
import org.litepal.crud.LitePalSupport;

public class InfoDao extends LitePalSupport {
    private int columnId;
    private int id;
    private String name;
    private boolean isCheck;


    /**
     *   新增数据
     * @param dao
     */
    public void saveDb(InfoDao dao){
        LitePal.use(LitePalDb.zkysDb);
        dao.save();
    }
    public boolean isCheck() {
        return isCheck;
    }

    public void setCheck(boolean check) {
        isCheck = check;
    }

    public int getColumnId() {
        return columnId;
    }

    public void setColumnId(int columnId) {
        this.columnId = columnId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
