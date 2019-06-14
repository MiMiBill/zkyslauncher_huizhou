package com.muju.note.launcher.app.sign.contract;

import com.muju.note.launcher.app.sign.bean.PriseBean;
import com.muju.note.launcher.base.IPresenter;
import com.muju.note.launcher.base.IView;

public interface PriseContract {
    interface View extends IView {
        void useReward(PriseBean priseBeans);
    }


    interface Presenter extends IPresenter<PriseContract.View> {
        void useReward(int typeId,int count);
    }
}
