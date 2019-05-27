package com.muju.note.launcher.app.video.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.muju.note.launcher.R;
import com.muju.note.launcher.app.video.adapter.VideoContentAdapter;
import com.muju.note.launcher.app.video.contract.VideoContentContract;
import com.muju.note.launcher.app.video.db.VideoInfoDao;
import com.muju.note.launcher.app.video.event.VideoFinishEvent;
import com.muju.note.launcher.app.video.presenter.VideoContentPresenter;
import com.muju.note.launcher.base.BaseFragment;
import com.muju.note.launcher.base.LauncherApplication;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * 视频列表
 */
public class VideoContentFragment extends BaseFragment<VideoContentPresenter> implements View.OnClickListener, VideoContentContract.View {

    private static final String TAG = "VideoContentFragment";
    private static final String COLUMNS_ID = "columnsId";
    private static final String COLUMNS_NAME = "columnsName";

    @BindView(R.id.rv_video_view)
    RecyclerView rvVideoView;
    @BindView(R.id.tv_null)
    TextView tvNull;
    @BindView(R.id.ll_null)
    LinearLayout llNull;
    @BindView(R.id.smartRefresh)
    SmartRefreshLayout smartRefresh;

    private int columnsId;
    private String columnName;
    private int pageNum = 1;

    private List<VideoInfoDao> videoInfoDaos;
    private VideoContentAdapter contentAdapter;

    public static VideoContentFragment newInstance(int id, String name) {
        Bundle args = new Bundle();
        args.putInt(COLUMNS_ID, id);
        args.putString(COLUMNS_NAME, name);
        VideoContentFragment fragment = new VideoContentFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getLayout() {
        return R.layout.fragment_video_content;
    }

    @Override
    public void initData() {
        Bundle args = getArguments();
        if (args != null) {
            columnsId = args.getInt(COLUMNS_ID);
            columnName = args.getString(COLUMNS_NAME);
        }

        tvNull.setOnClickListener(this);

        videoInfoDaos = new ArrayList<>();
        contentAdapter = new VideoContentAdapter(R.layout.rv_item_video_content, videoInfoDaos);
        rvVideoView.setLayoutManager(new GridLayoutManager(LauncherApplication.getContext(), 5));
        rvVideoView.setAdapter(contentAdapter);

        mPresenter.queryVideo(columnsId,columnName,pageNum);
        refreshVideo();
        loadMore();
    }

    @Override
    public void initPresenter() {
        mPresenter = new VideoContentPresenter();
    }

    @Override
    public void showError(String msg) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_null:
                EventBus.getDefault().post(new VideoFinishEvent());
                break;
        }
    }

    @Override
    public void getVideoSuccess(List<VideoInfoDao> list) {
        if(pageNum==1){
            videoInfoDaos.clear();
        }
        smartRefresh.finishRefresh();
        contentAdapter.loadMoreComplete();
        videoInfoDaos.addAll(list);
        contentAdapter.notifyDataSetChanged();
    }

    @Override
    public void getVideoNull() {
        smartRefresh.finishRefresh();
        contentAdapter.loadMoreComplete();
        llNull.setVisibility(View.VISIBLE);
    }

    @Override
    public void getVideoend() {
        smartRefresh.finishRefresh();
        contentAdapter.loadMoreEnd();
    }

    /**
     *  刷新数据
     */
    private void refreshVideo(){
        smartRefresh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                pageNum=1;
                mPresenter.queryVideo(columnsId,columnName,pageNum);
            }
        });
    }

    /**
     *  加载更多
     */
    private void loadMore(){
        contentAdapter.setEnableLoadMore(true);
        contentAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                pageNum++;
                mPresenter.queryVideo(columnsId,columnName,pageNum);
            }
        });
    }

}
