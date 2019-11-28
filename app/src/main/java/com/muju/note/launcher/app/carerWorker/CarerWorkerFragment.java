package com.muju.note.launcher.app.carerWorker;

import android.app.AlertDialog;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.View;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.muju.note.launcher.R;
import com.muju.note.launcher.base.BaseFragment;
import com.muju.note.launcher.util.ActiveUtils;
import com.muju.note.launcher.util.log.LogUtil;

import butterknife.BindView;
import butterknife.OnClick;

public class CarerWorkerFragment extends BaseFragment {
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.rel_titlebar)
    RelativeLayout relTitlebar;
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_back)
    TextView tvBack;
    @BindView(R.id.ll_back)
    LinearLayout llBack;

    @BindView(R.id.wv_carer_worker)
    WebView wvCarerWorker;


    @Override
    public int getLayout() {
        return R.layout.fragment_carer_worker;
    }

    @Override
    public void initData() {
        relTitlebar.setBackgroundColor(getResources().getColor(R.color.white));
        tvTitle.setText("护工预约");
        initWebview();

    }

    private void initWebview()
    {

        wvCarerWorker.addJavascriptInterface(this,"android");//添加js监听 这样html就能调用客户端
        wvCarerWorker.setWebChromeClient(webChromeClient);
        wvCarerWorker.setWebViewClient(webViewClient);

        WebSettings webSettings = wvCarerWorker.getSettings();
        webSettings.setJavaScriptEnabled(true);//允许使用js

        /**
         * LOAD_CACHE_ONLY: 不使用网络，只读取本地缓存数据
         * LOAD_DEFAULT: （默认）根据cache-control决定是否从网络上取数据。
         * LOAD_NO_CACHE: 不使用缓存，只从网络获取数据.
         * LOAD_CACHE_ELSE_NETWORK，只要本地有，无论是否过期，或者no-cache，都使用缓存中的数据。
         */
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);//不使用缓存，只从网络获取数据.

        //不支持屏幕缩放
        webSettings.setSupportZoom(false);
        webSettings.setBuiltInZoomControls(false);
      //  wvCarerWorker.loadUrl("http://zk-hugong.battcn.com/index.html?hospId=4&deptId=2");
        String carerWorker = "http://zk-hugong.battcn.com/index.html?" +
                "hospId="+ActiveUtils.getPadActiveInfo().getHospitalId() +
                "&deptId=" + ActiveUtils.getPadActiveInfo().getDeptId()+
                "&bedNo=" + ActiveUtils.getPadActiveInfo().getBedNumber()+
                "&hospName=" + ActiveUtils.getPadActiveInfo().getHospitalName()+
                "&deptName=" + ActiveUtils.getPadActiveInfo().getDeptName()+
                "&imei=" + ActiveUtils.getPadActiveInfo().getCode();
        wvCarerWorker.loadUrl(carerWorker);
        LogUtil.d("carerWorker:" + carerWorker);
    }



    //WebViewClient主要帮助WebView处理各种通知、请求事件
    private WebViewClient webViewClient = new WebViewClient(){
        @Override
        public void onPageFinished(WebView view, String url) {//页面加载完成
            //progressBar.setVisibility(View.GONE);
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
            return super.shouldOverrideUrlLoading(view, url);
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
            if ("about:blank".equalsIgnoreCase(title))
            {
               pop();
            }
        }

        //加载进度回调
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            //progressBar.setProgress(newProgress);
        }
    };

    @Override
    public void initPresenter() {

    }

    @Override
    public void showError(String msg) {

    }


    @OnClick(R.id.ll_back)
    public void onViewClicked() {
        pop();
    }

}
