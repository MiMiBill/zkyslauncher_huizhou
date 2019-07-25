package com.muju.note.launcher.app.orderfood.contract;

import com.muju.note.launcher.base.IPresenter;
import com.muju.note.launcher.base.IView;

import org.json.JSONObject;

public interface OrderPayContract {
    interface View extends IView {
        void orderCreate(String data);
        void orderError();
    }


    interface Presenter extends IPresenter<View> {
        void orderCreate(JSONObject jsonObject);
    }
}
