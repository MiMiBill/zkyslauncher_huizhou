package com.muju.note.launcher.app.orderfood;

import android.widget.TextView;

import com.muju.note.launcher.R;
import com.muju.note.launcher.base.BaseFragment;

import butterknife.BindView;
import butterknife.OnClick;

//金融财富
public class OrderFoodFragment extends BaseFragment {
    @BindView(R.id.tv_title)
    TextView tvTitle;

    @Override
    public int getLayout() {
        return R.layout.fragment_order_food;
    }

    @Override
    public void initData() {
        tvTitle.setText("点餐服务");
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
