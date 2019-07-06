package com.muju.note.launcher.app.setting.ui;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.muju.note.launcher.R;
import com.muju.note.launcher.base.BaseFragment;
import com.muju.note.launcher.util.app.MobileInfoUtil;
import com.muju.note.launcher.util.qr.QrCodeUtils;

import butterknife.BindView;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class ToolFragment extends BaseFragment {
    @BindView(R.id.iv_ma)
    ImageView ivMa;

    @Override
    public int getLayout() {
        return R.layout.fragment_tool;
    }

    @Override
    public void initData() {

    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void showError(String msg) {

    }

    public static ToolFragment getInstance() {
        ToolFragment toolFragment = new ToolFragment();
        return toolFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        showQurCode();
    }

    private void showQurCode() {
        Observable.create(new ObservableOnSubscribe<Bitmap>() {
            @Override
            public void subscribe(ObservableEmitter<Bitmap> observableEmitter) throws Exception {

                Bitmap bitmap = QrCodeUtils.generateBitmap(MobileInfoUtil.getICCID(getContext())
                        + "," + MobileInfoUtil.getIMEI(getContext()), 418, 418);
                observableEmitter.onNext(bitmap);


            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Bitmap>() {
                    @Override
                    public void accept(Bitmap bitmap) throws Exception {
                        if(ivMa!=null)
                        ivMa.setImageBitmap(bitmap);
                    }
                });
    }

}
