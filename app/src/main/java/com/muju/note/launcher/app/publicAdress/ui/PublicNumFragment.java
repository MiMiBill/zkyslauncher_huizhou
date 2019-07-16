package com.muju.note.launcher.app.publicAdress.ui;


import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.muju.note.launcher.R;
import com.muju.note.launcher.app.home.db.AdvertsCodeDao;
import com.muju.note.launcher.app.publicAdress.contract.PublicContract;
import com.muju.note.launcher.app.publicAdress.presenter.PublicPresenter;
import com.muju.note.launcher.app.sign.bean.TaskBean;
import com.muju.note.launcher.base.BaseFragment;
import com.muju.note.launcher.litepal.LitePalDb;
import com.muju.note.launcher.util.qr.QrCodeUtils;
import com.muju.note.launcher.view.password.OnPasswordFinish;
import com.muju.note.launcher.view.password.PopEnterPassword;

import org.json.JSONException;
import org.json.JSONObject;
import org.litepal.LitePal;
import org.litepal.crud.callback.FindMultiCallback;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class PublicNumFragment extends BaseFragment<PublicPresenter> implements PublicContract
        .View {
    @BindView(R.id.btn_code)
    Button btnCode;
    @BindView(R.id.iv_code)
    ImageView ivCode;
    @BindView(R.id.layoutContent)
    RelativeLayout layoutContent;
    @BindView(R.id.tv_title)
    TextView tvTitle;

    private String advertCode = "";
    private static final String PUB_PIC_ID = "pub_pic_id";
    private static final String PUB_PIC_URL = "pub_pic_url";
    private int advertId = 0;
    private String url;
    private PopEnterPassword popEnterPassword;

    public static PublicNumFragment newInstance(int id, String url) {
        Bundle args = new Bundle();
        args.putString(PUB_PIC_URL, url);
        args.putInt(PUB_PIC_ID, id);
        PublicNumFragment fragment = new PublicNumFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getLayout() {
        return R.layout.fragment_public;
    }

    @Override
    public void initData() {
        tvTitle.setText("做任务");
        LitePalDb.setZkysDb();
        LitePal.where("taskType =?", "1").limit(1).findAsync(AdvertsCodeDao.class).listen(new FindMultiCallback<AdvertsCodeDao>() {
            @Override
            public void onFinish(List<AdvertsCodeDao> list) {
                if (list != null && list.size() > 0) {
                    AdvertsCodeDao codeDao = list.get(0);
                    if (codeDao != null) {
                        advertId = codeDao.getAdid();
                        url = codeDao.getTaskUrl();
                        ivCode.setImageBitmap(QrCodeUtils.generateOriginalBitmap(url, 350, 350));
                    }
                }
            }
        });

    }

    @Override
    public void initPresenter() {
        mPresenter = new PublicPresenter();
    }

    @Override
    public void showError(String msg) {

    }


    //输入验证码框
    private void showPassDialog() {
        popEnterPassword = new PopEnterPassword(getActivity());
        // 显示窗口
        popEnterPassword.showAtLocation(getActivity().findViewById(R.id.layoutContent),
                Gravity.CENTER, 0, 0); // 设置layout在PopupWindow中显示的位置
        popEnterPassword.setOnPassFinish(new OnPasswordFinish() {
            @Override
            public void passwordFinish(String password) {
                mPresenter.verfycode(password, advertId, advertCode);
            }
        });
    }


    @Override
    public void onSupportInvisible() {
        super.onSupportInvisible();
        if (popEnterPassword != null && popEnterPassword.isShowing())
            popEnterPassword.dismiss();
    }

    @Override
    public void verfycode(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            if (jsonObject.optInt("code") == 200) {
                showToast("验证码验证成功");
                Bundle bundle = new Bundle();
                setFragmentResult(RESULT_OK, bundle);
                pop();
//                mPresenter.doTask(UserUtil.getUserBean().getId(), advertId);
            } else {
                showToast("验证码验证失败");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void verfycodeError() {
        showToast("网络错误,请重试");
    }

    @Override
    public void doTask(TaskBean taskBean) {
        /*Bundle bundle=new Bundle();
        setFragmentResult(RESULT_OK,bundle);
        EventBus.getDefault().post(new UserInfoEvent(UserUtil.getUserBean()));*/
    }


    @OnClick({R.id.ll_back, R.id.btn_code})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_back:
                pop();
                break;
            case R.id.btn_code:
                showPassDialog();
                break;
        }
    }
}
