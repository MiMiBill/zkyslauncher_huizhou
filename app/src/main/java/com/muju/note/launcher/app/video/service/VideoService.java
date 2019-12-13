package com.muju.note.launcher.app.video.service;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.FileCallback;
import com.lzy.okgo.model.HttpParams;
import com.lzy.okgo.model.Progress;
import com.lzy.okgo.model.Response;
import com.muju.note.launcher.app.startUp.event.StartCheckDataEvent;
import com.muju.note.launcher.app.video.bean.VideoDownLoadBean;
import com.muju.note.launcher.app.video.db.VideoColumnsDao;
import com.muju.note.launcher.app.video.db.VideoHisDao;
import com.muju.note.launcher.app.video.db.VideoInfoDao;
import com.muju.note.launcher.app.video.db.VideoInfoTopDao;
import com.muju.note.launcher.app.video.db.VideoPlayerCountDao;
import com.muju.note.launcher.app.video.db.VideoTagSubDao;
import com.muju.note.launcher.app.video.db.VideoTagsDao;
import com.muju.note.launcher.app.video.util.DbHelper;
import com.muju.note.launcher.base.LauncherApplication;
import com.muju.note.launcher.litepal.LitePalDb;
import com.muju.note.launcher.litepal.UpVideoInfoDao;
import com.muju.note.launcher.okgo.BaseBean;
import com.muju.note.launcher.okgo.JsonCallback;
import com.muju.note.launcher.topics.SpTopics;
import com.muju.note.launcher.url.UrlUtil;
import com.muju.note.launcher.util.ActiveUtils;
import com.muju.note.launcher.util.app.MobileInfoUtil;
import com.muju.note.launcher.util.log.LogUtil;
import com.muju.note.launcher.util.sp.SPUtil;

import org.greenrobot.eventbus.EventBus;
import org.litepal.LitePal;
import org.litepal.crud.callback.CountCallback;
import org.litepal.crud.callback.FindCallback;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class VideoService {

    private static final String TAG=VideoService.class.getSimpleName();

    public static VideoService videoService=null;

    public static VideoService getInstance(){
        if(videoService==null){
            videoService=new VideoService();
        }
        return videoService;
    }

    public void start(){
        LitePalDb.setZkysDb();
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
     *  加载影视分类数据
     */
    public void startColumns(){
        EventBus.getDefault().post(new StartCheckDataEvent(StartCheckDataEvent.Status.VIDEO_COLUMN_START));
        LitePal.countAsync(VideoColumnsDao.class).listen(new CountCallback() {
            @Override
            public void onFinish(int count) {
                if(count<=0){
                    getVideoCloumns();
                }else {
                    EventBus.getDefault().post(new StartCheckDataEvent(StartCheckDataEvent.Status.VIDEO_COLUMN_SUCCESS));
                }
            }
        });
    }

    /**
     *  加载影视首页推荐数据
     */
    public void startVideoTop(){
        EventBus.getDefault().post(new StartCheckDataEvent(StartCheckDataEvent.Status.VIDEO_TOP_START));
        LitePal.countAsync(VideoInfoTopDao.class).listen(new CountCallback() {
            @Override
            public void onFinish(int count) {
                if(count<=0){
                    getVideoTopInfo();
                }else {
                    EventBus.getDefault().post(new StartCheckDataEvent(StartCheckDataEvent.Status.VIDEO_TOP_SUCCESS));
                }
            }
        });
    }

    /**
     *  加载影视分类，并界面展示
     */
    public void startVideoInfo(){
        EventBus.getDefault().post(new StartCheckDataEvent(StartCheckDataEvent.Status.VIDEO_INFO_START));
        LitePal.countAsync(VideoInfoDao.class).listen(new CountCallback() {
            @Override
            public void onFinish(int count) {
                if(count<10000){
                    queryVideo();
                }else {
                    EventBus.getDefault().post(new StartCheckDataEvent(StartCheckDataEvent.Status.VIDEO_INFO_SUCCESS));
                }
            }
        });
    }


    /**
     *  查询视频本地化信息
     */
    public void queryVideo(){
        EventBus.getDefault().post(new StartCheckDataEvent(StartCheckDataEvent.Status.VIDEO_INFO_HTTP_START));
        OkGo.<BaseBean<VideoDownLoadBean>>get(UrlUtil.getVideoDownLoadUrl())
                .execute(new JsonCallback<BaseBean<VideoDownLoadBean>>() {
                    @Override
                    public void onSuccess(Response<BaseBean<VideoDownLoadBean>> response) {
                        try{
                            if(response.body().getData()==null){
                                EventBus.getDefault().post(new StartCheckDataEvent(StartCheckDataEvent.Status.VIDEO_INFO_HTTP_DATA_NULL));
                                return;
                            }
                            downVideoDb(response.body().getData().getPath(),response.body().getData().getTableName(),response.body().getData().getCount(),response.body().getData().getCreateDate());
                        }catch (Exception e){
                            e.printStackTrace();
                            EventBus.getDefault().post(new StartCheckDataEvent(StartCheckDataEvent.Status.VIDEO_INFO_CARSH,e));
                        }
                    }

                    @Override
                    public void onError(Response<BaseBean<VideoDownLoadBean>> response) {
                        super.onError(response);
                        EventBus.getDefault().post(new StartCheckDataEvent(StartCheckDataEvent.Status.VIDEO_INFO_HTTP_FAIL));
                    }
                });
    }

    /**
     *  下载数据库文件
     * @param url
     * @param tableName
     */
    private void downVideoDb(String url, final String tableName, final int count, final long crateTime){
        EventBus.getDefault().post(new StartCheckDataEvent(StartCheckDataEvent.Status.VIDEO_INFO_DOWNLOAD_START));
        OkGo.<File>get(url)
                .execute(new FileCallback("/sdcard/zkysdb/","video.db") {
                    @Override
                    public void onSuccess(Response<File> response) {
                        insertVideoDb(response.body().getPath(),tableName,count,crateTime);
                    }

                    @Override
                    public void downloadProgress(Progress progress) {
                        super.downloadProgress(progress);
                        int pro=(int)(progress.fraction*100);
                        EventBus.getDefault().post(new StartCheckDataEvent(StartCheckDataEvent.Status.VIDEO_INFO_DOWNLOAD_PROGRESS,pro));
                    }

                    @Override
                    public void onError(Response<File> response) {
                        super.onError(response);
                        EventBus.getDefault().post(new StartCheckDataEvent(StartCheckDataEvent.Status.VIDEO_INFO_DOWNLOAD_FAIL));
                    }
                });
    }

    /**
     *  插入影视数据
     * @param path
     * @param table
     */
    private void insertVideoDb(String path, String table,int count,long crateTime){
        try {
            DbHelper.insertToVideo(path,table,count,crateTime);
        } catch (Exception e) {
            e.printStackTrace();
            EventBus.getDefault().post(new StartCheckDataEvent(StartCheckDataEvent.Status.VIDEO_INFO_CARSH,e));
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
                               for (VideoInfoDao dao:response.body().getData()){
                                   LitePalDb.setZkysDb();
                                   VideoInfoDao videoInfoDao=LitePal.where("cid = ?",dao.getCid()+"").findFirst(VideoInfoDao.class);
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
        EventBus.getDefault().post(new StartCheckDataEvent(StartCheckDataEvent.Status.VIDEO_COLUMN_HTTP_START));
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
                            EventBus.getDefault().post(new StartCheckDataEvent(StartCheckDataEvent.Status.VIDEO_COLUMN_HTTP_DATA_NULL));
                            return;
                        }
                        EventBus.getDefault().post(new StartCheckDataEvent(StartCheckDataEvent.Status.VIDEO_COLUMN_DB_START));
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
                                EventBus.getDefault().post(new StartCheckDataEvent(StartCheckDataEvent.Status.VIDEO_COLUMN_SUCCESS));
                            }
                        });
                    }

                    @Override
                    public void onError(Response<BaseBean<List<VideoColumnsDao>>> response) {
                        super.onError(response);
                        EventBus.getDefault().post(new StartCheckDataEvent(StartCheckDataEvent.Status.VIDEO_COLUMN_HTTP_FAIL));
                    }
                });
    }

    /**
     *  更新分类
     */
    private void setColumns(VideoColumnsDao dao){
        LitePalDb.setZkysDb();
        dao.setColumnsId(dao.getId());
        dao.save();
    }

    /**
     *  更新标签分类
     */
    private void setTags(VideoTagsDao dao,int columnsId){
        LitePalDb.setZkysDb();
        dao.setTagId(dao.getId());
        dao.setCoulmnsId(columnsId);
        dao.save();
    }

    /**
     *  更新标签子分类
     */
    private void setTagsSub(VideoTagSubDao dao){
        LitePalDb.setZkysDb();
        dao.setSubId(dao.getId());
        dao.save();
    }

    /**
     *  获取首页数据
     */
    public void getVideoTopInfo(){
        EventBus.getDefault().post(new StartCheckDataEvent(StartCheckDataEvent.Status.VIDEO_TOP_HTTP_START));
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
                            EventBus.getDefault().post(new StartCheckDataEvent(StartCheckDataEvent.Status.VIDEO_TOP_HTTP_DATA_NULL));
                            return;
                        }
                        EventBus.getDefault().post(new StartCheckDataEvent(StartCheckDataEvent.Status.VIDEO_TOP_DB_START));
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
                                EventBus.getDefault().post(new StartCheckDataEvent(StartCheckDataEvent.Status.VIDEO_TOP_SUCCESS));
                            }
                        });
                    }

                    @Override
                    public void onError(Response<BaseBean<List<VideoInfoTopDao>>> response) {
                        super.onError(response);
                        EventBus.getDefault().post(new StartCheckDataEvent(StartCheckDataEvent.Status.VIDEO_TOP_HTTP_FAIL));
                    }
                });
    }


    /**
     *  添加播放count数据
     * @param vid
     * @param vName
     * @param startTime
     * @param endTime
     */
    public void addVideoCount(final String vid, final String cid, final String vName, final long startTime, final long endTime){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date d = new Date(System.currentTimeMillis());
        final String date = format.format(d);
        LitePalDb.setZkysDb();
        LitePal.where("cid = ? and date = ?",cid+"",date).findFirstAsync(VideoPlayerCountDao.class).listen(new FindCallback<VideoPlayerCountDao>() {
            @Override
            public void onFinish(VideoPlayerCountDao videoPlayerCountDao) {
                try {
                    LitePalDb.setZkysDb();
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
                        countDao.setCid(cid);
                        countDao.save();
                    }else {
                        long time=videoPlayerCountDao.getPlayTime()+(endTime-startTime);
                        videoPlayerCountDao.setPlayTime(time);
                        videoPlayerCountDao.setPlayCount(videoPlayerCountDao.getPlayCount()+1);
                        videoPlayerCountDao.update(videoPlayerCountDao.getId());
                    }
                }catch (Exception e){
                    e.printStackTrace();
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
    public void addVideoInfoDb(final String vid,String cid, final String vName, final long startTime, final long endTime){
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:dd");
            Date d = new Date(System.currentTimeMillis());
            final String date = format.format(d);
            LitePalDb.setZkysDb();
            UpVideoInfoDao countDao=new UpVideoInfoDao();
            countDao.setDate(date);
            countDao.setDepId(ActiveUtils.getPadActiveInfo().getDeptId());
            countDao.setHosId(ActiveUtils.getPadActiveInfo().getHospitalId());
            countDao.setImei(MobileInfoUtil.getIMEI(LauncherApplication.getContext()));
            countDao.setStartTime(startTime);
            countDao.setEndTime(endTime);
            countDao.setVideoId(vid);
            countDao.setVideoName(vName);
            countDao.setCid(cid);
//        countDao.save();
            DbHelper.insertToVideoData(LitePalDb.DBNAME_ZKYS_DATA,countDao);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     *  添加
     * @param dao
     */
    public void addVideoHisInfo(final VideoHisDao dao){
        LitePalDb.setZkysDb();
        LitePal.where("cid=?",dao.getCid()+"").findFirstAsync(VideoHisDao.class).listen(new FindCallback<VideoHisDao>() {
            @Override
            public void onFinish(VideoHisDao videoHisDao) {
                try {
                    if(videoHisDao==null){
                        dao.save();
                        return;
                    }
                    LitePalDb.setZkysDb();
                    videoHisDao.setCreateTime(dao.getCreateTime());
                    videoHisDao.setDuration(dao.getDuration());
                    videoHisDao.update(videoHisDao.getId());
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }

}
