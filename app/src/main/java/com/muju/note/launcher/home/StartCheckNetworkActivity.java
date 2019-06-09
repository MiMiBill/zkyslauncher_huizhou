package com.muju.note.launcher.home;

import android.content.Intent;

import com.muju.note.launcher.R;
import com.muju.note.launcher.app.startUp.ui.ActivationActivity;
import com.muju.note.launcher.base.BaseActivity;

/**
 *  开机页
 */
public class StartCheckNetworkActivity extends BaseActivity {
    @Override
    public int getLayout() {
        return R.layout.activity_startcheck;
    }

    @Override
    public void initData() {

        startActivity(new Intent(this, ActivationActivity.class));

    }
}
