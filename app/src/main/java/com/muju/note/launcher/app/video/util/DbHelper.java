package com.muju.note.launcher.app.video.util;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;


import com.muju.note.launcher.app.home.db.AdvertsInfoDao;
import com.muju.note.launcher.app.video.db.VideoInfoDao;
import com.muju.note.launcher.litepal.LitePalDb;
import com.muju.note.launcher.litepal.UpAdvertInfoDao;
import com.muju.note.launcher.litepal.UpVideoInfoDao;
import com.muju.note.launcher.topics.SpTopics;
import com.muju.note.launcher.util.log.LogUtil;
import com.muju.note.launcher.util.sp.SPUtil;

import org.litepal.LitePal;
import org.litepal.crud.callback.FindMultiCallback;

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
     * @throws Exception
     */
    public static void insertToVideo(final String dbPath, final String tableName) throws Exception {
        LogUtil.i(TAG,"数据插入开始时间："+System.currentTimeMillis());
        ExecutorService service=Executors.newSingleThreadExecutor();
        service.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    SQLiteDatabase database=getDataBase(dbPath);
                    Cursor cursor=database.query(tableName,null,null,null,null,null,null);
                    while (cursor.moveToNext()){
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
                        dao.saveDb(dao);
                    }
                    cursor.close();
                    database.close();
                    SPUtil.putLong(SpTopics.SP_VIDEO_UPDATE_TIME,(System.currentTimeMillis()/1000));
                    LogUtil.i(TAG,"数据插入结束时间："+System.currentTimeMillis());
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     *  插入广告详情数据
     * @param dbPath
     * @param dao
     * @throws Exception
     */
    public static void insertToAdvertData(String dbPath, UpAdvertInfoDao dao) throws Exception{
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
    }

    /**
     *  插入影视统计数据
     * @param dbPath
     * @param dao
     * @throws Exception
     */
    public static void insertToVideoData(String dbPath, UpVideoInfoDao dao) throws Exception{
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
