package com.muju.note.launcher.app.hostipal.ui;

import android.app.AlertDialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.muju.note.launcher.R;
import com.muju.note.launcher.app.carerWorker.CarerWorkerFragment;
import com.muju.note.launcher.app.hostipal.adapter.HospitalMienAdapter;
import com.muju.note.launcher.app.hostipal.contract.HospitalMienContract;
import com.muju.note.launcher.app.hostipal.db.MienInfoDao;
import com.muju.note.launcher.app.hostipal.presenter.HospitalMienPresenter;
import com.muju.note.launcher.base.BaseFragment;
import com.muju.note.launcher.base.LauncherApplication;
import com.muju.note.launcher.util.ActiveUtils;
import com.muju.note.launcher.util.log.LogUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;


/**
 * 医院风采
 */
public class HospitalMienFragment extends BaseFragment<HospitalMienPresenter> implements
        HospitalMienContract.View, View.OnClickListener {

    private static final String TAG = "HospitalMienFragment";

    public static HospitalMienFragment hospitalMienFragment = null;
    @BindView(R.id.ll_type_title)
    LinearLayout llTypeTitle;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.wv_Mien)
    WebView wvMien;
    @BindView(R.id.tv_type_title)
    TextView tvTypeTitle;
    @BindView(R.id.ll_back)
    LinearLayout llBack;
    @BindView(R.id.ll_null)
    LinearLayout llNull;
    @BindView(R.id.tv_null)
    TextView tvNull;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.rel_titlebar)
    RelativeLayout relTitlebar;

    private int prePosition = 0;


    private HospitalMienAdapter hospitalMienAdapter;
    private List<MienInfoDao> list;


    public static HospitalMienFragment newInstance() {
        Bundle args = new Bundle();
        HospitalMienFragment fragment = new HospitalMienFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getLayout() {
        return R.layout.fragment_hostpital_mien;
    }

    @Override
    public void onSupportInvisible() {
        super.onSupportInvisible();
//解决退出页面web中还有声音的问题
        wvMien.onPause();
        wvMien.pauseTimers();
//        wvMien.loadUrl("about:blank");
    }

    @Override
    public void onSupportVisible() {
        super.onSupportVisible();
        wvMien.resumeTimers();
        wvMien.onResume();
    }

    @Override
    public void initData() {

        initWebview();

        if (ActiveUtils.getPadActiveInfo() != null) {
            tvTitle.setText(ActiveUtils.getPadActiveInfo().getHospitalName());
        }
        list = new ArrayList<>();
        hospitalMienAdapter = new HospitalMienAdapter(R.layout.rv_item_hospital_mien_type, list);
        recyclerView.setLayoutManager(new LinearLayoutManager(LauncherApplication.getContext(),
                LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(hospitalMienAdapter);

        hospitalMienAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

                prePosition = position;

                llNull.setVisibility(View.GONE);
                llTypeTitle.setSelected(false);
                hospitalMienAdapter.setPos(position);
                hospitalMienAdapter.notifyDataSetChanged();
                setWvMien(list.get(position).getIntroduction());
            }
        });

        llTypeTitle.setOnClickListener(this);
        llBack.setOnClickListener(this);
        tvNull.setOnClickListener(this);

        // 查询医院风采数据
        mPresenter.queryMien();

    }




    private void initWebview()
    {
        wvMien.addJavascriptInterface(this,"android");//添加js监听 这样html就能调用客户端
        wvMien.setWebChromeClient(webChromeClient);
        wvMien.setWebViewClient(webViewClient);
        wvMien.resumeTimers();
        wvMien.onResume();
        WebSettings webSettings = wvMien.getSettings();
        webSettings.setJavaScriptEnabled(true);//允许使用js
        webSettings.setDomStorageEnabled(true);

        /**
         * LOAD_CACHE_ONLY: 不使用网络，只读取本地缓存数据
         * LOAD_DEFAULT: （默认）根据cache-control决定是否从网络上取数据。
         * LOAD_NO_CACHE: 不使用缓存，只从网络获取数据.
         * LOAD_CACHE_ELSE_NETWORK，只要本地有，无论是否过期，或者no-cache，都使用缓存中的数据。
         */
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);//不使用缓存，只从网络获取数据.
        //设置自适应屏幕，两者合用
        webSettings.setUseWideViewPort(true); //将图片调整到适合webview的大小
        webSettings.setLoadWithOverviewMode(true); // 缩放至屏幕的大小


        webSettings.setAllowFileAccess(true); //设置可以访问文件
        webSettings.setDomStorageEnabled(true);
        webSettings.setAppCacheMaxSize(1024 * 1024 * 8);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true); //支持通过JS打开新窗口
        webSettings.setLoadsImagesAutomatically(true); //支持自动加载图片
        webSettings.setDefaultTextEncodingName("utf-8");//设置编码格式

        //添加与js交互事件 ，js 调用 android（日志2）
        //wvCarerWorker.addJavascriptInterface(new CarerWorkerFragment.InJavaScriptInterface(), "java_obj");
        //不支持屏幕缩放
        webSettings.setSupportZoom(false);
        webSettings.setBuiltInZoomControls(false);

    }
//    private void initWebview()
//    {
//
//        wvMien.addJavascriptInterface(this,"android");//添加js监听 这样html就能调用客户端
//        wvMien.setWebChromeClient(webChromeClient);
//        wvMien.setWebViewClient(webViewClient);
//
//        WebSettings webSettings = wvMien.getSettings();
//        webSettings.setJavaScriptEnabled(true);//允许使用js
//        webSettings.setDomStorageEnabled(true);
//
//        /**
//         * LOAD_CACHE_ONLY: 不使用网络，只读取本地缓存数据
//         * LOAD_DEFAULT: （默认）根据cache-control决定是否从网络上取数据。
//         * LOAD_NO_CACHE: 不使用缓存，只从网络获取数据.
//         * LOAD_CACHE_ELSE_NETWORK，只要本地有，无论是否过期，或者no-cache，都使用缓存中的数据。
//         */
//        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);//不使用缓存，只从网络获取数据.
//
//        //不支持屏幕缩放
//        webSettings.setSupportZoom(false);
//        webSettings.setBuiltInZoomControls(false);
//
//    }

    //WebViewClient主要帮助WebView处理各种通知、请求事件
    private WebViewClient webViewClient = new WebViewClient(){
        @Override
        public void onPageFinished(WebView view, String url) {//页面加载完成
            //progressBar.setVisibility(View.GONE);
            // 获取页面内容


        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {//页面开始加载
            //progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            Log.i("ansen","拦截url:"+url);
//            if(url.equals("http://www.google.com/")){
//                Toast.makeText(MainActivity.this,"国内不能访问google,拦截该url",Toast.LENGTH_LONG).show();
//                return true;//表示我已经处理过了
//            }
            wvMien.loadUrl(url);
            return true;
            //return super.shouldOverrideUrlLoading(view, url);
        }

    };


    //WebChromeClient主要辅助WebView处理Javascript的对话框、网站图标、网站title、加载进度等
    private WebChromeClient webChromeClient = new WebChromeClient(){
        //不支持js的alert弹窗，需要自己监听然后通过dialog弹窗
        @Override
        public boolean onJsAlert(WebView webView, String url, String message, JsResult result) {
            AlertDialog.Builder localBuilder = new AlertDialog.Builder(webView.getContext());
            localBuilder.setMessage(message).setPositiveButton("确定",null);
            localBuilder.setCancelable(false);
            localBuilder.create().show();

            //注意:
            //必须要这一句代码:result.confirm()表示:
            //处理结果为确定状态同时唤醒WebCore线程
            //否则不能继续点击按钮
            result.confirm();
            return true;
        }

        //获取网页标题
        @Override
        public void onReceivedTitle(WebView view, String title) {
            super.onReceivedTitle(view, title);
            Log.i("ansen","网页标题:"+title);
            //说明是空白页
            if ("about:blank".equalsIgnoreCase(title) && list.size() > prePosition)
            {
                llNull.setVisibility(View.GONE);
                llTypeTitle.setSelected(false);
                hospitalMienAdapter.setPos(prePosition);
                hospitalMienAdapter.notifyDataSetChanged();
                setWvMien(list.get(prePosition).getIntroduction());
            }
        }

        //加载进度回调
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            //progressBar.setProgress(newProgress);
            LogUtil.d("医院风采进度：" + newProgress);
        }
    };

    @Override
    public void initPresenter() {
        mPresenter = new HospitalMienPresenter();
    }

    @Override
    public void showError(String msg) {

    }

    @Override

    public void getMien(List<MienInfoDao> list) {
        llTypeTitle.setVisibility(View.VISIBLE);
        list.get(0).setSelete(true);
        this.list.addAll(list);
        hospitalMienAdapter.notifyDataSetChanged();
        setWvMien(this.list.get(0).getIntroduction());
    }

    @Override
    public void getMienNull() {
        llNull.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_type_title:
                if (llTypeTitle.isSelected()) {
                    recyclerView.setVisibility(View.VISIBLE);
                    tvTypeTitle.setText("收起");
                } else {
                    recyclerView.setVisibility(View.GONE);
                    tvTypeTitle.setText("展开");
                }
                llTypeTitle.setSelected(!llTypeTitle.isSelected());
                break;

            case R.id.ll_back:
                pop();
                break;

            case R.id.tv_null:
                pop();
                break;
        }
    }

    public void setWvMien(String data) {
        if (TextUtils.isEmpty(data)) {
            getMienNull();
            return;
        }
        wvMien.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                return true;
            }
        });
        wvMien.loadDataWithBaseURL(null, data, "text/html", "UTF-8", null);
    }
}
