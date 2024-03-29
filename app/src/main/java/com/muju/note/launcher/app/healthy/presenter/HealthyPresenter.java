package com.muju.note.launcher.app.healthy.presenter;

import com.muju.note.launcher.app.healthy.contract.HealthyContract;
import com.muju.note.launcher.app.video.db.VideoInfoDao;
import com.muju.note.launcher.base.BasePresenter;
import com.muju.note.launcher.litepal.LitePalDb;
import com.muju.note.launcher.util.log.LogUtil;

import org.litepal.LitePal;
import org.litepal.crud.callback.FindMultiCallback;

import java.util.List;

public class HealthyPresenter extends BasePresenter<HealthyContract.View> implements HealthyContract.Presenter {
    @Override
    public void getHealthy(String name,final int pageNum) {
        LitePalDb.setZkysDb();
        String sql = "customTag like '%" + name + "%' or keywords like '%" + name + "%' or name like '%" + name + "%'" +
                " and status = 1 order by number desc,onwayTime desc,editTime desc,updateTime desc";
        final int size = 20;

        final int count = LitePal.where(sql).count(VideoInfoDao.class);

        LitePal.limit(size).offset((pageNum - 1) * size).where(sql).findAsync(VideoInfoDao.class).listen(new FindMultiCallback<VideoInfoDao>() {
            @Override
            public void onFinish(List<VideoInfoDao> list) {
                if (mView == null) {
                    LogUtil.e("mView为空");
                    return;
                }
                if (list == null || list.size() <= 0) {
                    mView.getHealthyNull();
                    return;
                }
                mView.getHealthySuccess(list);

                if (((pageNum - 1) * size + list.size() ) >= count)
                {
                    if (mView != null)
                    {
                        mView.getHealthyEnd();
                    }
                }
            }
        });
    }
}
