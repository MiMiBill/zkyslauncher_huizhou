package com.muju.note.launcher.app.sign.contract;

import com.muju.note.launcher.app.sign.bean.PriseBean;
import com.muju.note.launcher.base.IPresenter;
import com.muju.note.launcher.base.IView;

import java.util.ArrayList;

public interface PriseContract {
    interface View extends IView {
        void setPrise(ArrayList<PriseBean> priseBeans);
    }


    interface Presenter extends IPresenter<PriseContract.View> {
        void getPointList(int userId);
    }
}
