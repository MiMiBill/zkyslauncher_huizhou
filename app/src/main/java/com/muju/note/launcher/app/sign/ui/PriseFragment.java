package com.muju.note.launcher.app.sign.ui;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.muju.note.launcher.R;
import com.muju.note.launcher.app.sign.adapter.PriseAdapter;
import com.muju.note.launcher.app.sign.bean.PriseBean;
import com.muju.note.launcher.app.sign.contract.PriseContract;
import com.muju.note.launcher.app.sign.presenter.PrisePresenter;
import com.muju.note.launcher.base.BaseFragment;
import com.muju.note.launcher.base.LauncherApplication;
import com.muju.note.launcher.util.user.UserUtil;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

public class PriseFragment extends BaseFragment<PrisePresenter> implements PriseContract.View {
    @BindView(R.id.ll_back)
    LinearLayout llBack;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    private ArrayList<PriseBean> priseBeans=new ArrayList<>();
    private PriseAdapter priseAdapter;

    @Override
    public int getLayout() {
        return R.layout.activity_prize;
    }

    @Override
    public void initData() {
        mPresenter.getPointList(UserUtil.getUserBean().getId());

        recyclerView.setLayoutManager(new GridLayoutManager(LauncherApplication.getContext(), 4));
        priseAdapter = new PriseAdapter(R.layout.item_prise,priseBeans);
        recyclerView.setAdapter(priseAdapter);
        initListener();
    }

    private void initListener() {
        priseAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                PriseBean priseBean = priseBeans.get(position);

            }
        });
    }

    @Override
    public void initPresenter() {
        mPresenter=new PrisePresenter();
    }

    @Override
    public void showError(String msg) {

    }



    @OnClick(R.id.ll_back)
    public void onViewClicked() {
        pop();
    }


    @Override
    public void setPrise(ArrayList<PriseBean> priseList) {
        if(priseList!=null){
            priseBeans.clear();
            priseBeans.addAll(priseList);
            priseAdapter.setNewData(priseBeans);
        }
    }
}
