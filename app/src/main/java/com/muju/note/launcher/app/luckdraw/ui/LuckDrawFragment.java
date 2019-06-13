package com.muju.note.launcher.app.luckdraw.ui;

import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.muju.note.launcher.R;
import com.muju.note.launcher.app.luckdraw.contract.LuckContract;
import com.muju.note.launcher.app.luckdraw.presenter.LuckPresenter;
import com.muju.note.launcher.app.sign.ui.PriseFragment;
import com.muju.note.launcher.app.video.dialog.LoginDialog;
import com.muju.note.launcher.base.BaseFragment;
import com.muju.note.launcher.util.toast.ToastUtil;
import com.muju.note.launcher.util.user.UserUtil;
import com.muju.note.launcher.view.LuckDrawView;

import butterknife.BindView;
import butterknife.OnClick;

public class LuckDrawFragment extends BaseFragment<LuckPresenter> implements LuckContract.View {
    @BindView(R.id.ll_back)
    LinearLayout llBack;
    @BindView(R.id.luckdrawView)
    LuckDrawView luckdrawView;
    @BindView(R.id.btn_list)
    Button btnList;

    private int[] luckIndex = {1, 4, 6, 9, 11};
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    int index = (int) (Math.random() * luckIndex.length);
                    luckdrawView.stop(luckIndex[index]);
                    break;
            }
        }
    };
    private LoginDialog loginDialog;

    @Override
    public int getLayout() {
        return R.layout.activity_luckdraw;
    }

    @Override
    public void initData() {

        luckdrawView.setOnLuckStartListener(new LuckDrawView.OnStartListener() {
            @Override
            public void start() {
                mPresenter.startLuck(UserUtil.getUserBean().getId());
                handler.sendEmptyMessageDelayed(1, 3000);
            }
        });
        luckdrawView.setOnLuckDrawListener(new LuckDrawView.OnLuckDrawListener() {
            @Override
            public void stop() {
                ToastUtil.showToast(getActivity(), "很遗憾本次未中奖");
            }

            @Override
            public void start() {

            }
        });
    }


    @Override
    public void initPresenter() {
        mPresenter = new LuckPresenter();
    }

    @Override
    public void showError(String msg) {

    }


    @OnClick({R.id.ll_back,R.id.btn_list})
    public void onViewClicked(View view) {
        switch (view.getId()){
            case R.id.ll_back:
                pop();
                break;
            case R.id.btn_list:
                if(UserUtil.getUserBean()==null){
                    showLoginDialog();
                }else {
                    start(new PriseFragment());
                }
                break;
        }
    }

    private void showLoginDialog() {
        loginDialog = new LoginDialog(getActivity(), R.style.DialogFullscreen, new LoginDialog
                .OnLoginListener() {
            @Override
            public void onSuccess() {
                loginDialog.dismiss();
            }

            @Override
            public void onFail() {

            }
        });
        loginDialog.show();
    }

    @Override
    public void startSuccess(String data) {
        if (UserUtil.getUserBean().getIntegral() >= 10) {
            UserUtil.getUserBean().setIntegral(UserUtil.getUserBean().getIntegral() - 10);
        } else {
            UserUtil.getUserBean().setIntegral(0);
        }
    }

    @Override
    public void startFail() {

    }
}
