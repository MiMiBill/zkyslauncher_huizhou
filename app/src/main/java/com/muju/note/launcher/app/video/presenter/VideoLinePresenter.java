package com.muju.note.launcher.app.video.presenter;

import com.muju.note.launcher.app.video.contract.VideoLineContract;
import com.muju.note.launcher.app.video.db.VideoInfoDao;
import com.muju.note.launcher.base.BasePresenter;
import com.muju.note.launcher.litepal.LitePalDb;
import com.muju.note.launcher.util.log.LogUtil;

import org.litepal.LitePal;
import org.litepal.crud.callback.FindMultiCallback;

import java.util.List;

public class VideoLinePresenter extends BasePresenter<VideoLineContract.View> implements VideoLineContract.Presenter {


    /**
     *  查询电视直播
     */
    @Override
    public void queryVideo() {
        LitePalDb.setZkysDb();
        String sql;
        sql="columnName like '%"+"电视"+"%' and status = 1 order by number desc,onwayTime desc,editTime desc,updateTime desc";
        LitePal.where(sql).findAsync(VideoInfoDao.class).listen(new FindMultiCallback<VideoInfoDao>() {
            @Override
            public void onFinish(List<VideoInfoDao> list) {
                if(mView==null){
                    LogUtil.e("mView为空");
                    return;
                }
                if(list==null||list.size()<=0){
                    mView.getVideoNull();
                    return;
                }
                mView.getVideoSuccess(list);
            }
        });

    }
}
