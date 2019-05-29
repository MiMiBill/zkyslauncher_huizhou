package com.muju.note.launcher.litepal;

import com.muju.note.launcher.app.home.db.AdvertsCountDao;
import com.muju.note.launcher.app.home.db.AdvertsInfoDao;
import com.muju.note.launcher.app.home.db.AdvertsCacheDao;
import com.muju.note.launcher.app.hostipal.db.InfoDao;
import com.muju.note.launcher.app.hostipal.db.InfomationDao;
import com.muju.note.launcher.app.hostipal.db.MienInfoDao;
import com.muju.note.launcher.app.hostipal.db.MissionInfoDao;
import com.muju.note.launcher.app.video.db.VideoColumnsDao;
import com.muju.note.launcher.app.video.db.VideoHisDao;
import com.muju.note.launcher.app.video.db.VideoInfoDao;
import com.muju.note.launcher.app.video.db.VideoInfoTopDao;
import com.muju.note.launcher.app.video.db.VideoPlayerCountDao;
import com.muju.note.launcher.app.video.db.VideoPlayerInfoDao;
import com.muju.note.launcher.app.video.db.VideoTagSubDao;
import com.muju.note.launcher.app.video.db.VideoTagsDao;
import com.muju.note.launcher.service.db.PadConfigDao;
import com.muju.note.launcher.service.db.PadConfigSubDao;

import org.litepal.LitePal;
import org.litepal.LitePalDB;

public class LitePalDb {

    public static final LitePalDB zkysDataDb=new LitePalDB("zkys-data",9);
    public static final LitePalDB zkysDb=new LitePalDB("zkys",15);

    /**
     *  初始化数据库
     */
    public static void addDb(){

        zkysDataDb.setStorage("zkysdb");
        zkysDataDb.addClassName(UpAdvertInfoDao.class.getName());
        zkysDataDb.addClassName(UpVideoInfoDao.class.getName());
        LitePal.use(zkysDataDb);

        zkysDb.setStorage("zkysdb");
        zkysDb.addClassName(AdvertsCountDao.class.getName());
        zkysDb.addClassName(AdvertsInfoDao.class.getName());
        zkysDb.addClassName(AdvertsCacheDao.class.getName());
        zkysDb.addClassName(MienInfoDao.class.getName());
        zkysDb.addClassName(MissionInfoDao.class.getName());
        zkysDb.addClassName(PadConfigDao.class.getName());
        zkysDb.addClassName(InfoDao.class.getName());
        zkysDb.addClassName(InfomationDao.class.getName());
        zkysDb.addClassName(PadConfigSubDao.class.getName());
        zkysDb.addClassName(VideoColumnsDao.class.getName());
        zkysDb.addClassName(VideoInfoDao.class.getName());
        zkysDb.addClassName(VideoInfoTopDao.class.getName());
        zkysDb.addClassName(VideoTagsDao.class.getName());
        zkysDb.addClassName(VideoTagSubDao.class.getName());
        zkysDb.addClassName(VideoPlayerInfoDao.class.getName());
        zkysDb.addClassName(VideoPlayerCountDao.class.getName());
        zkysDb.addClassName(VideoHisDao.class.getName());
        zkysDb.addClassName(VideoHisDao.class.getName());
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
