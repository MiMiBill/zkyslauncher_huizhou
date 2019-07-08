package com.muju.note.launcher.app.setting.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.muju.note.launcher.R;
import com.muju.note.launcher.app.userinfo.contract.FeedBackContract;
import com.muju.note.launcher.app.userinfo.presenter.FeedBackPresenter;
import com.muju.note.launcher.base.BaseFragment;
import com.muju.note.launcher.base.LauncherApplication;
import com.muju.note.launcher.util.log.LogFactory;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class FeedBackFragment extends BaseFragment<FeedBackPresenter> implements FeedBackContract.View {
    @BindView(R.id.activity_feedback_flag)
    View activityFeedbackFlag;
    @BindView(R.id.activity_feedback_title_ll)
    RelativeLayout activityFeedbackTitleLl;
    @BindView(R.id.activity_feedback_content)
    EditText activityFeedbackContent;
    @BindView(R.id.activity_feedback_sumbit)
    Button activityFeedbackSumbit;
    Unbinder unbinder;

    public static FeedBackFragment getInstance() {
        FeedBackFragment feedBackFragment = new FeedBackFragment();
        return feedBackFragment;
    }
    @Override
    public int getLayout() {
        return R.layout.fragment_feedback;
    }

    @Override
    public void initData() {
    }

    @Override
    public void initPresenter() {
        mPresenter=new FeedBackPresenter();
    }

    @Override
    public void showError(String msg) {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        LogFactory.l().i("onDestroyView");
        super.onDestroyView();
        hideSoftInput();
        unbinder.unbind();
    }

    @OnClick(R.id.activity_feedback_sumbit)
    public void onViewClicked() {
        if (isEmptyContent(activityFeedbackContent)) {
            Toast.makeText(LauncherApplication.getContext(), "请输入内容", Toast.LENGTH_LONG).show();
            return;
        }
        mPresenter.post(activityFeedbackContent.getText().toString().trim());
    }

    //输入内容是否为空，空返回true
    public Boolean isEmptyContent(EditText edContent) {
        if (edContent.getText().length() > 0) {
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if(hidden){
            hideSoftInput();
        }
    }

    @Override
    public void post(String data) {
        if (data.isEmpty()) {
            Toast.makeText(LauncherApplication.getContext(), "响应异常！", Toast.LENGTH_LONG)
                    .show();
            return;
        }
        JsonObject jsonObject = (JsonObject) new JsonParser().parse(data);
        if (jsonObject.get("code").getAsInt() == 200) {
            Toast.makeText(LauncherApplication.getContext(),"提交成功",Toast.LENGTH_SHORT).show();
        }else {
            postFail();
        }
    }

    @Override
    public void postFail() {
        Toast.makeText(LauncherApplication.getContext(),"网络异常,请稍后再试",Toast.LENGTH_SHORT).show();
    }
}
