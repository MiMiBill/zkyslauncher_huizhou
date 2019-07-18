package com.muju.note.launcher.app.hostipal.ui;


import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.muju.note.launcher.R;
import com.muju.note.launcher.app.hostipal.adapter.CostAdapter;
import com.muju.note.launcher.app.hostipal.bean.CostBean;
import com.muju.note.launcher.base.BaseFragment;
import com.muju.note.launcher.base.LauncherApplication;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 一日清单
 */
public class CostFragment extends BaseFragment implements View.OnClickListener {

    @BindView(R.id.ll_back)
    LinearLayout llBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.rv_cost)
    RecyclerView rvCost;

    private CostAdapter costAdapter;
    private List<CostBean> costBeanList;

    @Override
    public int getLayout() {
        return R.layout.fragment_new_cost;
    }

    @Override
    public void initData() {
        llBack.setOnClickListener(this);

        costBeanList=new ArrayList<>();
        costAdapter=new CostAdapter(R.layout.item_rv_his_cost,costBeanList);
        rvCost.setLayoutManager(new LinearLayoutManager(LauncherApplication.getContext(), LinearLayoutManager.VERTICAL, false));
        rvCost.setAdapter(costAdapter);

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
        CostBean bean=new CostBean(1,"叶酸片","smg*100片/瓶",3,"片","70.00","西药","甲","2019-07-18");
        costBeanList.add(bean);
        CostBean bean1=new CostBean(2,"葡萄糖注射液","100ml*瓶/瓶",3,"瓶","52.30","西药","乙","2019-07-18");
        costBeanList.add(bean1);
        CostBean bean2=new CostBean(3,"注射用还原型谷","1g*瓶/瓶",2,"片","19.40","西药","甲","2019-07-18");
        costBeanList.add(bean2);
        CostBean bean3=new CostBean(4,"二级护理","--",1,"--","20.00","护理费","甲","2019-07-18");
        costBeanList.add(bean3);
        CostBean bean4=new CostBean(5,"住院夜查费","--",1,"--","10.00","护理费","甲","2019-07-18");
        costBeanList.add(bean4);
        CostBean bean5=new CostBean(6,"普通输液器","--",1,"--","20.00","治疗费","甲","2019-07-18");
        costBeanList.add(bean5);
        CostBean bean6=new CostBean(7,"静脉输液第一级","--",1,"--","30.00","治疗费","乙","2019-07-18");
        costBeanList.add(bean6);
        CostBean bean7=new CostBean(8,"床位","--",1,"--","10.00","床位费","乙","2019-07-18");
        costBeanList.add(bean7);
        CostBean bean8=new CostBean(9,"换药","--",2,"次","20.00","治疗费","乙","2019-07-18");
        costBeanList.add(bean8);
        costAdapter.notifyDataSetChanged();
    }
}
