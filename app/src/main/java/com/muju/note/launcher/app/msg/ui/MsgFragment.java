package com.muju.note.launcher.app.msg.ui;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.muju.note.launcher.R;
import com.muju.note.launcher.app.msg.adapter.MsgAdapter;
import com.muju.note.launcher.app.msg.contract.MsgContract;
import com.muju.note.launcher.app.msg.db.CustomMessageDao;
import com.muju.note.launcher.app.msg.presenter.MsgPresenter;
import com.muju.note.launcher.app.publicui.WebViewFragment;
import com.muju.note.launcher.base.BaseFragment;
import com.muju.note.launcher.base.LauncherApplication;
import com.muju.note.launcher.util.log.LogUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class MsgFragment extends BaseFragment<MsgPresenter> implements View.OnClickListener, MsgContract.View {

    private static final String TAG=MsgFragment.class.getSimpleName();

    @BindView(R.id.ll_back)
    LinearLayout llBack;
    @BindView(R.id.rv_msg)
    RecyclerView rvMsg;
    @BindView(R.id.ll_content)
    LinearLayout llContent;
    @BindView(R.id.tv_null)
    TextView tvNull;
    @BindView(R.id.ll_null)
    LinearLayout llNull;

    private List<CustomMessageDao> customMessageDaos;
    private MsgAdapter msgAdapter;

    @Override
    public int getLayout() {
        return R.layout.fragment_msg;
    }

    @Override
    public void initData() {
        llBack.setOnClickListener(this);
        tvNull.setOnClickListener(this);

        customMessageDaos=new ArrayList<>();
        msgAdapter=new MsgAdapter(R.layout.rv_item_msg,customMessageDaos);
        rvMsg.setLayoutManager(new LinearLayoutManager(LauncherApplication.getContext(),LinearLayoutManager.VERTICAL,false));
        rvMsg.setAdapter(msgAdapter);

        mPresenter.querymsg();

        msgAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                LogUtil.d(TAG,"title:"+customMessageDaos.get(position).getTitle());
                LogUtil.d(TAG,"url:"+customMessageDaos.get(position).getUrl());
                start(WebViewFragment.newInstance(customMessageDaos.get(position).getTitle(),customMessageDaos.get(position).getUrl()));
            }
        });

    }

    @Override
    public void initPresenter() {
        mPresenter=new MsgPresenter();
    }

    @Override
    public void showError(String msg) {

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

    @Override
    public void getMsg(List<CustomMessageDao> list) {
        customMessageDaos.addAll(list);
        msgAdapter.notifyDataSetChanged();
    }

    @Override
    public void getMsgNull() {
        llContent.setVisibility(View.GONE);
        llNull.setVisibility(View.VISIBLE);
    }
}
