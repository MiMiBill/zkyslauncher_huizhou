package com.muju.note.launcher.util.toast;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.muju.note.launcher.R;


/**
 * Created by 52620 on 2017/10/19.
 */

public class ToastUtil {
    public static ToastUtil mToastUtil;
    private static Toast toast;

    private ToastUtil() {
    }

    public static ToastUtil getToastUtil() {
        if (mToastUtil == null) {
            mToastUtil = new ToastUtil();
        }
        return mToastUtil;
    }

    public static void showToast(Context context, String info){
        if (toast==null) {
            toast = Toast.makeText(context, info, Toast.LENGTH_LONG);
//            LinearLayout layout = (LinearLayout) toast.getView();
//            layout.setBackgroundColor(Color.parseColor("#FFFFFF"));s
            TextView v = (TextView) toast.getView().findViewById(android.R.id.message);
//            v.setTextColor(Color.BLACK);
        }else {
            toast.setText(info);
        }
        toast.show();
    }

    /**
     * 显示
     */
    public static void ToastShow(Context context,String str) {
        View view = LayoutInflater.from(context).inflate(R.layout.toast_view, null);
        TextView text = (TextView) view.findViewById(R.id.tv_toast);
        text.setText(str);
        toast = new Toast(context);
        toast.setGravity(Gravity.CENTER, 0, 0); // Toast显示的位置
        toast.setDuration(Toast.LENGTH_SHORT); // Toast显示的时间
        toast.setView(view);
        toast.show();
    }

    public void ToastCancel() {
        if (toast != null) {
            toast.cancel();
        }
    }
}
