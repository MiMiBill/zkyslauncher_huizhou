package com.muju.note.launcher.service.homemenu;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.muju.note.launcher.app.home.db.HomeMenuDao;
import com.muju.note.launcher.app.home.db.HomeMenuSubDao;
import com.muju.note.launcher.app.startUp.event.StartCheckDataEvent;
import com.muju.note.launcher.app.video.db.VideoInfoDao;
import com.muju.note.launcher.litepal.LitePalDb;
import com.muju.note.launcher.okgo.BaseBean;
import com.muju.note.launcher.okgo.JsonCallback;
import com.muju.note.launcher.topics.SpTopics;
import com.muju.note.launcher.url.UrlUtil;
import com.muju.note.launcher.util.ActiveUtils;
import com.muju.note.launcher.util.sp.SPUtil;

import org.greenrobot.eventbus.EventBus;
import org.litepal.LitePal;
import org.litepal.crud.callback.FindMultiCallback;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 *  首页菜单
 */
public class HomeMenuService {

    private static final String TAG=HomeMenuService.class.getSimpleName();

    public static HomeMenuService homeMenuService=null;
    public static HomeMenuService getInstance(){
        if(homeMenuService==null){
            homeMenuService=new HomeMenuService();
        }
        return homeMenuService;
    }

    public void startMenu(){
        EventBus.getDefault().post(new StartCheckDataEvent(StartCheckDataEvent.Status.HOME_MENU_START));
        LitePal.findAllAsync(HomeMenuDao.class).listen(new FindMultiCallback<HomeMenuDao>() {
            @Override
            public void onFinish(List<HomeMenuDao> list) {
                if(list==null||list.size()<=0){
                    updateMenu(1);
                }else {
                    EventBus.getDefault().post(new StartCheckDataEvent(StartCheckDataEvent.Status.HOME_MENU_SUCCESS));
                }
            }
        });
    }

    /**
     *  根据自启动状态，查看是否需要更新数据
     */
    public void startRebootMenu(){
        EventBus.getDefault().post(new StartCheckDataEvent(StartCheckDataEvent.Status.HOME_MENU_REBOOT_START));
        boolean rebootPhone = SPUtil.getBoolean(SpTopics.SP_REBOOT);
        if(!rebootPhone){
            updateMenu(2);
        }else {
            EventBus.getDefault().post(new StartCheckDataEvent(StartCheckDataEvent.Status.HOME_MENU_REBOOT_SUCCESS));
        }
    }

    /**
     *  更新菜单数据
     */
    public void updateMenu(final int status){
        if(status==1) {
            EventBus.getDefault().post(new StartCheckDataEvent(StartCheckDataEvent.Status.HOME_MENU_HTTP_START));
        }else {
            EventBus.getDefault().post(new StartCheckDataEvent(StartCheckDataEvent.Status.HOME_MENU_REBOOT_HTTP_START));
        }
        OkGo.<BaseBean<List<HomeMenuDao>>>get(UrlUtil.getHomeMenu())
                .params("deptId", ActiveUtils.getPadActiveInfo().getDeptId())
                .execute(new JsonCallback<BaseBean<List<HomeMenuDao>>>() {
                    @Override
                    public void onSuccess(final Response<BaseBean<List<HomeMenuDao>>> response) {
                        if(response==null||response.body()==null||response.body().getData()==null){
                            if(status==1) {
                                EventBus.getDefault().post(new StartCheckDataEvent(StartCheckDataEvent.Status.HOME_MENU_REBOOT_HTTP_DATA_NULL));
                            }
                            return;
                        }
                        if(status==1) {
                            EventBus.getDefault().post(new StartCheckDataEvent(StartCheckDataEvent.Status.HOME_MENU_DB_START));
                        }else {
                            EventBus.getDefault().post(new StartCheckDataEvent(StartCheckDataEvent.Status.HOME_MENU_REBOOT_DB_START));
                        }
                        ExecutorService service= Executors.newSingleThreadExecutor();
                        service.execute(new Runnable() {
                            @Override
                            public void run() {
                                LitePalDb.setZkysDb();
                                LitePal.deleteAll(HomeMenuDao.class);
                                LitePal.deleteAll(HomeMenuSubDao.class);
                                for (HomeMenuDao dao:response.body().getData()){
                                    saveMenu(dao);
                                    for (HomeMenuSubDao subDao:dao.getChild()){
                                        saveSubMenu(subDao);
                                    }
                                }
                                if(status==1) {
                                    EventBus.getDefault().post(new StartCheckDataEvent(StartCheckDataEvent.Status.HOME_MENU_SUCCESS));
                                }else {
                                    EventBus.getDefault().post(new StartCheckDataEvent(StartCheckDataEvent.Status.HOME_MENU_REBOOT_SUCCESS));
                                }
                            }
                        });
                    }

                    @Override
                    public void onError(Response<BaseBean<List<HomeMenuDao>>> response) {
                        super.onError(response);
                        if(status==1) {
                            EventBus.getDefault().post(new StartCheckDataEvent(StartCheckDataEvent.Status.HOME_MENU_HTTP_FAIL));
                        }else {
                            EventBus.getDefault().post(new StartCheckDataEvent(StartCheckDataEvent.Status.HOME_MENU_REBOOT_HTTP_FAIL));
                        }
                    }
                });
    }

    private void saveMenu(HomeMenuDao dao){
        dao.setMenuId(dao.getId());
        dao.save();
    }

    private void saveSubMenu(HomeMenuSubDao subDao){
        subDao.setMenuId(subDao.getId());
        subDao.save();
    }

}
