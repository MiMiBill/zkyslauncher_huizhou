package com.muju.note.launcher.app.orderfood.presenter;

import com.muju.note.launcher.app.orderfood.contract.FoodContentContract;
import com.muju.note.launcher.app.orderfood.db.ComfoodDao;
import com.muju.note.launcher.base.BasePresenter;
import com.muju.note.launcher.litepal.LitePalDb;
import com.muju.note.launcher.util.log.LogUtil;

import org.litepal.LitePal;
import org.litepal.crud.callback.FindMultiCallback;

import java.util.List;

public class FoodContentPresenter extends BasePresenter<FoodContentContract.View> implements FoodContentContract.Presenter {

    @Override
    public void queryFoodList(int pageNum,int tabid) {
        LitePalDb.setZkysDb();
        int limit = (pageNum * 30) - 30;
        LitePal.where("tabid = ?",""+tabid).limit(100).offset(limit).findAsync(ComfoodDao.class).listen(new FindMultiCallback<ComfoodDao>() {
            @Override
            public void onFinish(List<ComfoodDao> list) {
                if (mView == null) {
                    LogUtil.e("mView为空");
                    return;
                }
                if (list == null || list.size() <= 0) {
                    mView.getListNull();
                    return;
                }
                mView.getFoodList(list);
                if (list.size() > 0 && list.size() < 100) {
                    mView.getListEnd();
                }
            }
        });
    }
}
