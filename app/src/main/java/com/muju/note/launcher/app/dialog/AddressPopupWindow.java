package com.muju.note.launcher.app.dialog;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;

import com.muju.note.launcher.R;
import com.muju.note.launcher.util.UIUtils;
import com.muju.note.launcher.util.toast.ToastUtil;
//税种
public class AddressPopupWindow extends PopupWindow implements View.OnClickListener{

    private Context mContext;
    private LayoutInflater mInflater;


    private Activity mActivity;
    private SureClickListener sureClickListener;
    private EditText edt_name;
    private EditText edt_phone;

    public AddressPopupWindow(Context context) {
        this.mContext = context;
        mInflater = LayoutInflater.from(mContext);
        mActivity = (Activity) context;
        initPopup();
    }

    /**
     * 初始化PopupWindow
     */
    private void initPopup() {
        View view = mInflater.inflate(R.layout.pop_food_order, null);
        setContentView(view);
        //设置PopupWindow宽高

        Button btn_commit=view.findViewById(R.id.btn_commit);
        edt_name = view.findViewById(R.id.edt_name);
        edt_phone = view.findViewById(R.id.edt_phone);
        btn_commit.setOnClickListener(this);

        WindowManager windowManager = mActivity.getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        setWidth(mContext.getResources().getDimensionPixelSize(R.dimen.px1000));
        setHeight(display.getHeight());
        //设置背景
        ColorDrawable dw = new ColorDrawable(0x60000000);
        setBackgroundDrawable(dw);
        setOutsideTouchable(true);
        setFocusable(true);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_commit:
                commit();
                break;
        }
    }

    private void commit() {
        String phoneNum = edt_phone.getText().toString().trim();
        String name = edt_name.getText().toString().trim();
        if(name.equals("")){
            ToastUtil.showToast(mContext,"请填写姓名");
            return;
        }
        if (phoneNum.equals("")) {
            ToastUtil.showToast(mContext, "请填写手机号码");
            return;
        }
        if (!(phoneNum.length() == 11 && UIUtils.isMobile(phoneNum))) {
            ToastUtil.showToast(mContext, "请填写正确的手机号码");
            return;
        }

        if(sureClickListener!=null){
            sureClickListener.onSureClick(name,phoneNum);
        }

    }


    public void setOnSureClickListener(SureClickListener sureClickListener) {
        this.sureClickListener = sureClickListener;
    }

    public interface SureClickListener{
        void onSureClick(String name,String phoneNum);
    }


    @Override
    public void showAsDropDown(View anchor) {
        if (Build.VERSION.SDK_INT >= 24) {
            Rect rect = new Rect();
            anchor.getGlobalVisibleRect(rect);
            int h = anchor.getResources().getDisplayMetrics().heightPixels - rect.bottom;
            setHeight(h);
        }
        super.showAsDropDown(anchor);
    }

    @Override
    public void showAsDropDown(View anchor, int xoff, int yoff) {
        if (Build.VERSION.SDK_INT >= 24) {
            Rect rect = new Rect();
            anchor.getGlobalVisibleRect(rect);
            int h = anchor.getResources().getDisplayMetrics().heightPixels - rect.bottom;
            setHeight(h);
        }
        super.showAsDropDown(anchor, xoff, yoff);
    }

    /**
     * 设置PopupWindow的位置
     *
     * @param parent
     */
    public void showPopupWindow(View parent) {
        if (!this.isShowing()) {
            this.showAsDropDown(parent, 0, 2);
        } else {
            this.dismiss();
        }
    }
    
}
