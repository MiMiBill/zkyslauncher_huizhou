package com.muju.note.launcher.app.shop;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.muju.note.launcher.R;
import com.muju.note.launcher.app.publicui.NullFragment;
import com.muju.note.launcher.base.BaseFragment;

import butterknife.BindView;
import butterknife.OnClick;

public class ShopFragment extends BaseFragment {
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.rel_titlebar)
    RelativeLayout relTitlebar;
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_back)
    TextView tvBack;
    @BindView(R.id.ll_back)
    LinearLayout llBack;
    @BindView(R.id.ll_main)
    LinearLayout llMain;

    @Override
    public int getLayout() {
        return R.layout.fragment_shop;
    }

    @Override
    public void initData() {
        relTitlebar.setBackgroundColor(getResources().getColor(R.color.white));
        tvTitle.setText("严选好物");

        llMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                start(new NullFragment());
            }
        });
    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void showError(String msg) {

    }


    @OnClick(R.id.ll_back)
    public void onViewClicked() {
        pop();
    }

}
