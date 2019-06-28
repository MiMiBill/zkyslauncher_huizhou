package com.muju.note.launcher.app.video.presenter;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.muju.note.launcher.app.video.contract.VideoContentContract;
import com.muju.note.launcher.app.video.contract.VideoLineContract;
import com.muju.note.launcher.app.video.db.VideoInfoDao;
import com.muju.note.launcher.app.video.db.VideoInfoTopDao;
import com.muju.note.launcher.app.video.db.VideoTagSubDao;
import com.muju.note.launcher.app.video.db.VideoTagsDao;
import com.muju.note.launcher.app.video.ui.VideoContentFragment;
import com.muju.note.launcher.base.BasePresenter;
import com.muju.note.launcher.litepal.LitePalDb;
import com.muju.note.launcher.util.log.LogUtil;
import com.orhanobut.logger.Logger;

import org.litepal.LitePal;
import org.litepal.crud.callback.FindMultiCallback;

import java.util.ArrayList;
import java.util.List;

public class VideoLinePresenter extends BasePresenter<VideoLineContract.View> implements VideoLineContract.Presenter {


    /**
     *  查询电视直播
     */
    @Override
    public void queryVideo() {
        String sql;
        sql="columnName like '%"+"电视"+"%' and status = 1 order by number desc,onwayTime desc,editTime desc,updateTime desc";
        LitePal.where(sql).findAsync(VideoInfoDao.class).listen(new FindMultiCallback<VideoInfoDao>() {
            @Override
            public void onFinish(List<VideoInfoDao> list) {
                if(list==null||list.size()<=0){
                    mView.getVideoNull();
                    return;
                }
                mView.getVideoSuccess(list);
            }
        });

    }
}
