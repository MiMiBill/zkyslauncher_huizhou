package com.muju.note.launcher.app.hostipal.ui;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.muju.note.launcher.R;
import com.muju.note.launcher.app.hostipal.adapter.HospitalMienAdapter;
import com.muju.note.launcher.app.hostipal.contract.HospitalMienContract;
import com.muju.note.launcher.app.hostipal.db.MienInfoDao;
import com.muju.note.launcher.app.hostipal.presenter.HospitalMienPresenter;
import com.muju.note.launcher.base.BaseFragment;
import com.muju.note.launcher.base.LauncherApplication;
import com.muju.note.launcher.util.ActiveUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


/**
 * 医院风采
 */
public class HospitalMienFragment extends BaseFragment<HospitalMienPresenter> implements
        HospitalMienContract.View, View.OnClickListener {

    private static final String TAG = "HospitalMienFragment";

    public static HospitalMienFragment hospitalMienFragment = null;
    @BindView(R.id.ll_type_title)
    LinearLayout llTypeTitle;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.wv_Mien)
    WebView wvMien;
    @BindView(R.id.tv_type_title)
    TextView tvTypeTitle;
    @BindView(R.id.ll_back)
    LinearLayout llBack;
    @BindView(R.id.ll_null)
    LinearLayout llNull;
    @BindView(R.id.tv_null)
    TextView tvNull;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.rel_titlebar)
    RelativeLayout relTitlebar;
    Unbinder unbinder;

    private HospitalMienAdapter hospitalMienAdapter;
    private List<MienInfoDao> list;


    public static HospitalMienFragment getInstance() {
        if (hospitalMienFragment == null) {
            hospitalMienFragment = new HospitalMienFragment();
        }
        return hospitalMienFragment;
    }

    @Override
    public int getLayout() {
        return R.layout.fragment_hostpital_mien;
    }

    @Override
    public void initData() {
        if(ActiveUtils.getPadActiveInfo()!=null){
            tvTitle.setText(ActiveUtils.getPadActiveInfo().getHospitalName());
        }

        list = new ArrayList<>();
        hospitalMienAdapter = new HospitalMienAdapter(R.layout.rv_item_hospital_mien_type, list);
        recyclerView.setLayoutManager(new LinearLayoutManager(LauncherApplication.getContext(),
                LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(hospitalMienAdapter);

        hospitalMienAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                llNull.setVisibility(View.GONE);
                llTypeTitle.setSelected(false);
                hospitalMienAdapter.setPos(position);
                hospitalMienAdapter.notifyDataSetChanged();
                setWvMien(list.get(position).getIntroduction());
            }
        });

        llTypeTitle.setOnClickListener(this);
        llBack.setOnClickListener(this);
        tvNull.setOnClickListener(this);

        // 查询医院风采数据
        mPresenter.queryMien();


    }

    @Override
    public void initPresenter() {
        mPresenter = new HospitalMienPresenter();
    }

    @Override
    public void showError(String msg) {

    }

    @Override
    public void getMien(List<MienInfoDao> list) {
        llTypeTitle.setVisibility(View.VISIBLE);
        list.get(0).setSelete(true);
        this.list.addAll(list);
        hospitalMienAdapter.notifyDataSetChanged();
        setWvMien(this.list.get(0).getIntroduction());
    }

    @Override
    public void getMienNull() {
        llNull.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_type_title:
                if (llTypeTitle.isSelected()) {
                    recyclerView.setVisibility(View.VISIBLE);
                    tvTypeTitle.setText("收起");
                } else {
                    recyclerView.setVisibility(View.GONE);
                    tvTypeTitle.setText("展开");
                }
                llTypeTitle.setSelected(!llTypeTitle.isSelected());
                break;

            case R.id.ll_back:
                pop();
                break;

            case R.id.tv_null:
                pop();
                break;
        }
    }

    public void setWvMien(String data) {
        if (TextUtils.isEmpty(data)) {
            getMienNull();
            return;
        }
        wvMien.loadDataWithBaseURL(null, data, "text/html", "UTF-8", null);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
