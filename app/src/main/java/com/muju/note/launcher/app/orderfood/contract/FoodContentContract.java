package com.muju.note.launcher.app.orderfood.contract;

import com.muju.note.launcher.app.orderfood.db.ComfoodDao;
import com.muju.note.launcher.base.IPresenter;
import com.muju.note.launcher.base.IView;

import java.util.List;

public interface FoodContentContract {
    interface View extends IView {
        void getFoodList(List<ComfoodDao> list);

        void getListNull();

        void getListEnd();

    }


    interface Presenter extends IPresenter<View> {
        void queryFoodList(int pageNum,int id);
    }
}
