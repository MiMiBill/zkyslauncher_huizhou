package com.muju.note.launcher.app.satisfaction;

import android.app.ProgressDialog;
import android.app.Service;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.base.Request;
import com.muju.note.launcher.R;
import com.muju.note.launcher.app.satisfaction.adapter.SatisfactionAdapter2;
import com.muju.note.launcher.app.satisfaction.response.SatisfactionDetailResponse;
import com.muju.note.launcher.base.BaseActivity;
import com.muju.note.launcher.url.UrlUtil;
import com.muju.note.launcher.util.Constants;
import com.muju.note.launcher.util.app.MobileInfoUtil;
import com.muju.note.launcher.util.log.LogUtil;
import com.muju.note.launcher.util.sign.Signature;
import com.muju.note.launcher.util.toast.FancyToast;
import com.muju.note.launcher.view.BackTitleView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 满意度调查页面，因为没有H5，所以搞得原生
 */
public class SatisfactionSurveyActivity extends BaseActivity {
    @BindView(R.id.title)
    BackTitleView title;
    @BindView(R.id.satisfaction_data)
    RecyclerView satisfactionData;
    List<SatisfactionDetailResponse.DataBean.ProblemsBean> data;
    SatisfactionAdapter2 adapter;
    View head, foot;
    ProgressDialog progressDialog;
    int failure = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_satisfaction);
        ButterKnife.bind(this);
        title.hideBackBtn();
        data = new ArrayList<>();
        satisfactionData.setLayoutManager(new LinearLayoutManager(this));
        inHeadAndFoot();
        head = LayoutInflater.from(this).inflate(R.layout.head_satisfaction_layout, null);
        getSatisfactionData(getIntent().getStringExtra(Constants.PAD_SURVEY_ID));
        satisfactionData.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (recyclerView.getWindowToken() != null) {
                    if (((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).isActive()) {
                        ((InputMethodManager) getSystemService(Service.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(recyclerView.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
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
    public int getLayout() {
        return R.layout.activity_satisfaction;
    }

    @Override
    public void initData() {

    }

    @Override
    public void initPresenter() {

    }

    private void inHeadAndFoot() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("提交中，请稍后...");
        progressDialog.setCancelable(false);
        head = LayoutInflater.from(this).inflate(R.layout.head_satisfaction_layout, null);
        foot = LayoutInflater.from(this).inflate(R.layout.foot_satisfaction_layout, null);
        foot.findViewById(R.id.commit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*String result = adapter.getSatisfaResult();
                LogUtil.e("SatisfactionSurveyActivity ", "getSatisfaResult()=" + result);*/
                commitSatisfaction();
            }
        });
    }

    private void commitSatisfaction() {
        if (adapter.getSatisfaResult() == null) return;
        String result = adapter.getSatisfaResult().toString();
        LogUtil.e("SatisfactionSurveyActivity ", "getSatisfaResult()=" + result);
        if (TextUtils.isEmpty(result)) {
            return;
        }
        Map<String, String> params = new HashMap();
        params.put("answerList", result);
        String sign = Signature.getSign(params, MobileInfoUtil.getICCID(this));
        OkGo.<String>post(UrlUtil.getGetCommitSurveyData()).headers("Content-Type", "application/json;charset=utf-8").headers(Constants.PAD_SIGN, sign).upJson(adapter.getSatisfaResult())./*params("answerList", result).*/tag(this).execute(new StringCallback() {
            @Override
            public void onStart(Request<String, ? extends Request> request) {
                super.onStart(request);
                progressDialog.show();
            }

            @Override
            public void onSuccess(Response<String> response) {
                try {
                    JSONObject jsonObject = new JSONObject(response.body());
                    if (jsonObject.optInt("code") == 200) {
                        FancyToast.makeText(SatisfactionSurveyActivity.this, "提交成功", FancyToast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        onError(response);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    onError(response);
                }


            }

            @Override
            public void onError(Response<String> response) {
                super.onError(response);
                failure++;
                if (failure == 3) {
                    FancyToast.makeText(SatisfactionSurveyActivity.this, "提交失败，请联系管理员", FancyToast.LENGTH_SHORT).show();
                    finish();
                    return;
                }
                FancyToast.makeText(SatisfactionSurveyActivity.this, "提交失败，请稍后再试", FancyToast.LENGTH_SHORT).show();
            }

            @Override
            public void onFinish() {
                super.onFinish();
                progressDialog.dismiss();
            }
        });
    }

    private void getSatisfactionData(String id) {
        if (TextUtils.isEmpty(id)) return;
        Map<String, String> params = new HashMap();
        params.put("id", id);
        String sign = Signature.getSign(params, MobileInfoUtil.getICCID(this));
        OkGo.<String>get(String.format(UrlUtil.getGetSurveyData(), id)).headers(Constants.PAD_SIGN, sign)/*.cacheMode(CacheMode.REQUEST_FAILED_READ_CACHE)*/.tag(this).execute(new StringCallback() {
            @Override
            public void onSuccess(Response<String> response) {
                Gson gson = new Gson();
                SatisfactionDetailResponse detailResponse = gson.fromJson(response.body(), SatisfactionDetailResponse.class);
                if (detailResponse.getCode() == 200) {
                    data.clear();
                    data.addAll(detailResponse.getData().getProblems());
                    /*data.addAll(detailResponse.getData().getProblems());
                    data.addAll(detailResponse.getData().getProblems());
                    data.addAll(detailResponse.getData().getProblems());
                    data.addAll(detailResponse.getData().getProblems());*/
                    adapter = new SatisfactionAdapter2(SatisfactionSurveyActivity.this, data);
                    adapter.bindToRecyclerView(satisfactionData);
                    adapter.removeAllHeaderView();
                    ((TextView) head.findViewById(R.id.head_title)).setText(detailResponse.getData().getTitle());
                    ((TextView) head.findViewById(R.id.head_content)).setText("        " + detailResponse.getData().getDescription());
                    adapter.setHeaderView(head);
                    adapter.setFooterView(foot);
                    //adapter.setNewData(data);
                }
            }

       /*     @Override
            public void onCacheSuccess(Response<String> response) {
                super.onCacheSuccess(response);
                Gson gson = new Gson();
                SatisfactionDetailResponse detailResponse = gson.fromJson(response.body(), SatisfactionDetailResponse.class);
                if (detailResponse.getCode() == 200) {
                    data.clear();
                    data.addAll(detailResponse.getData().getProblems());
                    data.addAll(detailResponse.getData().getProblems());
                    data.addAll(detailResponse.getData().getProblems());
                    data.addAll(detailResponse.getData().getProblems());
                    data.addAll(detailResponse.getData().getProblems());
                    adapter = new SatisfactionAdapter2(SatisfactionSurveyActivity.this, data);
                    adapter.bindToRecyclerView(satisfactionData);
                    adapter.removeAllHeaderView();
                    ((TextView) head.findViewById(R.id.head_title)).setText(detailResponse.getData().getTitle());
                    ((TextView) head.findViewById(R.id.head_content)).setText("        " + detailResponse.getData().getDescription());
                    adapter.setHeaderView(head);
                }
            }*/
        });

    }


}

