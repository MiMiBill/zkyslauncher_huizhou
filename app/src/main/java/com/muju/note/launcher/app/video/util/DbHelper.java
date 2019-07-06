package com.muju.note.launcher.app.video.util;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.muju.note.launcher.app.home.bean.AdverNewBean;
import com.muju.note.launcher.app.home.bean.AdvertsBean;
import com.muju.note.launcher.app.home.db.AdvertsCodeDao;
import com.muju.note.launcher.app.home.event.GetAdvertEvent;
import com.muju.note.launcher.app.hostipal.db.InfoDao;
import com.muju.note.launcher.app.hostipal.db.InfomationDao;
import com.muju.note.launcher.app.startUp.event.StartCheckDataEvent;
import com.muju.note.launcher.app.video.db.VideoInfoDao;
import com.muju.note.launcher.litepal.LitePalDb;
import com.muju.note.launcher.litepal.UpAdvertInfoDao;
import com.muju.note.launcher.litepal.UpVideoInfoDao;
import com.muju.note.launcher.topics.SpTopics;
import com.muju.note.launcher.util.Constants;
import com.muju.note.launcher.util.log.LogFactory;
import com.muju.note.launcher.util.log.LogUtil;
import com.muju.note.launcher.util.sp.SPUtil;

import org.greenrobot.eventbus.EventBus;
import org.litepal.LitePal;
import org.litepal.crud.callback.SaveCallback;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class DbHelper {

    public static final String TAG="DbHelper";

    public static SQLiteDatabase getDataBase(String dbPath) throws Exception {
        SQLiteDatabase sqLiteDatabase=SQLiteDatabase.openDatabase(dbPath,null,0);
        return sqLiteDatabase;
    }

    public static Cursor query(String dbPath, String tableName) throws Exception {
        SQLiteDatabase database=getDataBase(dbPath);
        Cursor cursor=database.query(tableName,null,null,null,null,null,null);
        return cursor;
    }

    /**
     *  插入影视数据
     * @param dbPath
     * @param tableName
     */
    public static void insertToVideo(final String dbPath, final String tableName, final int count, final long createTime) throws Exception {
        EventBus.getDefault().post(new StartCheckDataEvent(StartCheckDataEvent.Status.VIDEO_INFO_DB_START));
        final int[] num = {0};
        EventBus.getDefault().post(new StartCheckDataEvent(StartCheckDataEvent.Status.VIDEO_INFO_DB_PROGRESS,num[0]+"/"+count));
        ExecutorService service=Executors.newSingleThreadExecutor();
        service.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    LitePal.deleteAll(VideoInfoDao.class);
                    final SQLiteDatabase database=getDataBase(dbPath);
                    ArrayList<VideoInfoDao> videoInfoDaos=new ArrayList<>();
                    final Cursor cursor=database.query(tableName,null,null,null,null,null,null);
                    while (cursor.moveToNext()){
                        num[0]++;
                        EventBus.getDefault().post(new StartCheckDataEvent(StartCheckDataEvent.Status.VIDEO_INFO_DB_PROGRESS,num[0]+"/"+count));
                        VideoInfoDao dao=new VideoInfoDao();
                        dao.setSerialVersionUID(cursor.getInt(cursor.getColumnIndex("serialVersionUID")));
                        dao.setVideoId(cursor.getInt(cursor.getColumnIndex("id")));
                        dao.setCid(cursor.getString(cursor.getColumnIndex("cid")));
                        dao.setName(cursor.getString(cursor.getColumnIndex("name")));
                        dao.setDescription(cursor.getString(cursor.getColumnIndex("description")));
                        dao.setDirector(cursor.getString(cursor.getColumnIndex("director")));
                        dao.setImgUrl(cursor.getString(cursor.getColumnIndex("imgUrl")));
                        dao.setRegion(cursor.getString(cursor.getColumnIndex("region")));
                        dao.setScreenUrl(cursor.getString(cursor.getColumnIndex("screenUrl")));
                        dao.setKeywords(cursor.getString(cursor.getColumnIndex("keywords")));
                        dao.setActor(cursor.getString(cursor.getColumnIndex("actor")));
                        dao.setDuration(cursor.getInt(cursor.getColumnIndex("duration")));
                        dao.setEditTime(cursor.getString(cursor.getColumnIndex("editTime")));
                        dao.setOnwayTime(cursor.getString(cursor.getColumnIndex("onwayTime")));
                        dao.setVideoType(cursor.getInt(cursor.getColumnIndex("videoType")));
                        dao.setWatchCount(cursor.getInt(cursor.getColumnIndex("watchCount")));
                        dao.setStatus(cursor.getInt(cursor.getColumnIndex("status")));
                        dao.setFee(cursor.getInt(cursor.getColumnIndex("fee")));
                        dao.setCreateTime(cursor.getString(cursor.getColumnIndex("createTime")));
                        dao.setCustomTag(cursor.getString(cursor.getColumnIndex("customTag")));
                        dao.setIsRecommend(cursor.getInt(cursor.getColumnIndex("isRecommend")));
                        dao.setIsClassify(cursor.getInt(cursor.getColumnIndex("isClassify")));
                        dao.setIsNew(cursor.getInt(cursor.getColumnIndex("isNew")));
                        dao.setUpdateTime(cursor.getString(cursor.getColumnIndex("updateTime")));
                        dao.setSource(cursor.getInt(cursor.getColumnIndex("source")));
                        dao.setColumnId(cursor.getInt(cursor.getColumnIndex("columnId")));
                        dao.setColumnName(cursor.getString(cursor.getColumnIndex("columnName")));
                        dao.setScore(cursor.getString(cursor.getColumnIndex("score")));
                        dao.setTid(cursor.getString(cursor.getColumnIndex("tid")));
                        dao.setNumber(cursor.getInt(cursor.getColumnIndex("number")));
                        dao.setLogoUrl(cursor.getString(cursor.getColumnIndex("logoUrl")));
                        dao.setTypeId(cursor.getString(cursor.getColumnIndex("typeId")));
//                        dao.saveDb(dao);
                        videoInfoDaos.add(dao);
                    }
                    EventBus.getDefault().post(new StartCheckDataEvent(StartCheckDataEvent.Status.VIDEO_DATA_SAVE));
                    LitePal.saveAllAsync(videoInfoDaos).listen(new SaveCallback() {
                        @Override
                        public void onFinish(boolean success) {
                            if(success){
                                SPUtil.putLong(SpTopics.SP_VIDEO_UPDATE_TIME,(createTime/1000));
                                LogUtil.i(TAG,"数据插入结束时间："+System.currentTimeMillis());
                                EventBus.getDefault().post(new StartCheckDataEvent(StartCheckDataEvent.Status.VIDEO_INFO_SUCCESS));
                            }else {
                                EventBus.getDefault().post(new StartCheckDataEvent(StartCheckDataEvent.Status.VIDEO_DATA_SAVE_FAIL));
                            }
                            cursor.close();
                            database.close();
                        }
                    });
                }catch (Exception e){
                    e.printStackTrace();
                    EventBus.getDefault().post(new StartCheckDataEvent(StartCheckDataEvent.Status.VIDEO_INFO_CARSH,e));
                }
            }
        });
    }

    /**
     *  插入广告详情数据
     * @param dbPath
     * @param dao
     */
    public static void insertToAdvertData(String dbPath, UpAdvertInfoDao dao) throws Exception{
        try {
            SQLiteDatabase database=getDataBase(dbPath);
            ContentValues values=new ContentValues();
            values.put("imei",dao.getImei());
            values.put("advertId",dao.getAdvertId());
            values.put("hosId",dao.getHosId());
            values.put("depId",dao.getDepId());
            values.put("date",dao.getDate());
            values.put("type",dao.getType());
            values.put("startTime",dao.getStartTime());
            values.put("endTime",dao.getEndTime());
            values.put("time",dao.getTime());
            database.insert("UpAdvertInfoDao",null,values);
            database.close();
        }catch (Exception e){
            e.printStackTrace();
            LitePalDb.setZkysDataDb();
            UpAdvertInfoDao infoDao=new UpAdvertInfoDao();
            infoDao.setImei("异常数据");
            infoDao.save();
        }
    }

    /**
     *  插入影视统计数据
     * @param dbPath
     * @param dao
     */
    public static void insertToVideoData(String dbPath, UpVideoInfoDao dao) throws Exception{
        try {
            SQLiteDatabase database=getDataBase(dbPath);
            ContentValues values=new ContentValues();
            values.put("imei",dao.getImei());
            values.put("videoId",dao.getVideoId());
            values.put("hosId",dao.getHosId());
            values.put("depId",dao.getDepId());
            values.put("date",dao.getDate());
            values.put("videoName",dao.getVideoName());
            values.put("startTime",dao.getStartTime());
            values.put("endTime",dao.getEndTime());
            values.put("cid",dao.getCid());
            database.insert("UpVideoInfoDao",null,values);
            database.close();
        }catch (Exception e){
            e.printStackTrace();
            LitePalDb.setZkysDataDb();
            UpVideoInfoDao infoDao=new UpVideoInfoDao();
            infoDao.setVideoName("异常数据");
            infoDao.save();
        }
    }


    /**
     *  插入广告数据
     */
    public static void insertAdvertListDb(final List<AdverNewBean> dataList) throws Exception {
        LogUtil.i(TAG,"数据插入开始时间："+System.currentTimeMillis());
        ExecutorService service=Executors.newSingleThreadExecutor();
        service.execute(new Runnable() {
            @Override
            public void run() {
                try{
                    for (AdverNewBean adverNewBean : dataList) {
                        List<AdvertsBean> adverts = adverNewBean.getAdverts();
                        for (AdvertsBean adBean: adverts) {
                            AdvertsCodeDao advertsCodeDao=new AdvertsCodeDao();
                            advertsCodeDao.setCode(adverNewBean.getCode());
                            advertsCodeDao.setAdid(adBean.getId());
                            String linkContent = adBean.getLinkContent();
                            String resourceUrl = adBean.getResourceUrl();
                            String name = adBean.getName();
                            String advertType = adBean.getAdvertType();
                            String additionUrl = adBean.getAdditionUrl();
                            String taskUrl = adBean.getTaskUrl();
                            if(null==linkContent){
                                linkContent="";
                            }
                            if(null==resourceUrl){
                                resourceUrl="";
                            }
                            if(null==name){
                                name="";
                            }
                            if(null==advertType){
                                advertType="";
                            }
                            if(null==additionUrl){
                                additionUrl="";
                            }
                            if(null==taskUrl){
                                taskUrl="";
                            }
                            advertsCodeDao.setTaskType(adBean.getTaskType());
                            advertsCodeDao.setCloseType(adBean.getCloseType());
                            advertsCodeDao.setSecond(adBean.getSecond());
                            advertsCodeDao.setStatus(adBean.getStatus());
                            advertsCodeDao.setLinkType(adBean.getLinkType());
                            advertsCodeDao.setLinkContent(linkContent);
                            advertsCodeDao.setResourceUrl(resourceUrl);
                            advertsCodeDao.setName(name);
                            advertsCodeDao.setAdvertType(advertType);
                            advertsCodeDao.setAdditionUrl(additionUrl);
                            advertsCodeDao.setTaskUrl(taskUrl);
                            advertsCodeDao.saveDb(advertsCodeDao);
                        }
                    }
                    EventBus.getDefault().post(new GetAdvertEvent());
                    LogUtil.i(TAG,"数据插入结束时间："+System.currentTimeMillis());
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }


    /**
     *  插入科室数据
     */
    public static void insertEncyInfoDb(final String dbPath, final String tableName, final long createTime, final int count) throws Exception {
        LogUtil.i(TAG,"数据插入开始时间："+System.currentTimeMillis());
        EventBus.getDefault().post(new StartCheckDataEvent(StartCheckDataEvent.Status.HOSPITAL_ENCY_FIRST_DB_START));
        ExecutorService service=Executors.newSingleThreadExecutor();
        service.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    LitePalDb.setZkysDb();
                    LitePal.deleteAll(InfoDao.class);
                    final SQLiteDatabase database=getDataBase(dbPath);
                    final Cursor cursor=database.query(tableName,null,null,null,null,null,null);
                    ArrayList<InfoDao> infoDaos=new ArrayList<>();
                    while (cursor.moveToNext()){
                        InfoDao dao=new InfoDao();
                        dao.setColumnId(cursor.getInt(cursor.getColumnIndex("id")));
                        dao.setName(cursor.getString(cursor.getColumnIndex("name")));
                        dao.setIsDel(cursor.getInt(cursor.getColumnIndex("isDel")));
//                        dao.saveDb(dao);
                        infoDaos.add(dao);
                    }
                    EventBus.getDefault().post(new StartCheckDataEvent(StartCheckDataEvent.Status.ENCY_KS_DATA_SAVE));
                    LitePal.saveAllAsync(infoDaos).listen(new SaveCallback() {
                        @Override
                        public void onFinish(boolean success) {
                            if(success){
                                EventBus.getDefault().post(new StartCheckDataEvent(StartCheckDataEvent.Status.HOSPITAL_ENCY_FIRST_DB_END));
                                try {
                                    insertEncyInfoMationDb(dbPath,"medical_encyclopedia",createTime,count);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                LogUtil.i(TAG,"数据插入结束时间："+System.currentTimeMillis());
                            }else {
                                EventBus.getDefault().post(new StartCheckDataEvent(StartCheckDataEvent.Status.ENCY_KS_DATA_SAVE_FAIL));
                            }
                            cursor.close();
                            database.close();
                        }
                    });

                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }


    /**
     *  插入病例数据
     * @param dbPath
     * @param tableName
     */
    public static void insertEncyInfoMationDb(final String dbPath, final String tableName, final long createTime, final int count) throws Exception {
        LogUtil.i(TAG,"数据插入开始时间："+System.currentTimeMillis());
        EventBus.getDefault().post(new StartCheckDataEvent(StartCheckDataEvent.Status.HOSPITAL_ENCY_TWO_DB_START));
        final int[] num = {0};
        EventBus.getDefault().post(new StartCheckDataEvent(StartCheckDataEvent.Status.HOSPITAL_ENCY_TWO_DB_PROGRESS,num[0]+"/"+count));
        ExecutorService service=Executors.newSingleThreadExecutor();
        service.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    LitePalDb.setZkysDb();
                    LitePal.deleteAll(InfomationDao.class);
                    final SQLiteDatabase database=getDataBase(dbPath);
                    ArrayList<InfomationDao> infomationDaos=new ArrayList<>();
                    final Cursor cursor=database.query(tableName,null,null,null,null,null,null);
                    while (cursor.moveToNext()){
                        num[0]++;
                        EventBus.getDefault().post(new StartCheckDataEvent(StartCheckDataEvent.Status.HOSPITAL_ENCY_TWO_DB_PROGRESS,num[0]+"/"+count));
                        InfomationDao dao=new InfomationDao();
                        String source = cursor.getString(cursor.getColumnIndex("source"));
                        String title = cursor.getString(cursor.getColumnIndex("title"));
                        String summary = cursor.getString(cursor.getColumnIndex("summary"));
                        String cause = cursor.getString(cursor.getColumnIndex("cause"));
                        String check = cursor.getString(cursor.getColumnIndex("check"));
                        String diacrsis = cursor.getString(cursor.getColumnIndex("diacrisis"));
                        String antidiastole = cursor.getString(cursor.getColumnIndex("antidiastole"));
                        String cure = cursor.getString(cursor.getColumnIndex("cure"));
                        String prognosis = cursor.getString(cursor.getColumnIndex("prognosis"));
                        String prophylaxis = cursor.getString(cursor.getColumnIndex("prophylaxis"));
                        String complicatingDisease = cursor.getString(cursor.getColumnIndex("complicatingDisease"));
                        String author = cursor.getString(cursor.getColumnIndex("author"));
                        int columnid = cursor.getInt(cursor.getColumnIndex("columnId"));
                        String classification = cursor.getString(cursor.getColumnIndex("classification"));
                        String clinicalManifestation = cursor.getString(cursor.getColumnIndex
                                ("clinicalManifestation"));
                        String dietCare = cursor.getString(cursor.getColumnIndex("dietCare"));
                        String tag = cursor.getString(cursor.getColumnIndex("tag"));
                        int clickCount = cursor.getInt(cursor.getColumnIndex("clickCount"));
                        if (null == title) {
                            title = "";
                        }
                        if (null == source) {
                            source = "";
                        }
                        if (null == summary) {
                            summary = "";
                        }
                        if (null == cause) {
                            cause = "";
                        }
                        if (null == check) {
                            check = "";
                        }
                        if (null == diacrsis) {
                            diacrsis = "";
                        }
                        if (null == antidiastole) {
                            antidiastole = "";
                        }
                        if (null == cure) {
                            cure = "";
                        }
                        if (null == prognosis) {
                            prognosis = "";
                        }
                        if (null == prophylaxis) {
                            prophylaxis = "";
                        }
                        if (null == complicatingDisease) {
                            complicatingDisease = "";
                        }
                        if (null == author) {
                            author = "";
                        }
                        if (null == classification) {
                            classification = "";
                        }
                        if (null == clinicalManifestation) {
                            clinicalManifestation = "";
                        }
                        if (null == dietCare) {
                            dietCare = "";
                        }
                        if (null == tag) {
                            tag = "";
                        }
                        dao.setTitle(title);
                        dao.setSource(source);
                        dao.setSummary(summary);
                        dao.setCause(cause);
                        dao.setCheck(check);
                        dao.setDiacrsis(diacrsis);
                        dao.setAntidiastole(antidiastole);
                        dao.setCure(cure);
                        dao.setPrognosis(prognosis);
                        dao.setProphylaxis(prophylaxis);
                        dao.setComplicatingDisease(complicatingDisease);
                        dao.setAuthor(author);
                        dao.setColumnid(columnid);
                        dao.setClickCount(clickCount);
                        dao.setTag(tag);
                        dao.setDassification(classification);
                        dao.setClinicalManifestation(clinicalManifestation);
                        dao.setDietCare(dietCare);
//                        dao.saveDb(dao);
                        infomationDaos.add(dao);
                    }
                    EventBus.getDefault().post(new StartCheckDataEvent(StartCheckDataEvent.Status.ENCY_DATA_SAVE));
                    LitePal.saveAllAsync(infomationDaos).listen(new SaveCallback() {
                        @Override
                        public void onFinish(boolean success) {
                            LogFactory.l().i("success==="+success);
                            if(success){
                                SPUtil.putLong(Constants.SP_ENCY_UPDATE_TIME, createTime / 1000);
                                EventBus.getDefault().post(new StartCheckDataEvent(StartCheckDataEvent.Status.HOSPITAL_ENCY_SUCCESS));
                                LogUtil.i(TAG,"数据插入结束时间："+System.currentTimeMillis());
                            }else {
                                EventBus.getDefault().post(new StartCheckDataEvent(StartCheckDataEvent.Status.ENCY_DATA_SAVE_FAIL));
                            }
                            cursor.close();
                            database.close();

                        }
                    });
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     *  清除表数据
     * @param dbPath
     * @param table
     */
    public static void clearTable(String dbPath,String table) throws Exception{
        SQLiteDatabase database=getDataBase(dbPath);
        database.delete(table,null,null);
    }
}
