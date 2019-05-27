package com.muju.note.launcher.app.hostipal.contract;

import com.muju.note.launcher.app.hostipal.bean.GetDownloadBean;
import com.muju.note.launcher.base.IPresenter;
import com.muju.note.launcher.base.IView;

public interface EncyContract {

    interface View extends IView {
        void getDownLoadUrl(GetDownloadBean getDownloadBean);
    }


    interface Presenter extends IPresenter<View> {
        void queryEncy();
        void getDownLoadUrl();
    }
}
