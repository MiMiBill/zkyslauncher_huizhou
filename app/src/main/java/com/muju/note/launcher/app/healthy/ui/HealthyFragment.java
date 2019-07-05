package com.muju.note.launcher.app.healthy.ui;

import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.muju.note.launcher.R;
import com.muju.note.launcher.app.healthy.adapter.HealthyAdapter;
import com.muju.note.launcher.app.healthy.contract.HealthyContract;
import com.muju.note.launcher.app.healthy.presenter.HealthyPresenter;
import com.muju.note.launcher.app.video.db.VideoHisDao;
import com.muju.note.launcher.app.video.db.VideoInfoDao;
import com.muju.note.launcher.app.video.ui.WotvPlayFragment;
import com.muju.note.launcher.app.video.util.WoTvUtil;
import com.muju.note.launcher.base.BaseFragment;
import com.muju.note.launcher.base.LauncherApplication;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.unicom.common.VideoSdkConfig;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import me.yokeyword.fragmentation.ISupportFragment;

public class HealthyFragment extends BaseFragment<HealthyPresenter> implements View.OnClickListener, HealthyContract.View {


    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_back)
    TextView tvBack;
    @BindView(R.id.ll_back)
    LinearLayout llBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.rel_titlebar)
    RelativeLayout relTitlebar;
    @BindView(R.id.rv_healthy)
    RecyclerView rvHealthy;
    @BindView(R.id.smartRefresh)
    SmartRefreshLayout smartRefresh;

    private int pageNum = 1;
    private List<VideoInfoDao> videoInfoDaos;
    private HealthyAdapter healthyAdapter;

    @Override
    public int getLayout() {
        return R.layout.fragment_healthy;
    }

    @Override
    public void initData() {
        tvTitle.setText("健康资讯");
        llBack.setOnClickListener(this);

        videoInfoDaos=new ArrayList<>();
        healthyAdapter=new HealthyAdapter(R.layout.rv_item_healthy,videoInfoDaos);
        GridLayoutManager gridLayoutManager=new GridLayoutManager(LauncherApplication.getContext(), 3);
        gridLayoutManager.offsetChildrenHorizontal(20);
        rvHealthy.setLayoutManager(gridLayoutManager);
        rvHealthy.setAdapter(healthyAdapter);

        healthyAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                toPlay(videoInfoDaos.get(i));
            }
        });

        refreshVideo();
        loadMore();

        mPresenter.getHealthy("健康",pageNum);
    }

    @Override
    public void initPresenter() {
        mPresenter=new HealthyPresenter();
    }

    @Override
    public void showError(String msg) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_back:
                pop();
                break;
        }
    }

    @Override
    public void getHealthySuccess(List<VideoInfoDao> list) {
        if(pageNum==1){
            videoInfoDaos.clear();
        }
        smartRefresh.finishRefresh();
        healthyAdapter.loadMoreComplete();
        videoInfoDaos.addAll(list);
        healthyAdapter.notifyDataSetChanged();
    }

    @Override
    public void getHealthyNull() {
        showToast("数据为空，请检查");
    }

    @Override
    public void getHealthyEnd() {
        smartRefresh.finishRefresh();
        healthyAdapter.loadMoreEnd();
    }

    /**
     *  刷新数据
     */
    private void refreshVideo(){
        smartRefresh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                pageNum=1;
                mPresenter.getHealthy("健康",pageNum);
            }
        });
    }

    /**
     *  加载更多
     */
    private void loadMore(){
        healthyAdapter.setEnableLoadMore(true);
        healthyAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                pageNum++;
                mPresenter.getHealthy("健康",pageNum);
            }
        });
    }

    /**
     *  跳转播放
     * @param infoDao
     */
    private void toPlay(VideoInfoDao infoDao){
        if (!VideoSdkConfig.getInstance().getUser().isLogined()) {
            WoTvUtil.getInstance().login();
            showToast("登入视频中，请稍后");
            return;
        }
        VideoHisDao hisDao=new VideoHisDao();
        hisDao.setCid(infoDao.getCid());
        hisDao.setCustomTag(infoDao.getCustomTag());
        hisDao.setDescription(infoDao.getDescription());
        hisDao.setImgUrl(infoDao.getImgUrl());
        hisDao.setName(infoDao.getName());
        hisDao.setVideoId(infoDao.getVideoId());
        hisDao.setVideoType(infoDao.getVideoType());
        hisDao.setScreenUrl(infoDao.getScreenUrl());
        WotvPlayFragment wotvPlayFragment=new WotvPlayFragment();
        wotvPlayFragment.setHisDao(hisDao);
        start(wotvPlayFragment, ISupportFragment.SINGLETASK);
    }
}
