package com.muju.note.launcher.app.hostipal.ui;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.muju.note.launcher.R;
import com.muju.note.launcher.app.hostipal.adapter.DrugsAdapter;
import com.muju.note.launcher.app.hostipal.bean.DrugsBean;
import com.muju.note.launcher.app.hostipal.bean.DrugsSubBean;
import com.muju.note.launcher.base.BaseFragment;
import com.muju.note.launcher.base.LauncherApplication;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class DrugsFragment extends BaseFragment implements View.OnClickListener {
    @BindView(R.id.ll_back)
    LinearLayout llBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.rv_drugs)
    RecyclerView rvDrugs;

    private List<DrugsBean> drugsBeanList;
    private DrugsAdapter drugsAdapter;

    @Override
    public int getLayout() {
        return R.layout.fragment_drugs;
    }

    @Override
    public void initData() {
        llBack.setOnClickListener(this);

        drugsBeanList=new ArrayList<>();
        drugsAdapter=new DrugsAdapter(R.layout.item_rv_drugs,drugsBeanList);
        rvDrugs.setLayoutManager(new LinearLayoutManager(LauncherApplication.getContext(),LinearLayoutManager.VERTICAL,false));
        rvDrugs.setAdapter(drugsAdapter);

        addDate();
    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void showError(String msg) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ll_back:
                pop();
                break;
        }
    }

    private void addDate(){
        for (int i=0;i<4;i++){
            DrugsSubBean subBean=new DrugsSubBean(1,"盐酸左氧氟沙星","用法：每日服2次 每4小时一次 每次服3粒",1);
            DrugsSubBean subBean1=new DrugsSubBean(2,"胶体洒酸秘","用法：每日服2次 每4小时一次 每次服3粒",1);
            DrugsSubBean subBean2=new DrugsSubBean(3,"芙药师","用法：每日服2次 每4小时一次",2);
            List<DrugsSubBean> subBeanList=new ArrayList<>();
            subBeanList.add(subBean);
            subBeanList.add(subBean1);
            subBeanList.add(subBean2);
            DrugsBean bean=new DrugsBean("2019年7月21日  16:32","吃药提醒",subBeanList);
            drugsBeanList.add(bean);
        }
        drugsAdapter.notifyDataSetChanged();
    }
}
