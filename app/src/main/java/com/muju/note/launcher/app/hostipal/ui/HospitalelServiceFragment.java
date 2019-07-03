package com.muju.note.launcher.app.hostipal.ui;


import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.muju.note.launcher.R;
import com.muju.note.launcher.app.hostipal.adapter.ServiceAdapter;
import com.muju.note.launcher.app.hostipal.bean.Entity;
import com.muju.note.launcher.base.BaseFragment;
import com.muju.note.launcher.base.LauncherApplication;
import com.muju.note.launcher.util.toast.FancyToast;
import com.muju.note.launcher.view.SpaceItemDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

//医院服务
public class HospitalelServiceFragment extends BaseFragment {
    private static final String TAG = "HospitalelEganceFragment";
    @BindView(R.id.ll_back)
    LinearLayout llBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.rel_titlebar)
    RelativeLayout relTitlebar;
    @BindView(R.id.rl)
    RecyclerView rl;
    private ServiceAdapter serviceAdapter;


    @Override
    public int getLayout() {
        return R.layout.fragment_service_navi;
    }

    @Override
    public void initData() {
        tvTitle.setText("医院服务");
        relTitlebar.setBackgroundColor(getResources().getColor(R.color.white));
        initServices();
    }


    private void initServices() {
        rl.setLayoutManager(new GridLayoutManager(LauncherApplication.getInstance(), 5));
        final List<Entity> data = new ArrayList<>();

        data.add(new Entity(R.mipmap.icon_cost_query,null));
        data.add(new Entity(R.mipmap.icon_laboratory_notice, null));
        data.add(new Entity(R.mipmap.icon_examination_report, null));
        data.add(new Entity(R.mipmap.icon_be_careful, null));
        data.add(new Entity(R.mipmap.icon_drug_use, null));
        serviceAdapter = new ServiceAdapter(data);

        rl.addItemDecoration(new SpaceItemDecoration(32, 0, 146, 0));
        rl.setAdapter(serviceAdapter);
        serviceAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if(position==0){
                    start(new CostFragment());
                }else if(position==2){
                    start(new ChemotherapyFragment());
                }else {
                    FancyToast.makeText(LauncherApplication.getInstance(), "更多功能,敬请期待", FancyToast.LENGTH_SHORT).show();
                }
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
