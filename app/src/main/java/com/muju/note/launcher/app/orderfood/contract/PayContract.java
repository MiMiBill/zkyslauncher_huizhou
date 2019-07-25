package com.muju.note.launcher.app.orderfood.contract;

import com.muju.note.launcher.base.IPresenter;
import com.muju.note.launcher.base.IView;

public interface PayContract {
    interface View extends IView {
        void queryPay(String data);
        void canclePay(String data);
    }


    interface Presenter extends IPresenter<View> {
        void queryPay(int orderId);
        void canclePay(int orderId);
    }
}
