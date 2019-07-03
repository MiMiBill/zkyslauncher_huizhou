package com.muju.note.launcher.app.bedsidecard.presenter;

import com.muju.note.launcher.app.bedsidecard.contract.BedsideContract;
import com.muju.note.launcher.base.BasePresenter;
import com.muju.note.launcher.base.LauncherApplication;
import com.muju.note.launcher.util.DateUtil;
import com.muju.note.launcher.util.log.LogUtil;
import com.muju.note.launcher.util.net.NetWorkUtil;
import com.muju.note.launcher.util.rx.RxUtil;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;

public class BedsidePresenter extends BasePresenter<BedsideContract.View> implements BedsideContract
        .Presenter {
    private static final String TAG="HomePresenter";
    private Disposable diDateTimer;

    /**
     * 更新标题栏时间
     */
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
                        if(mView==null){
                            LogUtil.e("mView为空");
                            return;
                        }
                        mView.getDate(DateUtil.getDate("yyyy年MM月dd日"), DateUtil.getDate
                                ("HH:mm:ss"), DateUtil.getWeek(), NetWorkUtil.getNetWorkLine(),
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



    public void onDestroy() {
        RxUtil.closeDisposable(diDateTimer);
    }

}
