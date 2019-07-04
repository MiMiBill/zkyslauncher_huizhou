package com.muju.note.launcher.app.startUp.ui;

import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.muju.note.launcher.R;
import com.muju.note.launcher.base.BaseFragment;
import com.muju.note.launcher.base.LauncherApplication;
import com.muju.note.launcher.util.app.MobileInfoUtil;
import com.muju.note.launcher.util.net.NetWorkUtil;
import com.muju.note.launcher.util.rx.RxUtil;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import me.yokeyword.fragmentation.ISupportFragment;

/**
 * 初始化的网络检查页面
 */
public class StartCheckNetWorkFragment extends BaseFragment {
    @BindView(R.id.tv_reason)
    TextView tvReason;
    @BindView(R.id.tv_retry)
    TextView tvRetry;
    @BindView(R.id.lly_no_internet)
    LinearLayout llyNoInternet;
    @BindView(R.id.view_hide)
    View viewHide;

    private Disposable diNetWork;

    @Override
    public int getLayout() {
        return R.layout.fragment_startcheck;
    }

    @Override
    public void initData() {
        Observable.interval(1, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Long>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        diNetWork = d;
                    }

                    @Override
                    public void onNext(Long aLong) {
                        checkNet();
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void showError(String msg) {

    }

    private void checkNet() {
        String iccid = MobileInfoUtil.getICCID(LauncherApplication.getContext());
        if (MobileInfoUtil.haveSIMCard(LauncherApplication.getContext()) && !TextUtils.isEmpty(iccid)) {
            boolean isConnect = NetWorkUtil.isConnected(LauncherApplication.getContext());
            if (!isConnect) {
                llyNoInternet.setVisibility(View.VISIBLE);
                tvReason.setText("没有检测到网络哦");
                tvRetry.setText("正在重新检测网络");
            } else {
                RxUtil.closeDisposable(diNetWork);
                if(llyNoInternet!=null){
                    llyNoInternet.setVisibility(View.GONE);
                }
                start(new ActivationFragment(), ISupportFragment.SINGLETASK);
            }
        } else {
            llyNoInternet.setVisibility(View.VISIBLE);
            tvReason.setText("没有检测到手机卡哦");
            tvRetry.setText("正在重新检测");
        }
    }
}
