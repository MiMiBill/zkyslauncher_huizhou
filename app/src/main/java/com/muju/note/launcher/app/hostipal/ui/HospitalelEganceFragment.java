package com.muju.note.launcher.app.hostipal.ui;

import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.muju.note.launcher.R;
import com.muju.note.launcher.base.BaseFragment;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.Unbinder;

//医院风采
public class HospitalelEganceFragment extends BaseFragment {
    private static final String TAG = "HospitalelEganceFragment";
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.ll_back)
    LinearLayout llBack;
    Unbinder unbinder;
    @BindView(R.id.tv_back)
    TextView tvBack;



    @Override
    public int getLayout() {
        return R.layout.fragment_elegance;
    }

    @Override
    public void initData() {
        ivBack.setImageResource(R.mipmap.egan_back);
        tvBack.setTextColor(getResources().getColor(R.color.white));
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
