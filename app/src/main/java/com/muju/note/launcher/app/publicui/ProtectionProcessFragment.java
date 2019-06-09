package com.muju.note.launcher.app.publicui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.muju.note.launcher.R;
import com.muju.note.launcher.app.home.bean.AdvertsBean;
import com.muju.note.launcher.app.video.event.VideoNoLockEvent;
import com.muju.note.launcher.base.BaseFragment;
import com.muju.note.launcher.topics.AdvertsTopics;
import com.muju.note.launcher.util.FormatUtils;
import com.muju.note.launcher.util.adverts.NewAdvertsUtil;
import com.muju.note.launcher.util.file.CacheUtil;
import com.muju.note.launcher.util.rx.RxUtil;
import com.muju.note.launcher.util.system.SystemUtils;
import com.muju.note.launcher.view.banana.Banner;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * 锁屏轮播界面
 */
public class ProtectionProcessFragment extends BaseFragment {
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
    private List<AdvertsBean> adverts=new ArrayList<>();

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
        adverts = CacheUtil.getDataList(AdvertsTopics.CODE_LOCK);
        if(adverts!=null && adverts.size()>0){
            NewAdvertsUtil.getInstance().showByBanner(adverts,bannerLc);
        }
        NewAdvertsUtil.getInstance().setOnBannerSuccessLisinter(new NewAdvertsUtil
                .OnBannerSuccessLisinter() {
            @Override
            public void success() {
                List<AdvertsBean> adverts = CacheUtil.getDataList(AdvertsTopics.CODE_LOCK);
                NewAdvertsUtil.getInstance().showByBanner(adverts,bannerLc);
                RxUtil.closeDisposable(disposableAdjust);
                addBrightness();
                ivLauncher.setVisibility(View.GONE);
            }
        });

        NewAdvertsUtil.getInstance().setOnBannerFailLisinter(new NewAdvertsUtil.OnBannerFailLisinter() {
            @Override
            public void fail() {
                NewAdvertsUtil.getInstance().showDefaultBanner(bannerLc,0);
            }
        });
        if (adverts.size()==0){
            NewAdvertsUtil.getInstance().showDefaultBanner(bannerLc,0);
        }
    }

}
