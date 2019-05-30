package com.muju.note.launcher;

import android.content.Intent;
import android.support.v4.widget.DrawerLayout;

import com.muju.note.launcher.app.home.ui.HomeFragment;
import com.muju.note.launcher.app.video.service.VideoService;
import com.muju.note.launcher.app.video.util.WoTvUtil;
import com.muju.note.launcher.base.BaseActivity;
import com.muju.note.launcher.base.BaseFragment;
import com.muju.note.launcher.base.LauncherApplication;
import com.muju.note.launcher.service.MainService;
import com.muju.note.launcher.view.EBDrawerLayout;

import butterknife.BindView;

public class MainActivity extends BaseActivity {

    @BindView(R.id.drawlayout)
    EBDrawerLayout drawlayout;

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

        drawlayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);

    }

}
