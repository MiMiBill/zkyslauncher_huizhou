package com.muju.note.launcher.app.adtask.presenter;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.muju.note.launcher.app.adtask.TaskListBean;
import com.muju.note.launcher.app.adtask.contract.MainContract;
import com.muju.note.launcher.base.BasePresenter;
import com.muju.note.launcher.okgo.BaseBean;
import com.muju.note.launcher.okgo.JsonCallback;
import com.muju.note.launcher.url.UrlUtil;
import com.muju.note.launcher.util.log.LogUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainPresenter extends BasePresenter<MainContract.View> implements MainContract
        .Presenter {
    private static final String TAG = "MainPresenter";
    private TaskListener listener;
    @Override
    public void getTaskList(int userId, int hospitalId, int depId) {
        Map<String, String> params = new HashMap();
        params.put("userId", ""+userId);
        params.put("hospitalId", ""+hospitalId);
        params.put("deptId", ""+depId);
        OkGo.<BaseBean<List<TaskListBean>>>post(UrlUtil.getTaskList())
                .tag(this)
                .params(params)
                .execute(new JsonCallback<BaseBean<List<TaskListBean>>>() {
                             @Override
                             public void onSuccess(Response<BaseBean<List<TaskListBean>>> response) {
                                 LogUtil.i(TAG, response.body());

                                 if(listener!=null)
                                     listener.getTaskListSuccess(response.body().getData());
                             }

                             @Override
                             public void onError(Response<BaseBean<List<TaskListBean>>> response) {
                                 super.onError(response);
                             }
                         }
                );
    }


    public void setOnTaskListener(TaskListener listener){
        this.listener=listener;
    }

    public interface TaskListener{
        void getTaskListSuccess(List<TaskListBean> taskListBeans);
    }

}
