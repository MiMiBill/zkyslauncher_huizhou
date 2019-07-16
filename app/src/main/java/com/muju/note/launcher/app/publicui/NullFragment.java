package com.muju.note.launcher.app.publicui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.muju.note.launcher.R;
import com.muju.note.launcher.app.home.ui.HomeFragment;
import com.muju.note.launcher.base.BaseFragment;

import butterknife.BindView;

/**
 * 空页面
 */
public class NullFragment extends BaseFragment implements View.OnClickListener {
    @BindView(R.id.ll_back)
    LinearLayout llBack;
    @BindView(R.id.iv_null)
    ImageView ivNull;

    @Override
    public int getLayout() {
        return R.layout.fragment_null;
    }

    @Override
    public void initData() {

        llBack.setOnClickListener(this);
        ivNull.setOnClickListener(this);
    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void showError(String msg) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_back:
                pop();
                break;
            case R.id.iv_null:
                popTo(HomeFragment.class, false);
                break;
        }
    }
}
