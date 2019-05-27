package com.muju.note.launcher.app.hostipal.popupwindow;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.PopupWindow;

import com.muju.note.launcher.R;
import com.muju.note.launcher.app.hostipal.adapter.SearchAdapter;
import com.muju.note.launcher.app.hostipal.db.InfomationDao;

import java.util.ArrayList;

//文章分类
public class SearchPopupWindow extends PopupWindow implements SearchAdapter.OnItemClickListener{
    private ArrayList<InfomationDao> datas=new ArrayList<>();
    private Context mContext;
    private LayoutInflater mInflater;
    private RecyclerView recyclerView;
    private SearchAdapter articleAdapter;
    private Activity mActivity;
    private OnClickListener listener;
    private int selectPosition=-1;
    public SearchPopupWindow(Context context, ArrayList<InfomationDao> list) {
        this.mContext = context;
        this.datas = list;
        mInflater = LayoutInflater.from(mContext);
        mActivity = (Activity) context;
        initPopup();
    }

    /**
     * 初始化PopupWindow
     */
    private void initPopup() {
        View view = mInflater.inflate(R.layout.spiner_window_layout, null);
        setContentView(view);
        //设置PopupWindow宽高
        WindowManager windowManager = mActivity.getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        setWidth(display.getWidth()/4);
//        setHeight(display.getHeight());
        setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        //设置背景
        ColorDrawable dw = new ColorDrawable(0x60000000);
        setBackgroundDrawable(dw);
        setOutsideTouchable(true);

        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        //设置布局管理器
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        //设置adapter
        articleAdapter = new SearchAdapter(mContext,datas);
        articleAdapter.setOnItemClickListener(this);
        recyclerView.setAdapter(articleAdapter);
    }

   


    public void setOnClickListener(OnClickListener listener) {
        this.listener = listener;
    }

    @Override
    public void onItemClick(int position) {
        selectPosition=position;
        if(listener!=null)
            listener.onClick(selectPosition);
    }

    public interface OnClickListener{
        void onClick(int position);
    }

    /**
     * 刷新数据
     *
     * @param list
     */
    public void nodfiyData(ArrayList<InfomationDao> list) {
        if (articleAdapter != null) {
            articleAdapter.nodfiyData(list);
        }
    }


    @Override
    public void showAsDropDown(View anchor) {
//        if (Build.VERSION.SDK_INT >= 24) {
//            Rect rect = new Rect();
//            anchor.getGlobalVisibleRect(rect);
//            int h = anchor.getResources().getDisplayMetrics().heightPixels - rect.bottom;
//            setHeight(h);
//        }
//        super.showAsDropDown(anchor);
        if (Build.VERSION.SDK_INT < 24) {
            showAsDropDown(anchor);
        } else {
            int[] location = new int[2];
            anchor.getLocationOnScreen(location);
            int x = location[0];
            int y = location[1];
            showAtLocation(anchor, Gravity.NO_GRAVITY, x, y + anchor.getHeight());
        }

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
