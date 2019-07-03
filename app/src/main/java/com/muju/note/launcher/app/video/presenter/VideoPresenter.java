package com.muju.note.launcher.app.video.presenter;

import com.muju.note.launcher.app.video.contract.VideoContract;
import com.muju.note.launcher.app.video.db.VideoColumnsDao;
import com.muju.note.launcher.base.BasePresenter;
import com.muju.note.launcher.litepal.LitePalDb;
import com.muju.note.launcher.util.log.LogUtil;

import org.litepal.LitePal;
import org.litepal.crud.callback.FindMultiCallback;

import java.util.List;

public class VideoPresenter extends BasePresenter<VideoContract.View> implements VideoContract.Presenter {


    /**
     *  查询分类类型
     */
    @Override
    public void queryColumns() {
        LitePalDb.setZkysDb();
        LitePal.findAllAsync(VideoColumnsDao.class).listen(new FindMultiCallback<VideoColumnsDao>() {
            @Override
            public void onFinish(List<VideoColumnsDao> list) {
                if(mView==null){
                    LogUtil.e("mView为空");
                    return;
                }
                if(list==null||list.size()<=0){
                    mView.getColumnsNull();
                    return;
                }
                mView.getColumnsSuccess(list);
            }
        });
    }
}
