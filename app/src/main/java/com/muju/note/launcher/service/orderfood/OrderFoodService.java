package com.muju.note.launcher.service.orderfood;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.muju.note.launcher.app.home.db.HomeMenuDao;
import com.muju.note.launcher.app.orderfood.db.ComfoodDao;
import com.muju.note.launcher.app.orderfood.db.CommodityDao;
import com.muju.note.launcher.app.startUp.event.StartCheckDataEvent;
import com.muju.note.launcher.litepal.LitePalDb;
import com.muju.note.launcher.okgo.BaseBean;
import com.muju.note.launcher.okgo.JsonCallback;
import com.muju.note.launcher.topics.SpTopics;
import com.muju.note.launcher.url.UrlUtil;
import com.muju.note.launcher.util.ActiveUtils;
import com.muju.note.launcher.util.log.LogFactory;
import com.muju.note.launcher.util.sp.SPUtil;

import org.greenrobot.eventbus.EventBus;
import org.litepal.LitePal;
import org.litepal.crud.callback.FindMultiCallback;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 *  点餐
 */
public class OrderFoodService {

    private static final String TAG=OrderFoodService.class.getSimpleName();

    public static OrderFoodService foodService =null;
    public static OrderFoodService getInstance(){
        if(foodService ==null){
            foodService =new OrderFoodService();
        }
        return foodService;
    }

    public boolean isUpdate=false;

    public void startfoodMenu(){
        EventBus.getDefault().post(new StartCheckDataEvent(StartCheckDataEvent.Status.FOOD_MENU_START));
        LitePal.findAllAsync(HomeMenuDao.class).listen(new FindMultiCallback<HomeMenuDao>() {
            @Override
            public void onFinish(List<HomeMenuDao> list) {
                if(list==null||list.size()<=0){
                    updatefoodMenu(1);
                }else {
                    EventBus.getDefault().post(new StartCheckDataEvent(StartCheckDataEvent.Status.FOOD_MENU_SUCCESS));
                }
            }
        });
    }

    /**
     *  根据自启动状态，查看是否需要更新数据
     */
    public void startRebootFoodMenu(){
        EventBus.getDefault().post(new StartCheckDataEvent(StartCheckDataEvent.Status.FOOD_MENU_REBOOT_START));
        boolean rebootPhone = SPUtil.getBoolean(SpTopics.SP_REBOOT);
        if(!rebootPhone){
            updatefoodMenu(2);
        }else {
            EventBus.getDefault().post(new StartCheckDataEvent(StartCheckDataEvent.Status.FOOD_MENU_REBOOT_SUCCESS));
        }
    }

    /**
     *  更新菜单数据
     */
    public void updatefoodMenu(final int status){
        LogFactory.l().i("status==="+status);
        if(status==1) {
            EventBus.getDefault().post(new StartCheckDataEvent(StartCheckDataEvent.Status.FOOD_MENU_HTTP_START));
        }else {
            EventBus.getDefault().post(new StartCheckDataEvent(StartCheckDataEvent.Status.FOOD_MENU_REBOOT_HTTP_START));
        }
        OkGo.<BaseBean<List<CommodityDao>>>post(UrlUtil.commodityAll())
                .params("hospitalId", ActiveUtils.getPadActiveInfo().getHospitalId())
                .params("deptId", ActiveUtils.getPadActiveInfo().getDeptId())
                .execute(new JsonCallback<BaseBean<List<CommodityDao>>>() {
                    @Override
                    public void onSuccess(final Response<BaseBean<List<CommodityDao>>> response) {
                        if(response==null||response.body()==null||response.body().getData()==null){
                            if(status==1) {
                                EventBus.getDefault().post(new StartCheckDataEvent(StartCheckDataEvent.Status.FOOD_MENU_REBOOT_HTTP_DATA_NULL));
                            }
                            return;
                        }
                        if(status==1) {
                            EventBus.getDefault().post(new StartCheckDataEvent(StartCheckDataEvent.Status.FOOD_MENU_DB_START));
                        }else {
                            EventBus.getDefault().post(new StartCheckDataEvent(StartCheckDataEvent.Status.FOOD_MENU_REBOOT_DB_START));
                        }
                        ExecutorService service= Executors.newSingleThreadExecutor();
                        service.execute(new Runnable() {
                            @Override
                            public void run() {
                                LitePalDb.setZkysDb();
                                LitePal.deleteAll(CommodityDao.class);
                                LitePal.deleteAll(ComfoodDao.class);
                                for (CommodityDao dao: response.body().getData()){
                                    saveComdity(dao);
                                    for (ComfoodDao subDao : dao.getCommodities()){
                                        subDao.setTabid(dao.getCommodid());
                                        saveFoodMenu(subDao);
                                    }
                                }
                                if(status==1) {
                                    EventBus.getDefault().post(new StartCheckDataEvent(StartCheckDataEvent.Status.FOOD_MENU_SUCCESS));
                                }else {
                                    EventBus.getDefault().post(new StartCheckDataEvent(StartCheckDataEvent.Status.FOOD_MENU_REBOOT_SUCCESS));
                                }
                                isUpdate=true;
                            }
                        });
                    }

                    @Override
                    public void onError(Response<BaseBean<List<CommodityDao>>> response) {
                        super.onError(response);
                        if(status==1) {
                            EventBus.getDefault().post(new StartCheckDataEvent(StartCheckDataEvent.Status.FOOD_MENU_HTTP_FAIL));
                        }else {
                            EventBus.getDefault().post(new StartCheckDataEvent(StartCheckDataEvent.Status.FOOD_MENU_REBOOT_HTTP_FAIL));
                        }
                    }
                });
    }

    private void saveComdity(CommodityDao dao){
        dao.setCommodid(dao.getId());
        dao.save();
    }

    private void saveFoodMenu(ComfoodDao subDao){
        subDao.setFoodid(subDao.getId());
        subDao.save();
    }

}
