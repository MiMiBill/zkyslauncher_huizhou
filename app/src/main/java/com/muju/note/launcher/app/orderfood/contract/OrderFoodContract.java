package com.muju.note.launcher.app.orderfood.contract;

import com.muju.note.launcher.app.orderfood.db.CommodityDao;
import com.muju.note.launcher.base.IPresenter;
import com.muju.note.launcher.base.IView;

import java.util.List;

public interface OrderFoodContract {
    interface View extends IView {
        void getTab(List<CommodityDao> list);

        void getTabNull();

    }


    interface Presenter extends IPresenter<View> {
        void queryTab();
    }
}
