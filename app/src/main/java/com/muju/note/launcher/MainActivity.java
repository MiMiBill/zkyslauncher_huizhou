package com.muju.note.launcher;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import com.muju.note.launcher.app.home.bean.AdvertsBean;
import com.muju.note.launcher.app.home.ui.HomeFragment;
import com.muju.note.launcher.app.video.util.WoTvUtil;
import com.muju.note.launcher.base.BaseActivity;
import com.muju.note.launcher.base.BaseFragment;
import com.muju.note.launcher.base.LauncherApplication;
import com.muju.note.launcher.service.MainService;
import com.muju.note.launcher.topics.AdvertsTopics;
import com.muju.note.launcher.util.adverts.NewAdvertsUtil;
import com.muju.note.launcher.util.file.CacheUtil;
import com.muju.note.launcher.view.EBDrawerLayout;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity {

    @BindView(R.id.drawlayout)
    EBDrawerLayout drawlayout;
    @BindView(R.id.iv_ad)
    ImageView ivAd;

    @Override
    public int getLayout() {
        return R.layout.activity_main;
    }

    @Override
    public void initData() {
        startService(new Intent(this, MainService.class));

        BaseFragment fragment = (BaseFragment) findFragment(HomeFragment.class);
        if (fragment == null) {
            loadRootFragment(R.id.fl_container, HomeFragment.newInstance());
        }

        // 登录沃tv
        WoTvUtil.getInstance().initSDK(LauncherApplication.getInstance());

        List<AdvertsBean> adverts = CacheUtil.getDataList(AdvertsTopics.CODE_VERTICAL);

        if(adverts!=null && adverts.size()>0){
            NewAdvertsUtil.getInstance().showByImageView(adverts, ivAd);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
