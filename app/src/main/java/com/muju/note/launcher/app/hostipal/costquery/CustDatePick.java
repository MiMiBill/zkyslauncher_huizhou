package com.muju.note.launcher.app.hostipal.costquery;

import android.app.DatePickerDialog;
import android.content.Context;
import android.widget.DatePicker;
import android.widget.TextView;

import java.util.Calendar;

/**
 * Created by Administrator on 2018/6/14.
 */

public class CustDatePick {

    private Context mContext;
    private int mYear;
    private int mMonth;
    private int mDay;
    private String days;
    private TextView mTvDate;

    public CustDatePick(Context context) {
        this.mContext = context;
    }

    public void show(TextView tvDate) {
        this.mTvDate = tvDate;
        init();
        new DatePickerDialog(mContext, onDateSetListener, mYear, mMonth, mDay).show();
    }

    private void init() {
        Calendar ca = Calendar.getInstance();
        mYear = ca.get(Calendar.YEAR);
        mMonth = ca.get(Calendar.MONTH);
        mDay = ca.get(Calendar.DAY_OF_MONTH);
    }

    /**
     * 日期选择器对话框监听
     */
    private DatePickerDialog.OnDateSetListener onDateSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            mYear = year;
            mMonth = monthOfYear;
            mDay = dayOfMonth;

            if (mMonth + 1 < 10) {
                if (mDay < 10) {
                    days = new StringBuffer().append(mYear).append(".").append("0").
                            append(mMonth + 1).append(".").append("0").append(mDay).append("").toString();
                } else {
                    days = new StringBuffer().append(mYear).append(".").append("0").
                            append(mMonth + 1).append(".").append(mDay).append("").toString();
                }
            } else {
                if (mDay < 10) {
                    days = new StringBuffer().append(mYear).append(".").
                            append(mMonth + 1).append(".").append("0").append(mDay).append("").toString();
                } else {
                    days = new StringBuffer().append(mYear).append(".").
                            append(mMonth + 1).append(".").append(mDay).append("").toString();
                }
            }
            //days
            mTvDate.setText(days);
        }
    };
}
