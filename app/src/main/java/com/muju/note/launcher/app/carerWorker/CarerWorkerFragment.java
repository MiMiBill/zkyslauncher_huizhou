package com.muju.note.launcher.app.carerWorker;

import android.app.AlertDialog;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.View;
import android.webkit.JavascriptInterface;
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
//        String url = "http://zk-hugong.battcn.com/index.html?hospId=25&deptId=57&bedNo=123&hospName=%E4%B8%8A%E6%B5%B7%E7%AC%AC%E4%B9%9D%E4%BA%BA%E6%B0%91%E5%8C%BB%E9%99%A2&deptName=%E6%B5%8B%E8%AF%95%E7%A7%91&imei=353769223605675";
//        LogUtil.d("localUrl:" + localUrl);
//        LogUtil.d("carerWorker:" + carerWorker);
//        wvCarerWorker.loadUrl(url);
    }

//    private void initWebview(){
//
//
//        wvCarerWorker.addJavascriptInterface(this,"android");//添加js监听 这样html就能调用客户端
//        wvCarerWorker.setWebChromeClient(webChromeClient);
//        wvCarerWorker.setWebViewClient(webViewClient);
//
//        WebSettings webSettings = wvCarerWorker.getSettings();
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
//        //设置自适应屏幕，两者合用
//        webSettings.setUseWideViewPort(true); //将图片调整到适合webview的大小
//        webSettings.setLoadWithOverviewMode(true); // 缩放至屏幕的大小
//
//
//        webSettings.setAllowFileAccess(true); //设置可以访问文件
//        webSettings.setDomStorageEnabled(true);
//        webSettings.setAppCacheMaxSize(1024 * 1024 * 8);
//        webSettings.setJavaScriptCanOpenWindowsAutomatically(true); //支持通过JS打开新窗口
//        webSettings.setLoadsImagesAutomatically(true); //支持自动加载图片
//        webSettings.setDefaultTextEncodingName("utf-8");//设置编码格式
//
//        //添加与js交互事件 ，js 调用 android（日志2）
//        //wvCarerWorker.addJavascriptInterface(new CarerWorkerFragment.InJavaScriptInterface(), "java_obj");
//        //不支持屏幕缩放
//        webSettings.setSupportZoom(false);
//        webSettings.setBuiltInZoomControls(false);
//
//
//
//    }
    private void initWebview()
    {

        wvCarerWorker.resumeTimers();//必须加上，不然不能网络请求
        wvCarerWorker.onResume();
        wvCarerWorker.addJavascriptInterface(this,"android");//添加js监听 这样html就能调用客户端
        wvCarerWorker.setWebChromeClient(webChromeClient);
        wvCarerWorker.setWebViewClient(webViewClient);
        wvCarerWorker.clearCache(true);
        WebSettings webSettings = wvCarerWorker.getSettings();
        webSettings.setJavaScriptEnabled(true);//允许使用js
        webSettings.setDomStorageEnabled(true);

        getActivity().deleteDatabase("webview.db");
        getActivity().deleteDatabase("webviewCache.db");
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
//        webSettings.setAppCacheMaxSize(1024 * 1024 * 8);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true); //支持通过JS打开新窗口
        webSettings.setLoadsImagesAutomatically(true); //支持自动加载图片
        webSettings.setDefaultTextEncodingName("utf-8");//设置编码格式

        //添加与js交互事件 ，js 调用 android（日志2）
        wvCarerWorker.addJavascriptInterface(new InJavaScriptLocalObj(), "java_obj");
        //不支持屏幕缩放
        webSettings.setSupportZoom(false);
        webSettings.setBuiltInZoomControls(false);
      //  wvCarerWorker.loadUrl("http://zk-hugong.battcn.com/index.html?hospId=4&deptId=2");



        String carerWorker = "http://zk-hugong-prod.battcn.com/index.html?" +
                "hospId="+ActiveUtils.getPadActiveInfo().getHospitalId() +
                "&deptId=" + ActiveUtils.getPadActiveInfo().getDeptId()+
                "&bedNo=" + ActiveUtils.getPadActiveInfo().getBedNumber()+
                "&hospName=" + ActiveUtils.getPadActiveInfo().getHospitalName()+
                "&deptName=" + ActiveUtils.getPadActiveInfo().getDeptName()+
                "&imei=" + ActiveUtils.getPadActiveInfo().getCode() +
                "&timestamp=" + System.currentTimeMillis();

        String localUrl = "file:///android_asset/nurseWork/index.html?" +
                "hospId="+ActiveUtils.getPadActiveInfo().getHospitalId() +
                "&deptId=" + ActiveUtils.getPadActiveInfo().getDeptId()+
                "&bedNo=" + ActiveUtils.getPadActiveInfo().getBedNumber()+
                "&hospName=" + ActiveUtils.getPadActiveInfo().getHospitalName()+
                "&deptName=" + ActiveUtils.getPadActiveInfo().getDeptName()+
                "&imei=" + ActiveUtils.getPadActiveInfo().getCode() +
                "&timestamp=" + System.currentTimeMillis();
         wvCarerWorker.loadUrl(carerWorker);
//        wvCarerWorker.loadUrl(localUrl);
        LogUtil.d("localUrl:" + localUrl);
        LogUtil.d("carerWorker:" + carerWorker);
    }

    @Override
    public void onSupportVisible() {
        super.onSupportVisible();

        wvCarerWorker.resumeTimers();
        wvCarerWorker.onResume();

        String carerWorker = "http://zk-hugong-prod.battcn.com/index.html?" +
                "hospId="+ActiveUtils.getPadActiveInfo().getHospitalId() +
                "&deptId=" + ActiveUtils.getPadActiveInfo().getDeptId()+
                "&bedNo=" + ActiveUtils.getPadActiveInfo().getBedNumber()+
                "&hospName=" + ActiveUtils.getPadActiveInfo().getHospitalName()+
                "&deptName=" + ActiveUtils.getPadActiveInfo().getDeptName()+
                "&imei=" + ActiveUtils.getPadActiveInfo().getCode() +
                "&timestamp=" + System.currentTimeMillis();

        String localUrl = "file:///android_asset/nurseWork/index.html?" +
                "hospId="+ActiveUtils.getPadActiveInfo().getHospitalId() +
                "&deptId=" + ActiveUtils.getPadActiveInfo().getDeptId()+
                "&bedNo=" + ActiveUtils.getPadActiveInfo().getBedNumber()+
                "&hospName=" + ActiveUtils.getPadActiveInfo().getHospitalName()+
                "&deptName=" + ActiveUtils.getPadActiveInfo().getDeptName()+
                "&imei=" + ActiveUtils.getPadActiveInfo().getCode() +
                "&timestamp=" + System.currentTimeMillis();
        // wvCarerWorker.loadUrl(carerWorker);
//        wvCarerWorker.loadUrl(localUrl);
        LogUtil.d("localUrl:" + localUrl);
        LogUtil.d("carerWorker:" + carerWorker);

    }


    @Override
    public void onSupportInvisible() {
        super.onSupportInvisible();

        wvCarerWorker.onPause();
        wvCarerWorker.pauseTimers();
    }

    public class InJavaScriptLocalObj
    {
        @JavascriptInterface
        public void showSource(String content) {
            LogUtil.e("web --content",content);
           // wvCarerWorker.loadDataWithBaseURL(null, content, "text/html", "UTF-8", null);
        }

        @JavascriptInterface
        public void showDescription(String content) {
            LogUtil.e("web --title",content);
        }
    }


    //WebViewClient主要帮助WebView处理各种通知、请求事件
    private WebViewClient webViewClient = new WebViewClient(){
        @Override
        public void onPageFinished(WebView view, String url) {//页面加载完成
            //progressBar.setVisibility(View.GONE);
            view.loadUrl("javascript:window.java_obj.showSource(document.getElementsByTagName('html')[0].innerHTML);");

            // 获取解析<meta name="share-description"content="获取到的值">
            view.loadUrl("javascript:window.java_obj.showDescription(document.querySelector('meta[name=\"share-description\"]').getAttribute('content'));");

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
            LogUtil.d("护工:加载进度：" + newProgress);
        }
    };

    @Override
    public void initPresenter() {

    }

    @Override
    public void showError(String msg) {

    }


    @OnClick({R.id.ll_back,R.id.tv_title})
    public void onViewClicked(View view) {

        switch (view.getId())
        {
            case R.id.ll_back:
            {
                pop();
                break;
            }
            case R.id.tv_title:
            {
                String carerWorker = "http://zk-hugong-prod.battcn.com/index.html?" +
                        "hospId="+ActiveUtils.getPadActiveInfo().getHospitalId() +
                        "&deptId=" + ActiveUtils.getPadActiveInfo().getDeptId()+
                        "&bedNo=" + ActiveUtils.getPadActiveInfo().getBedNumber()+
                        "&hospName=" + ActiveUtils.getPadActiveInfo().getHospitalName()+
                        "&deptName=" + ActiveUtils.getPadActiveInfo().getDeptName()+
                        "&imei=" + ActiveUtils.getPadActiveInfo().getCode() +
                        "&timestamp=" + System.currentTimeMillis();

                String localUrl = "file:///android_asset/nurseWork/index.html?" +
                        "hospId="+ActiveUtils.getPadActiveInfo().getHospitalId() +
                        "&deptId=" + ActiveUtils.getPadActiveInfo().getDeptId()+
                        "&bedNo=" + ActiveUtils.getPadActiveInfo().getBedNumber()+
                        "&hospName=" + ActiveUtils.getPadActiveInfo().getHospitalName()+
                        "&deptName=" + ActiveUtils.getPadActiveInfo().getDeptName()+
                        "&imei=" + ActiveUtils.getPadActiveInfo().getCode() +
                        "&timestamp=" + System.currentTimeMillis();
//                 wvCarerWorker.loadUrl(carerWorker);
//                wvCarerWorker.loadUrl(localUrl);
                String url = "http://zk-hugong.battcn.com/index.html?hospId=25&deptId=57&bedNo=123&hospName=%E4%B8%8A%E6%B5%B7%E7%AC%AC%E4%B9%9D%E4%BA%BA%E6%B0%91%E5%8C%BB%E9%99%A2&deptName=%E6%B5%8B%E8%AF%95%E7%A7%91&imei=353769223605675";
                LogUtil.d("localUrl:" + localUrl);
                LogUtil.d("carerWorker:" + carerWorker);
                wvCarerWorker.loadUrl(url);
                break;
            }
        }
    }

}
