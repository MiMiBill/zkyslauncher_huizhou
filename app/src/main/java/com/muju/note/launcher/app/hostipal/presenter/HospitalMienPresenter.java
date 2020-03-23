package com.muju.note.launcher.app.hostipal.presenter;


import com.muju.note.launcher.app.hostipal.contract.HospitalMienContract;
import com.muju.note.launcher.app.hostipal.db.MienInfoDao;
import com.muju.note.launcher.app.video.db.VideoInfoDao;
import com.muju.note.launcher.base.BasePresenter;
import com.muju.note.launcher.litepal.LitePalDb;
import com.muju.note.launcher.util.log.LogUtil;

import org.litepal.LitePal;
import org.litepal.crud.callback.FindMultiCallback;

import java.util.List;

public class HospitalMienPresenter extends BasePresenter<HospitalMienContract.View> implements HospitalMienContract.Presenter {

    /**
     * 从数据库查询医院风采数据
     */
    @Override
    public void queryMien() {
        LitePalDb.setZkysDb();
        LitePal.findAllAsync(MienInfoDao.class).listen(new FindMultiCallback<MienInfoDao>() {
            @Override
            public void onFinish(List<MienInfoDao> list) {
                if (mView == null) {
                    LogUtil.e("mView为空");
                    return;
                }
                if (list == null || list.size() <= 0) {
                    mView.getMienNull();
                    return;
                }
                mView.getMien(list);
            }
        });
    }

    @Override
    public void getHospitalMienVideo(String name, int pageNum) {
        LitePalDb.setZkysDb();
//        String sql = "customTag like '%" + name + "%' or keywords like '%" + name + "%' or name like '%" + name + "%'" +
//                " and status = 1 order by number desc,onwayTime desc,editTime desc,updateTime desc";
//        LogUtil.d("医院风采视频SQL："  + sql);

        String sql = "customTag like '%" + name +
                "%' and status = 1 order by number desc,onwayTime desc,editTime desc,updateTime desc";
        LogUtil.d("医院风采视频SQL："  + sql);
        LitePal.where(sql).findAsync(VideoInfoDao.class).listen(new FindMultiCallback<VideoInfoDao>() {
            @Override
            public void onFinish(List<VideoInfoDao> list) {
                if (mView == null) {
                    LogUtil.e("mView为空");
                    return;
                }
                if (list == null || list.size() <= 0) {
                    mView.getHospitalMienVideoNull();
                    return;
                }
                mView.getHospitalMienVideoSuccess(list);
            }
        });


    }
}
