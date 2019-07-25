package com.muju.note.launcher.app.adtask.contract;

import com.muju.note.launcher.base.IPresenter;
import com.muju.note.launcher.base.IView;

public interface MainContract {

    interface View extends IView {
        void getTaskListSuccess();

        void getDate(String time, String net, String netType);
    }


    interface Presenter extends IPresenter<View> {

        void getTaskList(int userId, int hospitalId, int depId);

        void getUpdateArriveFlag(String pushId);
    }
}
