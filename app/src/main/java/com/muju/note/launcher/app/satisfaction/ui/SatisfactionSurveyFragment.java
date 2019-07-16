package com.muju.note.launcher.app.satisfaction.ui;

import android.app.ProgressDialog;
import android.app.Service;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;
import com.muju.note.launcher.R;
import com.muju.note.launcher.app.satisfaction.adapter.SatisfactionAdapter2;
import com.muju.note.launcher.app.satisfaction.response.SatisfactionDetailResponse;
import com.muju.note.launcher.app.satisfaction.contract.SatisfationContract;
import com.muju.note.launcher.app.satisfaction.presenter.SatisfationPresenter;
import com.muju.note.launcher.app.video.event.VideoNoLockEvent;
import com.muju.note.launcher.base.BaseFragment;
import com.muju.note.launcher.util.Constants;
import com.muju.note.launcher.util.app.MobileInfoUtil;
import com.muju.note.launcher.util.sign.Signature;
import com.muju.note.launcher.util.toast.FancyToast;
import com.muju.note.launcher.view.BackTitleView;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

/**
 * 满意度调查页面，因为没有H5，所以搞得原生
 */
public class SatisfactionSurveyFragment extends BaseFragment<SatisfationPresenter> implements SatisfationContract.View {
    @BindView(R.id.title)
    BackTitleView title;
    @BindView(R.id.btn_commit)
    Button btnCommit;
    @BindView(R.id.satisfaction_data)
    RecyclerView satisfactionData;
    List<SatisfactionDetailResponse.DataBean.ProblemsBean> data = new ArrayList<>();
    SatisfactionAdapter2 adapter;
    View head, foot;
    ProgressDialog progressDialog;
    int failure = 0;
    private Context context;
    private String padsurveyid = "";

    @Override
    public int getLayout() {
        return R.layout.activity_satisfaction;
    }

    public static SatisfactionSurveyFragment newInstance(String padsurveyid) {
        Bundle args = new Bundle();
        args.putString(Constants.PAD_SURVEY_ID, padsurveyid);
        SatisfactionSurveyFragment fragment = new SatisfactionSurveyFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void initData() {
        EventBus.getDefault().post(new VideoNoLockEvent(false));
        context = getActivity();
        title.hideBackBtn();
        padsurveyid = getArguments().getString(Constants.PAD_SURVEY_ID);
        satisfactionData.setLayoutManager(new LinearLayoutManager(context));
        inHeadAndFoot();
        head = LayoutInflater.from(context).inflate(R.layout.head_satisfaction_layout, null);
        getSatisfactionData(padsurveyid);
        satisfactionData.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (recyclerView.getWindowToken() != null) {
                    if (((InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE)).isActive()) {
                        ((InputMethodManager) context.getSystemService(Service.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(recyclerView.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().post(new VideoNoLockEvent(true));
    }

    @Override
    public void initPresenter() {
        mPresenter = new SatisfationPresenter();
    }

    private void inHeadAndFoot() {
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("提交中，请稍后...");
        progressDialog.setCancelable(false);
        head = LayoutInflater.from(context).inflate(R.layout.head_satisfaction_layout, null);
        foot = LayoutInflater.from(context).inflate(R.layout.foot_satisfaction_layout, null);
        btnCommit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                commitSatisfaction();
            }
        });
    }

    private void commitSatisfaction() {
        if (adapter.getSatisfaResult() == null) return;
        String result = adapter.getSatisfaResult().toString();
        if (TextUtils.isEmpty(result)) {
            return;
        }
        Map<String, String> params = new HashMap();
        params.put("answerList", result);
        String sign = Signature.getSign(params, MobileInfoUtil.getICCID(context));

        mPresenter.getCommitSurveyData(sign, adapter.getSatisfaResult());
    }

    private void getSatisfactionData(String id) {
        if (TextUtils.isEmpty(id)) return;
        Map<String, String> params = new HashMap();
        params.put("id", id);
        String sign = Signature.getSign(params, MobileInfoUtil.getICCID(context));

        mPresenter.getSurveyData(id, sign);
    }


    @Override
    public void onSupportInvisible() {
        super.onSupportInvisible();
        hideSoftInput();
    }

    @Override
    public void showError(String msg) {

    }

    @Override
    public void commitErrow() {
        failure++;
        if (failure == 3) {
            FancyToast.makeText(context, "提交失败，请联系管理员", FancyToast.LENGTH_SHORT).show();
            pop();
            return;
        }
        FancyToast.makeText(context, "提交失败，请稍后再试", FancyToast.LENGTH_SHORT).show();
    }

    @Override
    public void getCommitSurveyData(String response) {
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(response);
            if (jsonObject.optInt("code") == 200) {
                FancyToast.makeText(context, "提交成功", FancyToast.LENGTH_SHORT).show();
                pop();
            } else {
                commitErrow();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void getSurveyData(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            if (jsonObject.optInt("code") == 200) {
                Gson gson = new Gson();
                SatisfactionDetailResponse detailResponse = gson.fromJson(response, SatisfactionDetailResponse.class);
                if (detailResponse.getCode() == 200) {
                    data.clear();
                    data.addAll(detailResponse.getData().getProblems());
                    adapter = new SatisfactionAdapter2(context, data);
                    adapter.bindToRecyclerView(satisfactionData);
                    adapter.removeAllHeaderView();
                    ((TextView) head.findViewById(R.id.head_title)).setText(detailResponse.getData().getTitle());
                    ((TextView) head.findViewById(R.id.head_content)).setText("        " + detailResponse.getData().getDescription());
                    adapter.setHeaderView(head);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}

