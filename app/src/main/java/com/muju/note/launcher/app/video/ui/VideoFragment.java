package com.muju.note.launcher.app.video.ui;

import android.os.CountDownTimer;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.muju.note.launcher.R;
import com.muju.note.launcher.app.video.adapter.VideoPageAdapter;
import com.muju.note.launcher.app.video.bean.PayEntity;
import com.muju.note.launcher.app.video.bean.PayEvent;
import com.muju.note.launcher.app.video.contract.VideoContract;
import com.muju.note.launcher.app.video.db.PayInfoDao;
import com.muju.note.launcher.app.video.db.VideoColumnsDao;
import com.muju.note.launcher.app.video.event.VideoFinishEvent;
import com.muju.note.launcher.app.video.presenter.VideoPresenter;
import com.muju.note.launcher.base.BaseFragment;
import com.muju.note.launcher.base.LauncherApplication;
import com.muju.note.launcher.litepal.LitePalDb;
import com.muju.note.launcher.util.DateUtil;
import com.muju.note.launcher.util.FormatUtils;
import com.muju.note.launcher.util.log.LogUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.litepal.LitePal;
import org.litepal.crud.callback.FindMultiCallback;

import java.util.List;

import butterknife.BindView;

/**
 * 视频列表
 */
public class VideoFragment extends BaseFragment<VideoPresenter> implements View.OnClickListener, VideoContract.View {

    private static final String TAG = "VideoFragment";

    public static VideoFragment videoContentFragment;
    @BindView(R.id.ll_back)
    LinearLayout llBack;
    @BindView(R.id.tv_null)
    TextView tvNull;
    @BindView(R.id.ll_null)
    LinearLayout llNull;
    @BindView(R.id.viewPager)
    ViewPager viewPager;
    @BindView(R.id.tabLayout)
    TabLayout tabLayout;
    @BindView(R.id.tv_his)
    TextView tvHis;
    @BindView(R.id.vip_time_layout)
    LinearLayout vipTimeLayout;
    private CountDownTimer countDownTimer;
    private VideoPageAdapter pageAdapter;
    @BindView(R.id.vip_time)
    TextView vipTime;

    public static VideoFragment getIntance() {
        if (videoContentFragment == null) {
            videoContentFragment = new VideoFragment();
        }
        return videoContentFragment;
    }

    @Override
    public int getLayout() {
        return R.layout.fragment_video;
    }

    @Override
    public void initData() {

        EventBus.getDefault().register(this);

        llBack.setOnClickListener(this);
        tvNull.setOnClickListener(this);
        tvHis.setOnClickListener(this);

        mPresenter.queryColumns();

    }

    @Override
    public void initPresenter() {
        mPresenter = new VideoPresenter();
    }

    @Override
    public void showError(String msg) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_back:
            case R.id.tv_null:
                pop();
                break;
            case R.id.tv_his:
                start(new VideoHisFragment());
                break;
        }
    }

    @Override
    public void getColumnsSuccess(List<VideoColumnsDao> list) {
        for (VideoColumnsDao dao : list) {
            tabLayout.addTab(tabLayout.newTab().setText(dao.getName()));
        }
        pageAdapter = new VideoPageAdapter(getChildFragmentManager(), list);
        viewPager.setAdapter(pageAdapter);
        tabLayout.setupWithViewPager(viewPager);

        // 设置第一个tab选中
        TabLayout.Tab tab = tabLayout.getTabAt(tabLayout.getSelectedTabPosition());
        View view = LayoutInflater.from(LauncherApplication.getContext()).inflate(R.layout.tablayout_video_columns, null);
        TextView tvTitle = view.findViewById(R.id.tv_title);
        tvTitle.setText(tab.getText());
        tab.setCustomView(tvTitle);

        // 设置tablayout选中字体大小
        setTabLayoutTextSize();
        setVIPTime();
    }

    @Override
    public void getColumnsNull() {
        llNull.setVisibility(View.VISIBLE);
    }

    /**
     * 设置tablayout字体大小
     */
    private void setTabLayoutTextSize() {
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                View view = LayoutInflater.from(LauncherApplication.getContext()).inflate(R.layout.tablayout_video_columns, null);
                TextView tvTitle = view.findViewById(R.id.tv_title);
                tvTitle.setText(tab.getText());
                tab.setCustomView(tvTitle);
            }


            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                tab.setCustomView(null);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void finish(VideoFinishEvent event) {
        pop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        if (countDownTimer != null) {
            countDownTimer.cancel();
            countDownTimer = null;
        }
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(PayEvent event) {
        LogUtil.e(TAG, event.getOrderType() + "");
        if (event.getOrderType() == PayEntity.ORDER_TYPE_VIDEO || event.getOrderType() == PayEntity.ORDER_TYPE_VIDEO_PREMIUM) {
            setVIPTime();
        }
    }

    private void setVIPTime() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
            countDownTimer = null;
        }

        LitePalDb.setZkysDb();
        LitePal.where("expireTime > 0").findAsync(PayInfoDao.class).listen(new FindMultiCallback<PayInfoDao>() {
            @Override
            public void onFinish(List<PayInfoDao> list) {
                if(list!=null && list.size()>0){
                    PayInfoDao payInfoDao = list.get(0);
                    if(payInfoDao!=null && payInfoDao.getExpireTime()!=null){
                        countDownTimer = new CountDownTimer(FormatUtils.FormatDateUtil.parseDate(payInfoDao.getExpireTime()).getTime() - System.currentTimeMillis(), 1000) {
                            @Override
                            public void onTick(long millisUntilFinished) {
                                if(vipTime!=null && millisUntilFinished>0){
                                    vipTimeLayout.setVisibility(View.VISIBLE);
                                    vipTime.setText(DateUtil.diffTime(millisUntilFinished));
                                }
                            }

                            @Override
                            public void onFinish() {
                                vipTimeLayout.setVisibility(View.GONE);
                            }
                        };
                        countDownTimer.start();
                    }
                }else {
                    vipTimeLayout.setVisibility(View.GONE);
                }
            }
        });



       /* PayEntity entity = PayUtils.getPayEntity(PayEntity.ORDER_TYPE_VIDEO);
        if (entity == null) {
            vipTimeLayout.setVisibility(View.GONE);
        } else {
            countDownTimer = new CountDownTimer(FormatUtils.FormatDateUtil.parseDate(entity.getExpireTime()).getTime() - System.currentTimeMillis(), 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    if(vipTime!=null && millisUntilFinished>0){
                        vipTimeLayout.setVisibility(View.VISIBLE);
                        vipTime.setText(DateUtil.diffTime(millisUntilFinished));
                    }
                }

                @Override
                public void onFinish() {
                    vipTimeLayout.setVisibility(View.GONE);
                }
            };
            countDownTimer.start();
        }*/

    }
}
