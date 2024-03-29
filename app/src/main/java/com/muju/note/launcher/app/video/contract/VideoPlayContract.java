package com.muju.note.launcher.app.video.contract;

import com.muju.note.launcher.app.video.bean.WeiXinTask;
import com.muju.note.launcher.base.IPresenter;
import com.muju.note.launcher.base.IView;


public interface VideoPlayContract {

    interface View extends IView {
        void getComboList(String data);
        void getComboListFail();
        void verfycode(String response);
        void verfycodeError();
        void setPayPackageList(String data);
        void setPayFail();
        void intervalSLOrder(String data);

        /**
         * 获取到了微信任务
         * @param data
         */
        void getWeiXinTask(WeiXinTask.WeiXinTaskData data);

        /**
         * 获取微信任务失败
         */
        void getWeiXinTaskFail();

    }


    interface Presenter extends IPresenter<VideoPlayContract.View> {
        void getComboList(int hospitalId,int deptId);
        void verfycode(String code);
        void setPayPackageList();
        void intervalSLOrder();
        /**
         * 获取微信任务，新加的接口
         */
        void getWeiXinTask(String hospitalId,String deptId);
    }

}
