package com.muju.note.launcher.app.hostipal.presenter;


import com.muju.note.launcher.app.hostipal.contract.HospitalMissionContract;
import com.muju.note.launcher.app.hostipal.db.MissionInfoDao;
import com.muju.note.launcher.base.BasePresenter;
import com.muju.note.launcher.util.log.LogUtil;

import org.litepal.LitePal;
import org.litepal.crud.callback.FindMultiCallback;

import java.util.List;

public class HospitalMissionPresenter extends BasePresenter<HospitalMissionContract.View> implements HospitalMissionContract.Presenter {


    /**
     *  查询医院宣教数据
     */
    @Override
    public void queryMiss() {
        LitePal.findAllAsync(MissionInfoDao.class).listen(new FindMultiCallback<MissionInfoDao>() {
            @Override
            public void onFinish(List<MissionInfoDao> list) {
                if(mView==null){
                    LogUtil.e("mView为空");
                    return;
                }
                if(list==null||list.size()<=0){
                    mView.getMissNull();
                    return;
                }
                mView.getMission(list);
            }
        });
    }
}
