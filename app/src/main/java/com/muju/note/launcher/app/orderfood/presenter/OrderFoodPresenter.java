package com.muju.note.launcher.app.orderfood.presenter;

import com.muju.note.launcher.app.orderfood.contract.OrderFoodContract;
import com.muju.note.launcher.app.orderfood.db.CommodityDao;
import com.muju.note.launcher.base.BasePresenter;
import com.muju.note.launcher.litepal.LitePalDb;
import com.muju.note.launcher.util.log.LogUtil;

import org.litepal.LitePal;
import org.litepal.crud.callback.FindMultiCallback;

import java.util.List;

public class OrderFoodPresenter extends BasePresenter<OrderFoodContract.View> implements OrderFoodContract.Presenter {

    @Override
    public void queryTab() {
        LitePalDb.setZkysDb();
        LitePal.findAllAsync(CommodityDao.class).listen(new FindMultiCallback<CommodityDao>() {
            @Override
            public void onFinish(List<CommodityDao> list) {
                if (mView == null) {
                    LogUtil.e("mView为空");
                    return;
                }
                if (list == null || list.size() <= 0) {
                    mView.getTabNull();
                    return;
                }
                mView.getTab(list);
            }
        });
    }
}
