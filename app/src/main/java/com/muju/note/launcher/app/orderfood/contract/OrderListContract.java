package com.muju.note.launcher.app.orderfood.contract;

import com.muju.note.launcher.app.orderfood.bean.OrderListBean;
import com.muju.note.launcher.base.IPresenter;
import com.muju.note.launcher.base.IView;

import java.util.List;

public interface OrderListContract {
    interface View extends IView {
        void tabList(List<OrderListBean> list);
    }


    interface Presenter extends IPresenter<View> {
        void tabList(int bedid);
    }
}
