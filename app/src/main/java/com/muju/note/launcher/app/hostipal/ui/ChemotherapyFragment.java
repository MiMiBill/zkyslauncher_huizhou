package com.muju.note.launcher.app.hostipal.ui;


import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.muju.note.launcher.R;
import com.muju.note.launcher.app.hostipal.adapter.ChemotherapyAdapter;
import com.muju.note.launcher.app.hostipal.bean.ChemotherapyBean;
import com.muju.note.launcher.base.BaseFragment;
import com.muju.note.launcher.base.LauncherApplication;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;


public class ChemotherapyFragment extends BaseFragment implements View.OnClickListener {

    @BindView(R.id.ll_back)
    LinearLayout llBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.rv_chemotherapy)
    RecyclerView rvChemotherapy;

    private ChemotherapyAdapter chemotherapyAdapter;
    private List<ChemotherapyBean> chemotherapyBeanList;

    @Override
    public int getLayout() {
        return R.layout.fragment_new_chemotherapy;
    }

    @Override
    public void initData() {
        llBack.setOnClickListener(this);

        chemotherapyBeanList=new ArrayList<>();
        chemotherapyAdapter=new ChemotherapyAdapter(R.layout.item_rv_chemotherapy,chemotherapyBeanList);
        rvChemotherapy.setLayoutManager(new LinearLayoutManager(LauncherApplication.getContext(),LinearLayoutManager.VERTICAL,false));
        rvChemotherapy.setAdapter(chemotherapyAdapter);

        addData();
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

    private void addData(){
        ChemotherapyBean bean=new ChemotherapyBean("白细胞","2.9","3.5-9.5","10.9L","白细胞","6.4","3.5-9.5","10.9L");
        chemotherapyBeanList.add(bean);
        ChemotherapyBean bean1=new ChemotherapyBean("红细胞","4.12","3.8-5.1","10.12L","红细胞","6.4","3.8-5.1","10.12L");
        chemotherapyBeanList.add(bean1);
        ChemotherapyBean bean2=new ChemotherapyBean("血红蛋白","104","115-150","G/L","血红蛋白","148","115-150","G/L");
        chemotherapyBeanList.add(bean2);
        ChemotherapyBean bean3=new ChemotherapyBean("平均RBC体积","31.4","33-45","%","平均RBC体积","33.4","33-45","%");
        chemotherapyBeanList.add(bean3);
        ChemotherapyBean bean4=new ChemotherapyBean("平均HGB量","76.9","82-100","FL","平均HGB量","90.4","82-100","FL");
        chemotherapyBeanList.add(bean4);
        ChemotherapyBean bean5=new ChemotherapyBean("平均RGB量","22.9","27-34","PG","平均RGB量","30.1","27-34","PG");
        chemotherapyBeanList.add(bean5);
        ChemotherapyBean bean6=new ChemotherapyBean("血小板","331","316-354","G/L","血小板","331","316-354","G/L");
        chemotherapyBeanList.add(bean6);
        ChemotherapyBean bean7=new ChemotherapyBean("红细胞宽度标准差","103.3","125-350","10.9L","红细胞宽度标准差","220.9","125-350","10.9L");
        chemotherapyBeanList.add(bean7);
        ChemotherapyBean bean8=new ChemotherapyBean("红细胞变化系数","15.5","40-53","FL","红细胞变化系数","50","40-53","FL");
        chemotherapyBeanList.add(bean8);
        ChemotherapyBean bean9=new ChemotherapyBean("血小板体积","10.9","9.8-17","%","血小板体积","14.6","9.8-17","%");
        chemotherapyBeanList.add(bean9);
        ChemotherapyBean bean10=new ChemotherapyBean("血小板压和","9.9","6.8-13.5","%","血小板压和","10.6","6.8-13.5","%");
        chemotherapyBeanList.add(bean10);
        chemotherapyAdapter.notifyDataSetChanged();
    }
}
