package com.muju.note.launcher.app.insurance;

import android.widget.TextView;

import com.muju.note.launcher.R;
import com.muju.note.launcher.base.BaseFragment;

import butterknife.BindView;
import butterknife.OnClick;

//保险服务
public class InsureanceFragment extends BaseFragment {
    @BindView(R.id.tv_title)
    TextView tvTitle;

    @Override
    public int getLayout() {
        return R.layout.fragment_insureance;
    }

    @Override
    public void initData() {
        tvTitle.setText("保险服务");
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
