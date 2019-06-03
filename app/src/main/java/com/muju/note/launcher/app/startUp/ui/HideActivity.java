package com.muju.note.launcher.app.startUp.ui;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.muju.note.launcher.R;
import com.muju.note.launcher.app.startUp.dialog.DialogFactory;
import com.muju.note.launcher.app.startUp.entity.MyAppInfoEntity;
import com.muju.note.launcher.base.BaseActivity;
import com.muju.note.launcher.url.UrlUtil;
import com.muju.note.launcher.util.Constants;
import com.muju.note.launcher.util.UIUtils;
import com.muju.note.launcher.util.app.AppUtils;
import com.muju.note.launcher.util.sp.SPUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

//隐藏页面
public class HideActivity extends BaseActivity {
    @BindView(R.id.lv_app_list)
    GridView lvAppList;
    @BindView(R.id.one)
    Button one;
    @BindView(R.id.two)
    Button two;
    @BindView(R.id.three)
    Button three;
    @BindView(R.id.four)
    Button four;
    @BindView(R.id.btnOpenNetwork)
    CheckBox btnOpenNetwork;
    private AppAdapter mAppAdapter;
    public Handler mHandler = new Handler();

    public static void launch(Context context) {
        context.startActivity(new Intent(context, HideActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_app);
        //   sendBroadcast(new Intent(Constant.ACTION_SHOW_STATUSBAR));
        ButterKnife.bind(this);
        mAppAdapter = new AppAdapter();
        lvAppList.setAdapter(mAppAdapter);
        initAppList();
        lvAppList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AppUtils.openPackage(HideActivity.this, mAppAdapter.getItem(position).getPkgName());
            }
        });

        setSpeedText();
        btnOpenNetwork.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecke) {
                SPUtil.putBoolean(Constants.PAD_CONFIG_OPEN_NETWORK_SPEEK, isChecke);
            }
        });

    }

    @Override
    public int getLayout() {
        return R.layout.activity_list_app;
    }

    @Override
    public void initData() {

    }

    private void setSpeedText() {
        btnOpenNetwork.setChecked(SPUtil.getBoolean(Constants.PAD_CONFIG_OPEN_NETWORK_SPEEK));
    }


    private void initAppList() {
        new Thread() {
            @Override
            public void run() {
                super.run();
                //扫描得到APP列表
                final List<MyAppInfoEntity> appInfos = AppUtils.scanLocalInstallAppList(HideActivity.this.getPackageManager());
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        mAppAdapter.setData(appInfos);
                    }
                });
            }
        }.start();
    }

    @OnClick({R.id.one, R.id.two, R.id.three, R.id.four,R.id.btnConfig})
    public void onViewClicked(View view) {
        if (view.getId() == R.id.btnConfig) {
            final Dialog dialog = DialogFactory.dialog(getContext(), R.layout.layout_config);
            final EditText etHost = dialog.findViewById(R.id.etHost);
            final CheckBox cbxShowLog = dialog.findViewById(R.id.cbxShowLog);
            Button btnConfirm = dialog.findViewById(R.id.btnConfirm);
            etHost.setText(UrlUtil.getHost());

            cbxShowLog.setChecked(SPUtil.getBoolean(Constants.SHOW_LOG));
            btnConfirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SPUtil.putString(Constants.HOST, etHost.getText().toString());
                    SPUtil.putBoolean(Constants.SHOW_LOG, cbxShowLog.isChecked());
                    dialog.dismiss();
                    UIUtils.restartApplication();
                }
            });

            return;
        }

        Intent intent = new Intent();
        switch (view.getId()) {
            case R.id.one:
                intent.setAction(Constants.ACTION_HIDE_DEV);
                break;
            case R.id.two:
                intent.setAction(Constants.ACTION_SHOW_DEV);
                break;
            case R.id.three:
                intent.setAction(Constants.ACTION_HIDE_STATUSBAR);
                break;
            case R.id.four:
                intent.setAction(Constants.ACTION_SHOW_STATUSBAR);
                break;
        }
        sendBroadcast(intent);
    }


    class AppAdapter extends BaseAdapter {

        List<MyAppInfoEntity> MyAppInfoEntitys = new ArrayList<MyAppInfoEntity>();

        public void setData(List<MyAppInfoEntity> MyAppInfoEntitys) {
            this.MyAppInfoEntitys = MyAppInfoEntitys;
            notifyDataSetChanged();
        }

        public List<MyAppInfoEntity> getData() {
            return MyAppInfoEntitys;
        }

        @Override
        public int getCount() {
            if (MyAppInfoEntitys != null && MyAppInfoEntitys.size() > 0) {
                return MyAppInfoEntitys.size();
            }
            return 0;
        }

        @Override
        public MyAppInfoEntity getItem(int position) {
            if (MyAppInfoEntitys != null && MyAppInfoEntitys.size() > 0) {
                return MyAppInfoEntitys.get(position);
            }
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder mViewHolder;
            MyAppInfoEntity MyAppInfoEntity = MyAppInfoEntitys.get(position);
            if (convertView == null) {
                mViewHolder = new ViewHolder();
                convertView = LayoutInflater.from(getBaseContext()).inflate(R.layout.item_app_info, null);
                mViewHolder.iv_app_icon = (ImageView) convertView.findViewById(R.id.iv_app_icon);
                mViewHolder.tx_app_name = (TextView) convertView.findViewById(R.id.tv_app_name);
                mViewHolder.tx_pkg_name = (TextView) convertView.findViewById(R.id.tv_pkg_name);
                convertView.setTag(mViewHolder);
            } else {
                mViewHolder = (ViewHolder) convertView.getTag();
            }
            mViewHolder.iv_app_icon.setImageDrawable(MyAppInfoEntity.getImage());
            mViewHolder.tx_app_name.setText(MyAppInfoEntity.getAppName());
            mViewHolder.tx_pkg_name.setText(MyAppInfoEntity.getPkgName());
            return convertView;
        }

        class ViewHolder {

            ImageView iv_app_icon;
            TextView tx_app_name;
            TextView tx_pkg_name;
        }
    }


    @Override
    public void onBackPressedSupport() {
        sendBroadcast(new Intent(Constants.ACTION_HIDE_STATUSBAR));
        super.onBackPressedSupport();
    }

}
