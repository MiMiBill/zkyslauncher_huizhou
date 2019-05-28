package com.muju.note.launcher.app.video.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.google.gson.Gson;
import com.muju.note.launcher.R;
import com.muju.note.launcher.app.video.adapter.VideoContentAdapter;
import com.muju.note.launcher.app.video.bean.VideoMoreTagBean;
import com.muju.note.launcher.app.video.contract.VideoContentContract;
import com.muju.note.launcher.app.video.db.VideoHisDao;
import com.muju.note.launcher.app.video.db.VideoInfoDao;
import com.muju.note.launcher.app.video.db.VideoTagSubDao;
import com.muju.note.launcher.app.video.db.VideoTagsDao;
import com.muju.note.launcher.app.video.event.VideoFinishEvent;
import com.muju.note.launcher.app.video.presenter.VideoContentPresenter;
import com.muju.note.launcher.app.video.util.WoTvUtil;
import com.muju.note.launcher.base.BaseFragment;
import com.muju.note.launcher.base.LauncherApplication;
import com.orhanobut.logger.Logger;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.unicom.common.VideoSdkConfig;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 视频列表
 */
public class VideoContentFragment extends BaseFragment<VideoContentPresenter> implements View.OnClickListener, VideoContentContract.View {

    public static final String TAG = "VideoContentFragment";
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

    private RecyclerView rvFilterView;
    private BaseQuickAdapter<VideoTagsDao,BaseViewHolder> adapterFilter;

    private int columnsId;
    private String columnName;
    private int pageNum = 1;

    private List<VideoInfoDao> videoInfoDaos;
    private VideoContentAdapter contentAdapter;
    private List<VideoTagsDao> filterList = new ArrayList<>();

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

        addHeader();

        refreshVideo();
        loadMore();

        mPresenter.queryVideo(columnsId,columnName,pageNum);
        mPresenter.queryFilter(columnsId);

        contentAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                toPlay(videoInfoDaos.get(position));
            }
        });
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
        rvVideoView.setVisibility(View.VISIBLE);
        llNull.setVisibility(View.GONE);
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
        rvVideoView.setVisibility(View.GONE);
        llNull.setVisibility(View.VISIBLE);
    }

    @Override
    public void getVideoend() {
        smartRefresh.finishRefresh();
        contentAdapter.loadMoreEnd();
    }

    @Override
    public void getFilter(List<VideoTagsDao> list) {
        Logger.json(new Gson().toJson(list));
        filterList.clear();
        filterList.addAll(list);
        adapterFilter.setNewData(filterList);
    }

    /**
     *  添加头部
     */
    private void addHeader(){
        View headerView=LayoutInflater.from(LauncherApplication.getContext()).inflate(R.layout.header_video_filter,null);
        rvFilterView=headerView.findViewById(R.id.rvFilter);
        adapterFilter = new BaseQuickAdapter<VideoTagsDao, BaseViewHolder>(R.layout.rv_item_filter) {
            @Override
            protected void convert(BaseViewHolder helper, final VideoTagsDao item) {
                RecyclerView rvFilter = helper.getView(R.id.rv_sub_filter);
                BaseQuickAdapter<VideoTagSubDao, BaseViewHolder> adapterFilter = new BaseQuickAdapter<VideoTagSubDao, BaseViewHolder>(R.layout.rv_item_filter_sub) {
                    @Override
                    protected void convert(BaseViewHolder helper, VideoTagSubDao item) {
                        TextView tvFilter = helper.getView(R.id.tv_filter);
                        tvFilter.setText(item.getName());
                        if (item.isChoice()) {
                            tvFilter.setSelected(true);
                        } else {
                            tvFilter.setSelected(false);
                        }
                        helper.addOnClickListener(R.id.tv_filter);
                    }
                };

                adapterFilter.setOnItemChildClickListener(new OnItemChildClickListener() {
                    @Override
                    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                        if (view.getId() == R.id.tv_filter) {
                            for (int i = 0; i < item.getList().size(); i++) {
                                VideoTagSubDao subBean = item.getList().get(i);
                                if (position == i) {
                                    subBean.setChoice(true);
                                } else {
                                    subBean.setChoice(false);
                                }
                            }
                            pageNum = 1;
                            sercahVideoByTags();
                            adapter.notifyDataSetChanged();
                        }
                    }
                });
                adapterFilter.setNewData(item.getList());
                rvFilter.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
                rvFilter.setAdapter(adapterFilter);
            }
        };
        contentAdapter.addHeaderView(headerView);
        rvFilterView.setLayoutManager(new LinearLayoutManager(getContext()));
        rvFilterView.setAdapter(adapterFilter);
    }

    /**
     *  刷新数据
     */
    private void refreshVideo(){
        smartRefresh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                pageNum=1;
                mPresenter.queryFilter(columnsId);
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
                sercahVideoByTags();
            }
        });
    }

    /**
     *  根据标签查询影视列表
     */
    private void sercahVideoByTags(){
        try {
            List<VideoMoreTagBean> tagBeanList = new ArrayList<>();
            for (VideoTagsDao dao : filterList) {
                for (int i = 0; i < dao.getList().size(); i++) {
                    VideoTagSubDao subBean = dao.getList().get(i);
                    if (subBean.isChoice() && i != 0) {
                        tagBeanList.add(new VideoMoreTagBean(subBean.getId(), subBean.getName()));
                    }
                }
            }
            String tags="";
            for (int i=0;i<tagBeanList.size();i++){
                if(TextUtils.isEmpty(tags)) {
                    tags = tagBeanList.get(i).getName();
                }else {
                    tags = tags + "," + tagBeanList.get(i).getName();
                }
            }
            contentAdapter.setEnableLoadMore(true);
            mPresenter.queryVideo(columnsId,columnName,pageNum,tags);
        } catch (Exception e) {
            e.printStackTrace();
        }
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
        WotvPlayFragment wotvPlayFragment=new WotvPlayFragment();
        wotvPlayFragment.setHisDao(hisDao);
        ((VideoFragment) getParentFragment()).start(wotvPlayFragment);
    }

}
