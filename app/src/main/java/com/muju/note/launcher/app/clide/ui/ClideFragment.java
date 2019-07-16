package com.muju.note.launcher.app.clide.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.muju.note.launcher.R;
import com.muju.note.launcher.app.clide.adapter.ClideAdapter;
import com.muju.note.launcher.app.clide.adapter.ClideHeaderAdapter;
import com.muju.note.launcher.app.clide.contract.ClideContract;
import com.muju.note.launcher.app.clide.presenter.ClidePresenter;
import com.muju.note.launcher.app.video.db.VideoHisDao;
import com.muju.note.launcher.app.video.db.VideoInfoDao;
import com.muju.note.launcher.app.video.ui.VideoFragment;
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

public class ClideFragment extends BaseFragment<ClidePresenter> implements View.OnClickListener, ClideContract.View {
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
    @BindView(R.id.rl_clide)
    RecyclerView rlClide;
    @BindView(R.id.smartRefresh)
    SmartRefreshLayout smartRefresh;

    private RecyclerView rvHeader;

    private int pageNum = 1;
    private List<VideoInfoDao> videoInfoDaos;
    private ClideAdapter clideAdapter;
    private List<VideoInfoDao> headerList;
    private ClideHeaderAdapter clideHeaderAdapter;

    @Override
    public int getLayout() {
        return R.layout.fragment_clide;
    }

    @Override
    public void initData() {
        tvTitle.setText("儿童专栏");
        llBack.setOnClickListener(this);

        videoInfoDaos = new ArrayList<>();
        clideAdapter = new ClideAdapter(R.layout.rv_item_video_content, videoInfoDaos);
        rlClide.setLayoutManager(new GridLayoutManager(LauncherApplication.getContext(), 5));
        rlClide.setAdapter(clideAdapter);

        clideAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                toPlay(videoInfoDaos.get(i));
            }
        });

        addHeader();

        refreshVideo();
        loadMore();

        mPresenter.getHeader("少儿");
    }

    @Override
    public void initPresenter() {
        mPresenter = new ClidePresenter();
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
    public void getClideSuccess(List<VideoInfoDao> list) {
        if (pageNum == 1) {
            videoInfoDaos.clear();
        }
        smartRefresh.finishRefresh();
        clideAdapter.loadMoreComplete();
        videoInfoDaos.addAll(list);
        clideAdapter.notifyDataSetChanged();
    }

    @Override
    public void getClideNull() {
        showToast("数据为空，请检查");
    }

    @Override
    public void getClideEnd() {
        smartRefresh.finishRefresh();
        clideAdapter.loadMoreEnd();
    }

    @Override
    public void getHeaderSuccess(List<VideoInfoDao> list) {
        headerList.clear();
        headerList.addAll(list);
        clideHeaderAdapter.notifyDataSetChanged();
        mPresenter.getCilde("少儿", pageNum);
    }

    /**
     * 刷新数据
     */
    private void refreshVideo() {
        smartRefresh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                pageNum = 1;
                mPresenter.getHeader("少儿");
            }
        });
    }

    /**
     * 加载更多
     */
    private void loadMore() {
        clideAdapter.setEnableLoadMore(true);
        clideAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                pageNum++;
                mPresenter.getCilde("少儿", pageNum);
            }
        });
    }

    /**
     * 添加头部
     */
    private void addHeader() {
        View headerView = LayoutInflater.from(LauncherApplication.getContext()).inflate(R.layout.header_clide, null);
        rvHeader = headerView.findViewById(R.id.rv_clide_top);
        headerList = new ArrayList<>();
        clideHeaderAdapter = new ClideHeaderAdapter(R.layout.rv_item_clide_header, headerList);
        rvHeader.setLayoutManager(new GridLayoutManager(LauncherApplication.getContext(), 3));
        rvHeader.setAdapter(clideHeaderAdapter);
        clideHeaderAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                toPlay(headerList.get(i));
            }
        });
        clideAdapter.addHeaderView(headerView);
    }

    /**
     * 跳转播放
     *
     * @param infoDao
     */
    private void toPlay(VideoInfoDao infoDao) {
        if (!VideoSdkConfig.getInstance().getUser().isLogined()) {
            WoTvUtil.getInstance().login();
            showToast("登入视频中，请稍后");
            return;
        }
        VideoHisDao hisDao = new VideoHisDao();
        hisDao.setCid(infoDao.getCid());
        hisDao.setCustomTag(infoDao.getCustomTag());
        hisDao.setDescription(infoDao.getDescription());
        hisDao.setImgUrl(infoDao.getImgUrl());
        hisDao.setName(infoDao.getName());
        hisDao.setVideoId(infoDao.getVideoId());
        hisDao.setVideoType(infoDao.getVideoType());
        hisDao.setScreenUrl(infoDao.getScreenUrl());
        WotvPlayFragment wotvPlayFragment = new WotvPlayFragment();
        wotvPlayFragment.setHisDao(hisDao);
        start(wotvPlayFragment, ISupportFragment.SINGLETASK);
    }

}
