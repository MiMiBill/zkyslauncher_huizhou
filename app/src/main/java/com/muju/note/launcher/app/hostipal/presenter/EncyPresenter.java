package com.muju.note.launcher.app.hostipal.presenter;


import com.muju.note.launcher.app.hostipal.contract.EncyHosContract;
import com.muju.note.launcher.app.hostipal.db.InfoDao;
import com.muju.note.launcher.app.hostipal.db.InfomationDao;
import com.muju.note.launcher.base.BasePresenter;
import com.muju.note.launcher.litepal.LitePalDb;

import org.litepal.LitePal;
import org.litepal.crud.callback.FindMultiCallback;

import java.util.List;

public class EncyPresenter extends BasePresenter<EncyHosContract.View> implements EncyHosContract.Presenter {
    /**
     *  从数据库查询科室数据
     */
    @Override
    public void queryEncyCloumn() {
        LitePalDb.setZkysDb();
        LitePal.findAllAsync(InfoDao.class).listen(new FindMultiCallback<InfoDao>() {
            @Override
            public void onFinish(List<InfoDao> list) {
                if(list==null||list.size()<=0){
                    mView.getInfoNull();
                    return;
                }
                mView.getInfo(list);
            }
        });
    }



    /**
     *  从数据库查询首条病例数据
     */
    @Override
    public void queryTopEncyClopedia() {
        LitePalDb.setZkysDb();
        InfomationDao infomationDao = LitePal.findFirst(InfomationDao.class);
        if(infomationDao!=null)
        mView.setTopInfomation(infomationDao);
    }


    /**
     *  从数据库查询首条病例数据
     */
    @Override
    public void queryEncyClopediaById(int id) {
        LitePalDb.setZkysDb();
        InfomationDao infomationDao= LitePal.find(InfomationDao.class,id);
        if(infomationDao!=null)
        mView.setInfomationById(infomationDao);
    }


    /**
     *  从数据库分页查询病例数据
     */
    @Override
    public void queryEncyClopediapage(int columnId,int pageNum,int type) {
        LitePalDb.setZkysDb();
        LitePal.where("columnId ="+columnId).limit(12).offset(pageNum).findAsync(InfomationDao.class).listen(new FindMultiCallback<InfomationDao>() {
            @Override
            public void onFinish(List<InfomationDao> list) {
                if(list==null||list.size()<=0){
                    mView.getInfoNull();
                    return;
                }
                mView.getInfoMationPage(list);
            }
        });
    }


    /**
     *  搜索病例
     */
    @Override
    public void querySearch(String keyWord) {
        LitePalDb.setZkysDb();
        LitePal.where("title like" + "\'" + "%" + keyWord + "%" + "\'").limit(40).findAsync(InfomationDao.class).listen(new FindMultiCallback<InfomationDao>() {
            @Override
            public void onFinish(List<InfomationDao> list) {
                if(list==null||list.size()<=0){
                    mView.getInfoNull();
                    return;
                }
                mView.search(list);
            }
        });
    }
}
