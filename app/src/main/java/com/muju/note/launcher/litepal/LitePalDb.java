package com.muju.note.launcher.litepal;

import com.muju.note.launcher.app.hostipal.db.MienInfoDao;
import com.muju.note.launcher.service.db.PadConfigDao;
import com.muju.note.launcher.service.db.PadConfigSubDao;

import org.litepal.LitePal;
import org.litepal.LitePalDB;

public class LitePalDb {

    public static final LitePalDB zkysDataDb=new LitePalDB("zkys-data",7);
    public static final LitePalDB zkysDb=new LitePalDB("zkys",11);

    /**
     *  初始化数据库
     */
    public static void addDb(){

        zkysDataDb.setStorage("zkysdb");
        LitePal.use(zkysDataDb);

        zkysDb.setStorage("zkysdb");
        zkysDb.addClassName(MienInfoDao.class.getName());
        zkysDb.addClassName(PadConfigDao.class.getName());
        zkysDb.addClassName(PadConfigSubDao.class.getName());
        LitePal.use(zkysDb);
    }

    /**
     *  切换成数据统计数据库
     */
    public static void setZkysDataDb(){
        LitePal.use(zkysDataDb);
    }

    public static void setZkysDb(){
        LitePal.use(zkysDb);
    }

}
