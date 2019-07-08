package com.muju.note.launcher.app.hostipal.presenter;


import com.muju.note.launcher.app.hostipal.contract.EncyHosContract;
import com.muju.note.launcher.app.hostipal.db.InfoDao;
import com.muju.note.launcher.app.hostipal.db.InfomationDao;
import com.muju.note.launcher.base.BasePresenter;
import com.muju.note.launcher.litepal.LitePalDb;
import com.muju.note.launcher.util.log.LogUtil;

import org.litepal.LitePal;
import org.litepal.crud.callback.FindMultiCallback;

import java.util.List;

public class EncyPresenter extends BasePresenter<EncyHosContract.View> implements EncyHosContract.Presenter {
    /**
     *  从数据库查询科室数据
     */
    @Override
    public void queryEncyCloumn() {
        if(mView==null){
            LogUtil.e("mView为空");
            return;
        }
        LitePalDb.setZkysDb();
        LitePal.where("isDel = 0").findAsync(InfoDao.class).listen(new FindMultiCallback<InfoDao>() {
//        LitePal.findAllAsync(InfoDao.class).listen(new FindMultiCallback<InfoDao>() {
            @Override
            public void onFinish(List<InfoDao> list) {
                if(mView==null){
                    LogUtil.e("mView为空");
                    return;
                }
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
        LitePal.where("isDel = ?","0").limit(1).findAsync(InfomationDao.class).listen(new FindMultiCallback<InfomationDao>() {

            @Override
            public void onFinish(List<InfomationDao> list) {
                if(mView==null){
                    LogUtil.e("mView为空");
                    return;
                }
                if(list!=null&&list.size()>0) {
                    mView.setTopInfomation(list.get(0));
                }else {
                    LogUtil.i("size:"+list.size());
                }
            }
        });
    }


    /**
     *  从数据库查询首条病例数据
     */
    @Override
    public void queryEncyClopediaById(int id) {
        LitePalDb.setZkysDb();
        LitePal.where("isDel = 0 and id =?",id+"").limit(1).findAsync(InfomationDao.class).listen(new FindMultiCallback<InfomationDao>() {
            @Override
            public void onFinish(List<InfomationDao> list) {
                if(mView==null){
                    LogUtil.e("mView为空");
                    return;
                }
                if(list!=null&&list.size()>0) {
                    mView.setInfomationById(list.get(0));
                }
            }
        });
    }


    /**
     *  从数据库分页查询病例数据
     */
    @Override
    public void queryEncyClopediapage(int columnId,int pageNum,int type) {
        if(mView==null){
            LogUtil.e("mView为空");
            return;
        }
        LitePalDb.setZkysDb();
        LitePal.where("isDel = 0 and columnId =?",""+columnId).limit(12).offset(pageNum*10).findAsync(InfomationDao.class).listen(new FindMultiCallback<InfomationDao>() {
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
        LitePal.where("isDel = 0 and title like"+"\'" + "%" + keyWord + "%" + "\'").limit(40).findAsync(InfomationDao.class).listen(new FindMultiCallback<InfomationDao>() {
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
