package com.muju.note.launcher.app.publicui.ui;

import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.muju.note.launcher.R;
import com.muju.note.launcher.app.home.db.AdvertsCodeDao;
import com.muju.note.launcher.app.publicui.contract.ProtectionContract;
import com.muju.note.launcher.app.publicui.presenter.ProtectionPresenter;
import com.muju.note.launcher.app.video.event.VideoNoLockEvent;
import com.muju.note.launcher.base.BaseFragment;
import com.muju.note.launcher.topics.AdvertsTopics;
import com.muju.note.launcher.util.FormatUtils;
import com.muju.note.launcher.util.adverts.AdvertsUtil;
import com.muju.note.launcher.util.rx.RxUtil;
import com.muju.note.launcher.util.system.SystemUtils;
import com.muju.note.launcher.view.banana.Banner;

import org.greenrobot.eventbus.EventBus;

import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * 锁屏轮播界面
 */
public class ProtectionProcessFragment extends BaseFragment<ProtectionPresenter> implements ProtectionContract.View {
    @BindView(R.id.iv_launcher)
    ImageView ivLauncher;
    @BindView(R.id.banner_lc)
    Banner bannerLc;
    @BindView(R.id.tvTime)
    TextView tvTime;
    @BindView(R.id.tvDate)
    TextView tvDate;
    @BindView(R.id.ivBtn)
    ImageView ivBtn;
    //调整亮度
    Disposable disposableAdjust;
    //时间自动增加
    private Disposable disposableTimeAdd;

    @Override
    public int getLayout() {
        return R.layout.fragment_protection_process;
    }

    @Override
    public void initData() {
        lowerBrightness();
        ivBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addBrightness();
                pop();
            }
        });
        setTime();
        queryNewAdverts();
    }

    @Override
    public void initPresenter() {
        mPresenter=new ProtectionPresenter();
    }


    @Override
    public void showError(String msg) {

    }

    /**
     * 降低亮度
     */
    private void lowerBrightness() {
        setBrightness(0.2);
    }

    /**
     * 增加亮度
     */
    private void addBrightness() {
        setBrightness(SystemUtils.getScreenBrightness());
    }

    /**
     * 设置亮度
     *
     * @param v
     */
    private void setBrightness(double v) {
        Window window = getActivity().getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.screenBrightness = (float) v;
        window.setAttributes(lp);
    }

    /**
     *  设置时间
     */
    public void setTime() {
        tvTime.setText(FormatUtils.FormatDateUtil.formatDateHHmm());
        tvDate.setText(FormatUtils.FormatDateUtil.formatDateWeek());

        RxUtil.closeDisposable(disposableTimeAdd);
        disposableTimeAdd = Observable.interval(15, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        try {
                            tvTime.setText(FormatUtils.FormatDateUtil.formatDateHHmm());
                            tvDate.setText(FormatUtils.FormatDateUtil.formatDateWeek());
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        RxUtil.closeDisposable(disposableTimeAdd);
        RxUtil.closeDisposable(disposableAdjust);
        if(bannerLc!=null){
            bannerLc.destroy();
        }
    }

    /**
     *  界面可见时
     */
    @Override
    public void onSupportVisible() {
        super.onSupportVisible();
        if(bannerLc!=null){
            bannerLc.startAutoPlay();
        }
        EventBus.getDefault().post(new VideoNoLockEvent(false));
    }

    /**
     *  界面不可见时
     */
    @Override
    public void onSupportInvisible() {
        super.onSupportInvisible();
        if(bannerLc!=null){
            bannerLc.stopAutoPlay();
        }
        EventBus.getDefault().post(new VideoNoLockEvent(true));
        addBrightness();
    }

    /**
     * 查询广告列表
     */
    private void queryNewAdverts() {
        mPresenter.getLockBananaList(AdvertsTopics.CODE_LOCK);
    }


    @Override
    public void getLockBananaList(List<AdvertsCodeDao> list) {
        AdvertsUtil.getInstance().showByDbBanner(list,bannerLc);
        RxUtil.closeDisposable(disposableAdjust);
        addBrightness();
        ivLauncher.setVisibility(View.GONE);
    }

    @Override
    public void getLockBananaNull() {
        AdvertsUtil.getInstance().showDefaultBanner(bannerLc,0);
    }
}
