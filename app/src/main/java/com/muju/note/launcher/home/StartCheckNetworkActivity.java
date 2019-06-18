package com.muju.note.launcher.home;

import android.widget.FrameLayout;

import com.muju.note.launcher.R;
import com.muju.note.launcher.app.startUp.ui.StartCheckNetWorkFragment;
import com.muju.note.launcher.base.BaseActivity;
import com.muju.note.launcher.base.BaseFragment;
import com.muju.note.launcher.util.log.LogUtil;

import butterknife.BindView;

/**
 * 开机页
 */
public class StartCheckNetworkActivity extends BaseActivity {


    @BindView(R.id.fl_container)
    FrameLayout flContainer;

    @Override
    public int getLayout() {
        return R.layout.activity_startcheck;
    }

    @Override
    public void initData() {
        BaseFragment fragment = (BaseFragment) findFragment(StartCheckNetWorkFragment.class);
        if (fragment == null) {
            loadRootFragment(R.id.fl_container, new StartCheckNetWorkFragment());
        }
    }

    @Override
    public void initPresenter() {

    }

}
