package com.muju.note.launcher.app.video.presenter;

import android.text.TextUtils;

import com.muju.note.launcher.app.video.contract.VideoContentContract;
import com.muju.note.launcher.app.video.contract.VideoContract;
import com.muju.note.launcher.app.video.db.VideoColumnsDao;
import com.muju.note.launcher.app.video.db.VideoInfoDao;
import com.muju.note.launcher.app.video.db.VideoInfoTopDao;
import com.muju.note.launcher.base.BasePresenter;
import com.muju.note.launcher.litepal.LitePalDb;

import org.litepal.LitePal;
import org.litepal.crud.callback.FindMultiCallback;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class VideoContentPresenter extends BasePresenter<VideoContentContract.View> implements VideoContentContract.Presenter {

    /**
     *  查询影视列表
     * @param columnId
     * @param name
     * @param pageNum
     */
    @Override
    public void queryVideo(int columnId,String name,int pageNum) {
        queryVideo(columnId,name,pageNum,"");
    }

    /**
     *  根据标签查询影视列表
     * @param columnId
     * @param name
     * @param pageNum
     * @param mapFilter
     */
    @Override
    public void queryVideo(int columnId, String name, int pageNum, String mapFilter) {
        LitePalDb.setZkysDb();
        if(columnId==1){
            LitePal.findAllAsync(VideoInfoTopDao.class).listen(new FindMultiCallback<VideoInfoTopDao>() {
                @Override
                public void onFinish(List<VideoInfoTopDao> list) {
                    if(list==null||list.size()<=0){
                        mView.getVideoNull();
                        return;
                    }
                    List<VideoInfoDao> topDaos=new ArrayList<>();
                    for (VideoInfoTopDao dao:list){
                        VideoInfoDao infoDao=new VideoInfoDao();
                        infoDao.setImgUrl(dao.getImgUrl());
                        infoDao.setColumnId(dao.getColumnId());
                        infoDao.setVideoType(dao.getVideoType());
                        infoDao.setScreenUrl(dao.getScreenUrl());
                        infoDao.setName(dao.getName());
                        infoDao.setDescription(dao.getDescription());
                        infoDao.setEditTime(dao.getEditTime());
                        infoDao.setCid(dao.getCid());
                        infoDao.setWatchCount(dao.getWatchCount());
                        topDaos.add(infoDao);
                    }
                    mView.getVideoSuccess(topDaos);
                    if(list.size()>0&&list.size()<30){
                        mView.getVideoend();
                    }
                }
            });
        }else {
            String sql;
            int limit=(pageNum*30)-30;
            if(TextUtils.isEmpty(mapFilter)||"".equals(mapFilter)){
                sql="columnName like '%"+name+"%' and status = 1";
            }else {
                sql="columnName like '%"+name+"%' and status = 1 and customtag like '%"+mapFilter+"%'";
            }
            LitePal.where(sql).limit(30).offset(limit).findAsync(VideoInfoDao.class).listen(new FindMultiCallback<VideoInfoDao>() {
                @Override
                public void onFinish(List<VideoInfoDao> list) {
                    if(list==null||list.size()<=0){
                        mView.getVideoNull();
                        return;
                    }
                    mView.getVideoSuccess(list);
                    if(list.size()>0&&list.size()<30){
                        mView.getVideoend();
                    }
                }
            });
        }
    }
}
