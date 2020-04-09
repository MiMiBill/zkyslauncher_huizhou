package com.muju.note.launcher.service.Department;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.muju.note.launcher.app.home.db.HomeMenuDao;
import com.muju.note.launcher.app.home.db.HomeMenuSubDao;
import com.muju.note.launcher.app.hostipal.db.DepartmentInfoDao;
import com.muju.note.launcher.app.startUp.event.StartCheckDataEvent;
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
 *  所有科室
 */
public class DepartmentInfoService {

    private static final String TAG= DepartmentInfoService.class.getSimpleName();

    public static DepartmentInfoService homeMenuService=null;
    public static DepartmentInfoService getInstance(){
        if(homeMenuService==null){
            homeMenuService=new DepartmentInfoService();
        }
        return homeMenuService;
    }

    public boolean isUpdate=false;

    public void startDepartment(){
        EventBus.getDefault().post(new StartCheckDataEvent(StartCheckDataEvent.Status.DEPARTMENT_START));
        LitePalDb.setZkysDb();
        LitePal.findAllAsync(DepartmentInfoDao.class).listen(new FindMultiCallback<DepartmentInfoDao>() {
            @Override
            public void onFinish(List<DepartmentInfoDao> list) {
                if(list==null||list.size()<=0){
                    updateDepartment();
                }else {
                    EventBus.getDefault().post(new StartCheckDataEvent(StartCheckDataEvent.Status.DEPARTMENT_SUCCESS));
                }
            }
        });
    }



    /**
     *  更新菜单数据
     */
    public void updateDepartment(){
        EventBus.getDefault().post(new StartCheckDataEvent(StartCheckDataEvent.Status.DEPARTMENT_HTTP_START));
        OkGo.<BaseBean<List<DepartmentInfoDao>>>get(UrlUtil.getHospitalDepartmentList(ActiveUtils.getPadActiveInfo().getHospitalId()))
                .execute(new JsonCallback<BaseBean<List<DepartmentInfoDao>>>() {
                    @Override
                    public void onSuccess(final Response<BaseBean<List<DepartmentInfoDao>>> response) {

                        if(response==null||response.body()==null||response.body().getData()==null || response.body().getData().size() == 0){
                            EventBus.getDefault().post(new StartCheckDataEvent(StartCheckDataEvent.Status.DEPARTMENT_SUCCESS));
                            return;
                        }
                        EventBus.getDefault().post(new StartCheckDataEvent(StartCheckDataEvent.Status.DEPARTMENT_DB_START));
                        ExecutorService service= Executors.newSingleThreadExecutor();
                        service.execute(new Runnable() {
                            @Override
                            public void run() {
                                LitePalDb.setZkysDb();
                                LitePal.deleteAll(DepartmentInfoDao.class);
                                LitePal.saveAll(response.body().getData());
                                EventBus.getDefault().post(new StartCheckDataEvent(StartCheckDataEvent.Status.DEPARTMENT_SUCCESS));
                                isUpdate=true;
                            }
                        });
                    }
                    @Override
                    public void onError(Response<BaseBean<List<DepartmentInfoDao>>> response) {
                        super.onError(response);
                        EventBus.getDefault().post(new StartCheckDataEvent(StartCheckDataEvent.Status.DEPARTMENT_HTTP_FAIL));

                    }
                });
    }


}
