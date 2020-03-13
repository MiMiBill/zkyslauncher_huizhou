package com.muju.note.launcher.app.hostipal.presenter;


import com.muju.note.launcher.app.hostipal.contract.HospitalMissionContract;
import com.muju.note.launcher.app.hostipal.db.MissionInfoDao;
import com.muju.note.launcher.app.video.db.VideoInfoDao;
import com.muju.note.launcher.base.BasePresenter;
import com.muju.note.launcher.litepal.LitePalDb;
import com.muju.note.launcher.util.log.LogUtil;

import org.litepal.LitePal;
import org.litepal.crud.callback.FindMultiCallback;

import java.util.List;

public class HospitalMissionPresenter extends BasePresenter<HospitalMissionContract.View> implements HospitalMissionContract.Presenter {


    /**
     * 查询医院宣教数据
     */
    @Override
    public void queryMiss() {
        LitePalDb.setZkysDb();
        LitePal.findAllAsync(MissionInfoDao.class).listen(new FindMultiCallback<MissionInfoDao>() {
            @Override
            public void onFinish(List<MissionInfoDao> list) {
                if (mView == null) {
                    LogUtil.e("mView为空");
                    return;
                }
                if (list == null || list.size() <= 0) {
                    mView.getMissNull();
                    return;
                }
                mView.getMission(list);
            }
        });
    }

    @Override
    public void getMissionVideo(String name, int pageNum) {
        LitePalDb.setZkysDb();
//        String sql = "customTag like '%" + name + "%' or keywords like '%" + name + "%' or name like '%" + name + "%'" +
//                " and status = 1 order by number desc,onwayTime desc,editTime desc,updateTime desc";
//        LogUtil.d("医院风采视频SQL："  + sql);

        //可能存在一个视频多个科室都使用的情况，所以用like去适配
        String sql = "customTag like '%" + name +
                "%' and status = 1 order by number desc,onwayTime desc,editTime desc,updateTime desc";
        LogUtil.d("医院宣教视频SQL："  + sql);
        LitePal.where(sql).findAsync(VideoInfoDao.class).listen(new FindMultiCallback<VideoInfoDao>() {
            @Override
            public void onFinish(List<VideoInfoDao> list) {
                if (mView == null) {
                    LogUtil.e("mView为空");
                    return;
                }
                if (list == null || list.size() <= 0) {
                    mView.getMissionVideoNull();
                    return;
                }
                mView.getMissionVideoSuccess(list);
            }
        });
    }
}
