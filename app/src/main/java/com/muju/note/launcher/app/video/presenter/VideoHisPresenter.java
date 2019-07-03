package com.muju.note.launcher.app.video.presenter;

import com.muju.note.launcher.app.video.contract.VideoHisContract;
import com.muju.note.launcher.app.video.db.VideoHisDao;
import com.muju.note.launcher.base.BasePresenter;
import com.muju.note.launcher.util.log.LogUtil;

import org.litepal.LitePal;
import org.litepal.crud.callback.FindMultiCallback;

import java.util.List;

public class VideoHisPresenter extends BasePresenter<VideoHisContract.View> implements VideoHisContract.Presenter {

    /**
     *  查询播放记录
     */
    @Override
    public void queryHis() {
        LitePal.order("createTime desc").findAsync(VideoHisDao.class).listen(new FindMultiCallback<VideoHisDao>() {
            @Override
            public void onFinish(List<VideoHisDao> list) {
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
