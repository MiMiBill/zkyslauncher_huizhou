package com.muju.note.launcher.app.hostipal.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.muju.note.launcher.R;
import com.muju.note.launcher.app.hostipal.db.InfoDao;

import java.util.ArrayList;
import java.util.List;

public class DepartmentAdapter extends BaseAdapter {
    List<InfoDao> infoBeans = new ArrayList<>();
    private Context context;
    public DepartmentAdapter(List<InfoDao> infoBeans,Context context) {
        this.infoBeans = infoBeans;
        this.context = context;
    }

    public void setNewData(List<InfoDao> infoBeans) {
        this.infoBeans = infoBeans;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return infoBeans.size();
    }

    @Override
    public Object getItem(int position) {
        return infoBeans.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        InfoDao infoBean = infoBeans.get(position);
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.sublist_item, null);
            viewHolder = new ViewHolder();
            viewHolder.textView = (TextView) convertView.findViewById(R.id.textview1);
            viewHolder.imageView = (ImageView) convertView.findViewById(R.id.iv_img);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.textView.setText(infoBean.getName());

        if (infoBean.isCheck()) {
            viewHolder.imageView.setImageResource(R.mipmap.hos_select);
            convertView.setBackgroundColor(context.getResources().getColor(R.color.color_38B4E9));
        } else {
            viewHolder.imageView.setImageResource(R.mipmap.hos_default);
            convertView.setBackgroundColor(context.getResources().getColor(R.color.color_585858));
        }
        return convertView;
    }

     class ViewHolder {
        public TextView textView;
        public ImageView imageView;
        public LinearLayout layout;
    }
}
