package com.muju.note.launcher.app.hostipal.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.muju.note.launcher.R;
import com.muju.note.launcher.app.hostipal.db.InfomationDao;

import java.util.ArrayList;
import java.util.List;

public class PathologyAdapter extends BaseAdapter {
    List<InfomationDao> infomationBeans = new ArrayList<>();
    private Context context;

    public PathologyAdapter(List<InfomationDao> infomationBeans, Context context) {
        this.infomationBeans = infomationBeans;
        this.context = context;
    }

    public void setNewData(List<InfomationDao> infomationBeans) {
        this.infomationBeans = infomationBeans;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return infomationBeans.size();
    }

    @Override
    public Object getItem(int position) {
        return infomationBeans.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        InfomationDao infomationBean = infomationBeans.get(position);
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.mylist_item, null);
            holder = new ViewHolder();
            holder.textView = (TextView) convertView.findViewById(R.id.textview);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.textView.setText(infomationBean.getTitle());
        return convertView;
    }

    class ViewHolder {
        public TextView textView;
    }
}
