package com.muju.note.launcher.app.video.service;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.FileCallback;
import com.lzy.okgo.db.CacheManager;
import com.lzy.okgo.model.HttpParams;
import com.lzy.okgo.model.Response;
import com.muju.note.launcher.app.video.bean.VideoDownLoadBean;
import com.muju.note.launcher.app.video.db.VideoColumnsDao;
import com.muju.note.launcher.app.video.db.VideoInfoDao;
import com.muju.note.launcher.app.video.db.VideoInfoTopDao;
import com.muju.note.launcher.app.video.db.VideoPlayerCountDao;
import com.muju.note.launcher.app.video.db.VideoPlayerInfoDao;
import com.muju.note.launcher.app.video.db.VideoTagSubDao;
import com.muju.note.launcher.app.video.db.VideoTagsDao;
import com.muju.note.launcher.app.video.util.DbHelper;
import com.muju.note.launcher.base.LauncherApplication;
import com.muju.note.launcher.litepal.LitePalDb;
import com.muju.note.launcher.okgo.BaseBean;
import com.muju.note.launcher.okgo.JsonCallback;
import com.muju.note.launcher.topics.SpTopics;
import com.muju.note.launcher.url.UrlUtil;
import com.muju.note.launcher.util.ActiveUtils;
import com.muju.note.launcher.util.Constants;
import com.muju.note.launcher.util.app.MobileInfoUtil;
import com.muju.note.launcher.util.sign.Signature;
import com.muju.note.launcher.util.sp.SPUtil;

import org.litepal.LitePal;
import org.litepal.crud.callback.CountCallback;
import org.litepal.crud.callback.FindCallback;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class VideoService {

    public static VideoService videoService=null;

    public static VideoService getInstance(){
        if(videoService==null){
            videoService=new VideoService();
        }
        return videoService;
    }

    private boolean isUpdate=false;

    public void start(){
        LitePal.countAsync(VideoInfoDao.class).listen(new CountCallback() {
            @Override
            public void onFinish(int count) {
                if(count<=0){
                    queryVideo();
                }
            }
        });

        LitePal.countAsync(VideoColumnsDao.class).listen(new CountCallback() {
            @Override
            public void onFinish(int count) {
                if(count<=0){
                    getVideoCloumns();
                }
            }
        });

        LitePal.countAsync(VideoInfoTopDao.class).listen(new CountCallback() {
            @Override
            public void onFinish(int count) {
                if(count<=0){
                    getVideoTopInfo();
                }
            }
        });
    }

    /**
     *  查询视频本地化信息
     */
    public void queryVideo(){
        OkGo.<BaseBean<VideoDownLoadBean>>get(UrlUtil.getVideoDownLoadUrl())
                .execute(new JsonCallback<BaseBean<VideoDownLoadBean>>() {
                    @Override
                    public void onSuccess(Response<BaseBean<VideoDownLoadBean>> response) {
                        downVideoDb(response.body().getData().getPath(),response.body().getData().getTableName());
                    }
                });
    }

    /**
     *  下载数据库文件
     * @param url
     * @param tableName
     */
    private void downVideoDb(String url, final String tableName){
        OkGo.<File>get(url)
                .execute(new FileCallback() {
                    @Override
                    public void onSuccess(Response<File> response) {
                        insertVideoDb(response.body().getPath(),tableName);
                    }
                });
    }

    /**
     *  插入影视数据
     * @param path
     * @param table
     */
    private void insertVideoDb(String path, String table){
        try {
            DbHelper.insertToVideo(path,table);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     *  获取影视更新的内容
     */
    public void getUpdateVideo(){
        HttpParams params=new HttpParams();
        params.put("timeStamp",SPUtil.getLong(SpTopics.SP_VIDEO_UPDATE_TIME));
        OkGo.<BaseBean<List<VideoInfoDao>>>post(UrlUtil.getVideoUpdate())
                .params(params)
                .execute(new JsonCallback<BaseBean<List<VideoInfoDao>>>() {
                    @Override
                    public void onSuccess(final Response<BaseBean<List<VideoInfoDao>>> response) {
                        ExecutorService service=Executors.newSingleThreadExecutor();
                        service.execute(new Runnable() {
                            @Override
                            public void run() {
                                LitePalDb.setZkysDb();
                               for (VideoInfoDao dao:response.body().getData()){
                                   VideoInfoDao videoInfoDao=LitePal.where("videoid = ?",dao.getId()+"").findFirst(VideoInfoDao.class);
                                   if(videoInfoDao==null){
                                       dao.setVideoId(dao.getId());
                                       dao.save();
                                   }else {
                                       LitePal.delete(VideoInfoDao.class,videoInfoDao.getId());
                                       dao.setVideoId(dao.getId());
                                       dao.save();
                                   }
                               }
                                SPUtil.putLong(SpTopics.SP_VIDEO_UPDATE_TIME,(System.currentTimeMillis()/1000));
                            }
                        });
                    }
                });

    }

    /**
     *  获取影视类型
     */
    public void getVideoCloumns(){
        Map<String, String> params = new HashMap();
        params.put("parentId", "" + 0);
        params.put("hospitalId",ActiveUtils.getPadActiveInfo().getHospitalId()+"");
        params.put("deptId",ActiveUtils.getPadActiveInfo().getDeptId()+"");
        OkGo.<BaseBean<List<VideoColumnsDao>>>get(UrlUtil.getVideoColumnsTags())
                .params(params)
                .tag(this)
                .execute(new JsonCallback<BaseBean<List<VideoColumnsDao>>>() {
                    @Override
                    public void onSuccess(final Response<BaseBean<List<VideoColumnsDao>>> response) {
                        if(response.body().getData()==null){
                            return;
                        }
                        ExecutorService service=Executors.newSingleThreadExecutor();
                        service.execute(new Runnable() {
                            @Override
                            public void run() {
                                LitePalDb.setZkysDb();
                                LitePal.deleteAll(VideoColumnsDao.class);
                                LitePal.deleteAll(VideoTagsDao.class);
                                LitePal.deleteAll(VideoTagSubDao.class);
                                for (VideoColumnsDao columnsDao:response.body().getData()){
                                    setColumns(columnsDao);
                                    for (VideoTagsDao tagsDao:columnsDao.getVideoTags()){
                                        setTags(tagsDao,columnsDao.getColumnsId());
                                        for (VideoTagSubDao listBean:tagsDao.getList()){
                                            setTagsSub(listBean);
                                        }
                                    }
                                }
                            }
                        });
                    }
                });
    }

    /**
     *  更新分类
     */
    private void setColumns(VideoColumnsDao dao){
//        VideoColumnsDao videoColumnsDao= LitePal.where("columnsId = ?",dao.getId()+"").findFirst(VideoColumnsDao.class);
//        if(videoColumnsDao==null){
//            videoColumnsDao=dao;
//            videoColumnsDao.setColumnsId(dao.getId());
//            videoColumnsDao.save();
//        }else {
//            videoColumnsDao.setColumnsId(dao.getId());
//            videoColumnsDao.setName(dao.getName());
//            videoColumnsDao.update(videoColumnsDao.getId());
//        }
        dao.setColumnsId(dao.getId());
        dao.save();
    }

    /**
     *  更新标签分类
     */
    private void setTags(VideoTagsDao dao,int columnsId){
//        VideoTagsDao tagsDao=LitePal.where("tagId = ?",dao.getId()+"").findFirst(VideoTagsDao.class);
//        if(tagsDao==null){
//            tagsDao=dao;
//            tagsDao.setTagId(dao.getId());
//            tagsDao.setCoulmnsId(columnsId);
//            tagsDao.save();
//        }else {
//            tagsDao.setName(dao.getName());
//            tagsDao.setTagId(dao.getId());
//            tagsDao.update(tagsDao.getId());
//        }
        dao.setTagId(dao.getId());
        dao.setCoulmnsId(columnsId);
        dao.save();
    }

    /**
     *  更新标签子分类
     */
    private void setTagsSub(VideoTagSubDao dao){
//        VideoTagSubDao subDao=LitePal.where("subId = ?",dao.getId()+"").findFirst(VideoTagSubDao.class);
//        if(subDao==null){
//            subDao=dao;
//            subDao.setSubId(dao.getId());
//            subDao.save();
//
//        }else {
//            subDao.setName(dao.getName());
//            subDao.setSubId(dao.getId());
//            subDao.update(subDao.getId());
//        }
        dao.setSubId(dao.getId());
        dao.save();
    }

    /**
     *  获取首页数据
     */
    public void getVideoTopInfo(){
        Map<String, String> params = new HashMap();
        params.put("columnId", "1");
        params.put("hospitalId",ActiveUtils.getPadActiveInfo().getHospitalId()+"");
        params.put("deptId",ActiveUtils.getPadActiveInfo().getDeptId()+"");
        params.put("pageNum","1");
        params.put("pageSize","30");
        OkGo.<BaseBean<List<VideoInfoTopDao>>>post(UrlUtil.getSerchVideo())
                .params(params)
                .tag(this)
                .execute(new JsonCallback<BaseBean<List<VideoInfoTopDao>>>() {
                    @Override
                    public void onSuccess(final Response<BaseBean<List<VideoInfoTopDao>>> response) {
                        if(response.body().getData()==null){
                            return;
                        }
                        ExecutorService service=Executors.newSingleThreadExecutor();
                        service.execute(new Runnable() {
                            @Override
                            public void run() {
                                if(LitePal.count(VideoInfoTopDao.class)>0){
                                    LitePal.deleteAll(VideoInfoTopDao.class);
                                }
                                for (VideoInfoTopDao dao:response.body().getData()){
                                    dao.setVideoId(dao.getId());
                                    LitePalDb.setZkysDb();
                                    dao.save();
                                }
                            }
                        });
                    }
                });
    }

    public void checkUpdateVideo(){
        int videoDay = SPUtil.getInt("UPDATE_VIDEO_DAY");
        Calendar c = Calendar.getInstance();
        final int day = c.get(Calendar.DAY_OF_MONTH);
        if (day != videoDay) {
            int hour = c.get(Calendar.HOUR_OF_DAY);
            if (hour >= 22 || hour <= 7) {
                if (isUpdate) {
                    return;
                }
                isUpdate = true;
                // 一个小时内随机获取
                int num = new Random().nextInt(59) + 1;
                new Timer().schedule(new TimerTask() {
                    @Override
                    public void run() {
                        // 更新影视相关信息
                        getUpdateVideo();
                        getVideoCloumns();
                        getVideoTopInfo();
                    }
                }, 1000 * 60 * num);
                new Timer().schedule(new TimerTask() {
                    @Override
                    public void run() {
                        // 以防万一，12个小时候重置状态
                        isUpdate=false;
                    }
                },1000*60*60*12);
            }
        }
    }

    /**
     *  添加播放count数据
     * @param vid
     * @param vName
     * @param startTime
     * @param endTime
     */
    public void addVideoCount(final String vid, final String vName, final long startTime, final long endTime){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date d = new Date(System.currentTimeMillis());
        final String date = format.format(d);
        LitePalDb.setZkysDb();
        LitePal.where("videoid = ? and date = ?",vid+"",date).findFirstAsync(VideoPlayerCountDao.class).listen(new FindCallback<VideoPlayerCountDao>() {
            @Override
            public void onFinish(VideoPlayerCountDao videoPlayerCountDao) {
                if(videoPlayerCountDao==null){
                    VideoPlayerCountDao countDao=new VideoPlayerCountDao();
                    countDao.setDate(date);
                    countDao.setDepId(ActiveUtils.getPadActiveInfo().getDeptId());
                    countDao.setHosId(ActiveUtils.getPadActiveInfo().getHospitalId());
                    countDao.setImei(MobileInfoUtil.getIMEI(LauncherApplication.getContext()));
                    countDao.setPlayCount(1);
                    countDao.setPlayTime((endTime-startTime));
                    countDao.setVideoId(vid);
                    countDao.setVideoName(vName);
                    countDao.save();
                }else {
                    long time=videoPlayerCountDao.getPlayTime()+(endTime-startTime);
                    videoPlayerCountDao.setPlayTime(time);
                    videoPlayerCountDao.setPlayCount(videoPlayerCountDao.getPlayCount()+1);
                    videoPlayerCountDao.update(videoPlayerCountDao.getId());
                }
            }
        });
    }

    /**
     *  添加影视播放详情数据
     * @param vid
     * @param vName
     * @param startTime
     * @param endTime
     */
    public void addVideoInfoDb(final String vid, final String vName, final long startTime, final long endTime){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:dd");
        Date d = new Date(System.currentTimeMillis());
        final String date = format.format(d);
        LitePalDb.setZkysDb();
        VideoPlayerInfoDao countDao=new VideoPlayerInfoDao();
        countDao.setDate(date);
        countDao.setDepId(ActiveUtils.getPadActiveInfo().getDeptId());
        countDao.setHosId(ActiveUtils.getPadActiveInfo().getHospitalId());
        countDao.setImei(MobileInfoUtil.getIMEI(LauncherApplication.getContext()));
        countDao.setStartTime(startTime);
        countDao.setEndTime(endTime);
        countDao.setVideoId(vid);
        countDao.setVideoName(vName);
        countDao.save();
    }

}
