package com.muju.note.launcher.app.userinfo;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.LinearLayout;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.muju.note.launcher.R;
import com.muju.note.launcher.url.UrlUtil;
import com.muju.note.launcher.util.log.LogFactory;
import com.muju.note.launcher.util.user.UserUtil;
import com.muju.note.launcher.view.LuckDrawView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LuckDrawActivity extends Activity implements View.OnClickListener {
    @BindView(R.id.luckdrawView)
    LuckDrawView luckdrawView;
    @BindView(R.id.ll_back)
    LinearLayout llBack;
    private int[] luckIndex={1,4,6,9,11};
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:
                    int index = (int) (Math.random() * luckIndex.length);
                    luckdrawView.stop(luckIndex[index]);
                    break;
            }
        }
    };
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_luckdraw);
        ButterKnife.bind(this);

        llBack.setOnClickListener(this);


        luckdrawView.setOnLuckStartListener(new LuckDrawView.OnStartListener() {
            @Override
            public void start() {
                    startLuck();
                    handler.sendEmptyMessageDelayed(1,3000);
            }
        });
        luckdrawView.setOnLuckDrawListener(new LuckDrawView.OnLuckDrawListener() {
            @Override
            public void stop() {
//                showToast("很遗憾本次未中奖");
            }

            @Override
            public void start() {

            }
        });
    }

    private void startLuck() {
//        Map<String, String> params = new HashMap();
//        params.put("userId", String.valueOf(UserUtil.getUserBean().getId()));
        OkGo.<String>get(String.format(UrlUtil.startLuck(), UserUtil.getUserBean().getId()))
                .tag(this)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        LogFactory.l().i(response);
                        if(UserUtil.getUserBean().getIntegral()>=10){
                            UserUtil.getUserBean().setIntegral(UserUtil.getUserBean().getIntegral()-10);
                        }else {
                            UserUtil.getUserBean().setIntegral(0);
                        }
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);

                    }
                });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.ll_back:
                finish();
                break;
        }
    }
}
