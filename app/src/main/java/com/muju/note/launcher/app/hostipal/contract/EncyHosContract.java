package com.muju.note.launcher.app.hostipal.contract;

import com.muju.note.launcher.app.hostipal.db.InfoDao;
import com.muju.note.launcher.app.hostipal.db.InfomationDao;
import com.muju.note.launcher.base.IPresenter;
import com.muju.note.launcher.base.IView;

import java.util.List;

public interface EncyHosContract {

    interface View extends IView {
        void getInfoNull();

        void getInfo(List<InfoDao> list);

        void getInfoMation(List<InfomationDao> list);

        void getInfoMationPage(List<InfomationDao> list);

        void setTopInfomation(InfomationDao infomation);

        void setInfomationById(InfomationDao infomation);

        void setInfomationByTitle(InfomationDao infomation);

        void search(List<InfomationDao> list);
    }


    interface Presenter extends IPresenter<View> {
        void queryEncyCloumn();

        void queryTopEncyClopedia();

        void queryEncyClopediapage(int columnId, int pageNum, int type);

        void queryEncyClopediaById(int columnId);

        void querySearch(String keyWord);
    }
}
