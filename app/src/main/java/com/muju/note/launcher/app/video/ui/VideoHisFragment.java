package com.muju.note.launcher.app.video.ui;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.muju.note.launcher.R;
import com.muju.note.launcher.app.video.adapter.VideoHisAdapter;
import com.muju.note.launcher.app.video.contract.VideoHisContract;
import com.muju.note.launcher.app.video.db.VideoHisDao;
import com.muju.note.launcher.app.video.presenter.VideoHisPresenter;
import com.muju.note.launcher.base.BaseFragment;
import com.muju.note.launcher.base.LauncherApplication;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import me.yokeyword.fragmentation.ISupportFragment;

/**
 *  历史记录
 */
public class VideoHisFragment extends BaseFragment<VideoHisPresenter> implements VideoHisContract.View, View.OnClickListener {
    @BindView(R.id.ll_back)
    LinearLayout llBack;
    @BindView(R.id.tv_null)
    TextView tvNull;
    @BindView(R.id.ll_null)
    LinearLayout llNull;
    @BindView(R.id.rv_his_view)
    RecyclerView rvHisView;

    private List<VideoHisDao> hisDaos;
    private VideoHisAdapter hisAdapter;

    @Override
    public int getLayout() {
        return R.layout.fragment_video_his;
    }

    @Override
    public void initData() {
        llBack.setOnClickListener(this);
        tvNull.setOnClickListener(this);

        hisDaos=new ArrayList<>();
        hisAdapter=new VideoHisAdapter(R.layout.rv_item_video_content,hisDaos);
        rvHisView.setLayoutManager(new GridLayoutManager(LauncherApplication.getContext(), 5));
        rvHisView.setAdapter(hisAdapter);

        hisAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                WotvPlayFragment wotvPlayFragment=new WotvPlayFragment();
                wotvPlayFragment.setHisDao(hisDaos.get(position));
                start(wotvPlayFragment, ISupportFragment.SINGLETASK);
            }
        });

        mPresenter.queryHis();
    }

    @Override
    public void initPresenter() {
        mPresenter=new VideoHisPresenter();
    }

    @Override
    public void showError(String msg) {

    }

    @Override
    public void getVideoSuccess(List<VideoHisDao> list) {
        hisDaos.addAll(list);
        hisAdapter.notifyDataSetChanged();
    }

    @Override
    public void getVideoNull() {
        llNull.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ll_back:
            case R.id.tv_null:
                pop();
                break;
        }
    }
}
