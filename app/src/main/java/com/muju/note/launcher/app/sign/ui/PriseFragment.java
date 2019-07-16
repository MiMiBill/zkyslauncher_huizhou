package com.muju.note.launcher.app.sign.ui;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.muju.note.launcher.R;
import com.muju.note.launcher.app.adtask.dialog.RewardDialog;
import com.muju.note.launcher.app.sign.adapter.PriseAdapter;
import com.muju.note.launcher.app.sign.bean.PriseBean;
import com.muju.note.launcher.app.sign.contract.PriseContract;
import com.muju.note.launcher.app.sign.presenter.PrisePresenter;
import com.muju.note.launcher.base.BaseFragment;
import com.muju.note.launcher.base.LauncherApplication;
import com.muju.note.launcher.util.Constants;
import com.muju.note.launcher.util.sp.SPUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class PriseFragment extends BaseFragment<PrisePresenter> implements PriseContract.View {
    @BindView(R.id.ll_back)
    LinearLayout llBack;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    private List<PriseBean.PointListBean> priseBeans = new ArrayList<>();
    private List<PriseBean.GiftListBean> giftBeans = new ArrayList<>();
    private PriseAdapter priseAdapter;
    private RewardDialog rewardDialog;

    @Override
    public int getLayout() {
        return R.layout.activity_prize;
    }

    @Override
    public void initData() {
        priseBeans = SPUtil.getPriseTaskList(Constants.PRISE_TASK_LIST);
        giftBeans = SPUtil.getUserTaskList(Constants.USER_TASK_LIST);
        recyclerView.setLayoutManager(new GridLayoutManager(LauncherApplication.getContext(), 4));
        priseAdapter = new PriseAdapter(R.layout.item_prise, priseBeans);
        recyclerView.setAdapter(priseAdapter);
        initListener();
    }

    private void initListener() {
        priseAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                PriseBean.PointListBean pointListBean = priseBeans.get(position);
                if (pointListBean.getCount() > 0 && giftBeans.get(0).getCount() >= pointListBean.getPoint()) {
                    showRewardDialog(pointListBean);
                }
            }
        });
    }


    //显示兑换dialog
    private void showRewardDialog(final PriseBean.PointListBean pointListBean) {
        rewardDialog = new RewardDialog(getActivity(), "将消耗您" + pointListBean.getPoint() + "积分兑换此奖品")
                .setCustView(R.layout.dialog_reward);
        rewardDialog.setOnRewardListener(new RewardDialog.OnRewardListener() {
            @Override
            public void onReward() {
                mPresenter.useReward(pointListBean.getId(), 1);
            }
        });
        rewardDialog.show();
    }


    @Override
    public void initPresenter() {
        mPresenter = new PrisePresenter();
    }

    @Override
    public void showError(String msg) {

    }


    @OnClick(R.id.ll_back)
    public void onViewClicked() {
        pop();
    }


    private void setGift(PriseBean priseBean) {
        giftBeans.clear();
        giftBeans = priseBean.getGiftList();
        priseBeans.clear();
        priseBeans = priseBean.getPointList();
        priseAdapter.setNewData(priseBean.getPointList());
        SPUtil.saveDataList(Constants.PRISE_TASK_LIST, priseBeans);
        SPUtil.saveDataList(Constants.USER_TASK_LIST, giftBeans);
    }

    @Override
    public void useReward(PriseBean priseBean) {
        setGift(priseBean);
    }

}
