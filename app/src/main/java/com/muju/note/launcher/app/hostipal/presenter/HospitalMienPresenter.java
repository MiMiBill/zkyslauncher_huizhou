package com.muju.note.launcher.app.hostipal.presenter;


import com.muju.note.launcher.app.hostipal.contract.HospitalMienContract;
import com.muju.note.launcher.app.hostipal.db.MienInfoDao;
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
}
