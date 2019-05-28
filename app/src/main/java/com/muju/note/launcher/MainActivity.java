package com.muju.note.launcher;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.muju.note.launcher.app.home.ui.HomeFragment;
import com.muju.note.launcher.app.hostipal.service.MienService;
import com.muju.note.launcher.app.video.service.VideoService;
import com.muju.note.launcher.app.video.util.WoTvUtil;
import com.muju.note.launcher.base.BaseActivity;
import com.muju.note.launcher.base.BaseFragment;
import com.muju.note.launcher.base.LauncherApplication;
import com.muju.note.launcher.service.MainService;

public class MainActivity extends BaseActivity {

    @Override
    public int getLayout() {
        return R.layout.activity_main;
    }

    @Override
    public void initData() {
        startService(new Intent(this,MainService.class));

        BaseFragment fragment = (BaseFragment) findFragment(HomeFragment.class);
        if (fragment == null) {
            loadRootFragment(R.id.fl_container, HomeFragment.newInstance());
        }

        // 登录沃tv
        WoTvUtil.getInstance().initSDK(LauncherApplication.getInstance());
    }
}
