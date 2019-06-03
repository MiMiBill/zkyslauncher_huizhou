package com.muju.note.launcher.app.lockScreen;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.muju.note.launcher.R;
import com.muju.note.launcher.app.home.bean.AdvertsBean;
import com.muju.note.launcher.base.BaseActivity;
import com.muju.note.launcher.topics.AdvertsTopics;
import com.muju.note.launcher.util.FormatUtils;
import com.muju.note.launcher.util.adverts.NewAdvertsUtil;
import com.muju.note.launcher.util.file.CacheUtil;
import com.muju.note.launcher.util.rx.RxUtil;
import com.muju.note.launcher.util.system.SystemUtils;
import com.muju.note.launcher.view.banana.Banner;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;


/**
 * 屏幕保护程序
 */
public class ProtectionProcessActivity extends BaseActivity {
    @BindView(R.id.tvTime)
    TextView tvTime;
    @BindView(R.id.tvDate)
    TextView tvDate;
    @BindView(R.id.ivBtn)
    ImageView ivBtn;
    @BindView(R.id.iv_launcher)
    ImageView ivLauncher;
    @BindView(R.id.banner_lc)
    Banner bannerLc;
    //调整亮度
    Disposable disposableAdjust;
    //时间自动增加
    private Disposable disposableTimeAdd;
    private List<AdvertsBean> adverts=new ArrayList<>();


    public static void launch(Context context) {
        Intent intent = new Intent(context, ProtectionProcessActivity.class);
        context.startActivity(intent);
    }


    @Override
    public int getLayout() {
        return R.layout.activity_protection_process;
    }

    @Override
    public void initData() {
        hideActionBar();
        lowerBrightness();

//        setStartProtection(false);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{Manifest.permission.WRITE_SETTINGS}, 0x01);
        }
        ivBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addBrightness();
                finish();
            }
        });
        setTime();
        queryNewAdverts();
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
        Window window = this.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.screenBrightness = (float) v;
        window.setAttributes(lp);
    }

//    @Override
//    public boolean dispatchTouchEvent(MotionEvent ev) {
//        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
//            LogUtil.d("ev.getAction():%s", ev.getAction());
//            addBrightness();
//            adjustDownCount();
//        }
//        return super.dispatchTouchEvent(ev);
//    }



    public void setTime() {
        tvTime.setText(FormatUtils.FormatDateUtil.formatDateHHmm());
        tvDate.setText(FormatUtils.FormatDateUtil.formatDateWeek());

        RxUtil.closeDisposable(disposableTimeAdd);
        disposableTimeAdd = Observable.interval(15, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        tvTime.setText(FormatUtils.FormatDateUtil.formatDateHHmm());
                        tvDate.setText(FormatUtils.FormatDateUtil.formatDateWeek());
                    }
                });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        RxUtil.closeDisposable(disposableTimeAdd);
        RxUtil.closeDisposable(disposableAdjust);
//        setStartProtection(true);
        bannerLc.destroy();
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
