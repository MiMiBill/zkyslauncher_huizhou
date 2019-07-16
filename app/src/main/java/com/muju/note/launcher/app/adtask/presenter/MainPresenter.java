package com.muju.note.launcher.app.adtask.presenter;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.muju.note.launcher.app.adtask.TaskListBean;
import com.muju.note.launcher.app.adtask.contract.MainContract;
import com.muju.note.launcher.base.BasePresenter;
import com.muju.note.launcher.base.LauncherApplication;
import com.muju.note.launcher.okgo.BaseBean;
import com.muju.note.launcher.okgo.JsonCallback;
import com.muju.note.launcher.url.UrlUtil;
import com.muju.note.launcher.util.DateUtil;
import com.muju.note.launcher.util.log.LogUtil;
import com.muju.note.launcher.util.net.NetWorkUtil;
import com.muju.note.launcher.util.rx.RxUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;

public class MainPresenter extends BasePresenter<MainContract.View> implements MainContract
        .Presenter {
    private static final String TAG = "MainPresenter";
    private TaskListener listener;

    @Override
    public void getTaskList(int userId, int hospitalId, int depId) {
        Map<String, String> params = new HashMap();
        params.put("userId", "" + userId);
        params.put("hospitalId", "" + hospitalId);
        params.put("deptId", "" + depId);
        OkGo.<BaseBean<List<TaskListBean>>>post(UrlUtil.getTaskList())
                .tag(this)
                .params(params)
                .execute(new JsonCallback<BaseBean<List<TaskListBean>>>() {
                             @Override
                             public void onSuccess(Response<BaseBean<List<TaskListBean>>> response) {
                                 LogUtil.i(TAG, response.body());

                                 if (listener != null)
                                     listener.getTaskListSuccess(response.body().getData());
                             }

                             @Override
                             public void onError(Response<BaseBean<List<TaskListBean>>> response) {
                                 super.onError(response);
                             }
                         }
                );
    }

    /**
     * 更新标题栏时间
     */
    private Disposable diDateTimer;

    public void updateDate() {
        Observable.interval(1, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Long>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        diDateTimer = d;
                    }

                    @Override
                    public void onNext(Long aLong) {
                        if (mView == null) {
                            LogUtil.e("mView为空");
                            return;
                        }
                        mView.getDate(DateUtil.getDate("HH:mm:ss"), NetWorkUtil.getNetWorkLine(),
                                NetWorkUtil.getNetworkState(LauncherApplication.getContext()));
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }


    public void setOnTaskListener(TaskListener listener) {
        this.listener = listener;
    }

    public interface TaskListener {
        void getTaskListSuccess(List<TaskListBean> taskListBeans);
    }

    public void onDestroy() {
        RxUtil.closeDisposable(diDateTimer);
    }

}
