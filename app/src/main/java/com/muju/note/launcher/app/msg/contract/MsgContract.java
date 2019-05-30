package com.muju.note.launcher.app.msg.contract;

import com.muju.note.launcher.app.msg.db.CustomMessageDao;
import com.muju.note.launcher.base.IPresenter;
import com.muju.note.launcher.base.IView;

import java.util.List;

public interface MsgContract {

    interface View extends IView {
        void getMsg(List<CustomMessageDao> list);
        void getMsgNull();
    }


    interface Presenter extends IPresenter<View> {
        void querymsg();
    }
}
