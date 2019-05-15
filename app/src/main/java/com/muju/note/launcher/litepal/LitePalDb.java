package com.muju.note.launcher.litepal;

import org.litepal.LitePal;
import org.litepal.LitePalDB;

public class LitePalDb {

    /**
     *  初始化数据库
     */
    public static void addDb(){
        LitePalDB litePalDB=new LitePalDB("zkys-data",2);
        litePalDB.setStorage("zkysdb");
        LitePal.use(litePalDB);
    }

}
