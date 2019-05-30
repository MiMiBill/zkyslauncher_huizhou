package com.muju.note.launcher.app.userinfo;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.model.Response;
import com.muju.note.launcher.R;
import com.muju.note.launcher.app.userinfo.bean.SignBean;
import com.muju.note.launcher.app.userinfo.bean.SignStatusBean;
import com.muju.note.launcher.okgo.BaseBean;
import com.muju.note.launcher.okgo.JsonCallback;
import com.muju.note.launcher.url.UrlUtil;
import com.muju.note.launcher.util.user.UserUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

//签到页面
public class SigninActivity extends Activity {
    @BindView(R.id.tv_sign)
    TextView tvSign;
    @BindView(R.id.ll_back)
    LinearLayout llBack;
    @BindView(R.id.tv_integral)
    TextView tvIntegral;
    private boolean isSign=false;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);
        ButterKnife.bind(this);

        checkSignStatus();
        tvIntegral.setText("您总共有"+UserUtil.getUserBean().getIntegral()+"积分");
    }


    //检查签到状态
    private void checkSignStatus() {
        OkGo.<BaseBean<SignStatusBean>>get(String.format(UrlUtil.checkSignStatus(), UserUtil.getUserBean().getId()))
                .tag(this)
                .execute(new JsonCallback<BaseBean<SignStatusBean>>() {
                    @Override
                    public void onSuccess(Response<BaseBean<SignStatusBean>> response) {
                        if(response.body().getData() instanceof SignStatusBean){
                            SignStatusBean bean = response.body().getData();
                            if(bean.getIsSign()==1){
                                isSign=true;
                                tvSign.setText("已签到");
                            }else {
                                tvSign.setText("签到");
                            }
                        }else {
                            tvSign.setText("签到");
                        }
                    }
                });
    }

    @OnClick({R.id.tv_sign, R.id.ll_back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_sign:
                if(!isSign)
                checkSign();
                break;
            case R.id.ll_back:
                finish();
                break;
        }
    }


    //签到
    private void checkSign() {
        OkGo.<BaseBean<SignBean>>post(UrlUtil.checkSign())
                .tag(this)
                .params("userId", UserUtil.getUserBean().getId())
                .cacheMode(CacheMode.NO_CACHE).execute(new JsonCallback<BaseBean<SignBean>>() {
            @Override
            public void onSuccess(Response<BaseBean<SignBean>> response) {
                if(response.body().getData() instanceof SignBean){
                    SignBean bean = response.body().getData();
                    tvIntegral.setText("您总共有"+bean.getIntegral()+"积分");
                    UserUtil.getUserBean().setIntegral(bean.getIntegral());
                }else {
                    Toast.makeText(SigninActivity.this,response.body().getMsg(),Toast.LENGTH_SHORT).show();
//                    Toast.makeText(this,response.body().getMsg(),Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
