package com.muju.note.launcher.app.clide.presenter;

import com.muju.note.launcher.app.clide.contract.ClideContract;
import com.muju.note.launcher.app.video.db.VideoInfoDao;
import com.muju.note.launcher.base.BasePresenter;
import com.muju.note.launcher.litepal.LitePalDb;
import com.muju.note.launcher.util.log.LogUtil;

import org.litepal.LitePal;
import org.litepal.crud.callback.FindMultiCallback;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ClidePresenter extends BasePresenter<ClideContract.View> implements ClideContract.Presenter {

    /**
     *  查询儿童相关影视
     */
    @Override
    public void getCilde(String name , int pageNum) {
        LitePalDb.setZkysDb();
        String sql="columnName like '%"+name+"%' and status = 1 order by number desc,onwayTime desc,editTime desc,updateTime desc";
        int limit=(pageNum*30)-30;
        LitePal.where(sql).limit(30).offset(limit).findAsync(VideoInfoDao.class).listen(new FindMultiCallback<VideoInfoDao>() {
            @Override
            public void onFinish(List<VideoInfoDao> list) {
                if(mView==null){
                    LogUtil.e("mView为空");
                    return;
                }
                if(list==null||list.size()<=0){
                    mView.getClideNull();
                    return;
                }
                mView.getClideSuccess(list);
                if(list.size()>0&&list.size()<30){
                    mView.getClideEnd();
                }
            }
        });

    }

    @Override
    public void getHeader(String name) {
        LitePalDb.setZkysDb();
        String sql="columnName like '%"+name+"%'";
        LitePal.where(sql).findAsync(VideoInfoDao.class).listen(new FindMultiCallback<VideoInfoDao>() {
            @Override
            public void onFinish(List<VideoInfoDao> list) {
                if(mView==null){
                    LogUtil.e("mView为空");
                    return;
                }
                if(list==null||list.size()<=0){
                    mView.getClideNull();
                    return;
                }
                getHeaderNum(list.size(),list);
            }
        });
    }

    private void getHeaderNum(int count,List<VideoInfoDao> list){
        List<VideoInfoDao> numList=new ArrayList<>();
        for (int i=0;i<6;i++){
            int num=new Random().nextInt(count+1);
            numList.add(list.get(num));
        }
        if(mView==null){
            LogUtil.e("mView为空");
            return;
        }
        mView.getHeaderSuccess(numList);
    }
}
