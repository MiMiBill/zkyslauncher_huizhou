package com.muju.note.launcher.app.publicui;

import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.muju.note.launcher.R;
import com.muju.note.launcher.base.BaseFragment;
import com.muju.note.launcher.util.adverts.AdvertsUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class WebViewFragment extends BaseFragment implements View.OnClickListener {

    private static final String TAG = WebViewFragment.class.getSimpleName();

    public static final String WEB_URL = "web_url";
    public static final String WEB_TITLE = "web_title";
    public static final String WEB_IS_CUSTOM_MSG = "web_is_custom_msg";
    public static final String WEB_CUSTOM_ID = "web_custom_id";
    public static final String WEB_ADVERT_ID = "web_advert_id";

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.ll_back)
    LinearLayout llBack;
    @BindView(R.id.web)
    WebView web;

    View loadingView;
    View emptyView;
    @BindView(R.id.rl_back)
    RelativeLayout rlBack;

    private int advertId = 0; //广告ID
    private long startTime; // 广告浏览开始时间
    private String title;
    private String url;
    private boolean isCustomMsg = false; // 是否是宣教推送
    private int customId; //宣教ID

    public static WebViewFragment newInstance(String title, String url) {
        return newInstance(title, url, 0, false, 0);
    }

    public static WebViewFragment newInstance(String title, String url, int advertId) {
        return newInstance(title, url, 0, false, advertId);
    }

    public static WebViewFragment newInstance(String title, String url, int customId, boolean isCustomMsg, int advertId) {
        Bundle args = new Bundle();
        args.putString(WEB_TITLE, title);
        args.putString(WEB_URL, url);
        args.putBoolean(WEB_IS_CUSTOM_MSG, isCustomMsg);
        args.putInt(WEB_CUSTOM_ID, customId);
        args.putInt(WEB_ADVERT_ID, advertId);
        WebViewFragment fragment = new WebViewFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public int getLayout() {
        return R.layout.fragment_web;
    }

    @Override
    public void initData() {

        loadingView = getView().findViewById(R.id.loadingView);
        emptyView = getView().findViewById(R.id.emptyView);
        emptyView.setVisibility(View.GONE);

        title = getArguments().getString(WEB_TITLE);
        url = getArguments().getString(WEB_URL);
        isCustomMsg = getArguments().getBoolean(WEB_IS_CUSTOM_MSG, false);
        customId = getArguments().getInt(WEB_CUSTOM_ID);
        tvTitle.setText(title);
        llBack.setOnClickListener(this);
        rlBack.setOnClickListener(this);

        startTime = System.currentTimeMillis();

        setWeb();

        web.loadUrl(url);

    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void showError(String msg) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_back:
            case R.id.rl_back:
                try {
                    if (web == null) {
                        pop();
                        return;
                    }
                    if (web.canGoBack()) {
                        web.goBack();
                        return;
                    }
                    pop();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    /**
     * 设置webView参数
     */
    private void setWeb() {
        WebSettings webSettings = web.getSettings();
        // 让WebView能够执行javaScript
        webSettings.setJavaScriptEnabled(true);
        // 让JavaScript可以自动打开windows
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        // 设置缓存
//        webSettings.setAppCacheEnabled(true);
        // 设置缓存模式,一共有四种模式
//        webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        // 设置缓存路径
//        webSettings.setAppCachePath("");
        // 支持缩放(适配到当前屏幕)
        webSettings.setSupportZoom(true);
        // 将图片调整到合适的大小
        webSettings.setUseWideViewPort(true);
        // 支持内容重新布局,一共有四种方式
        // 默认的是NARROW_COLUMNS
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        webSettings.setUserAgentString("User-Agent:Android");
        // 设置可以被显示的屏幕控制
        webSettings.setDisplayZoomControls(true);
        // 设置默认字体大小
//        webSettings.setDefaultFontSize(12);

        // 复写shouldOverrideUrlLoading()方法，使得打开网页时不调用系统浏览器， 而是在本WebView中显示
        web.setWebViewClient(new WebViewClient() {

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                String title = view.getTitle();
                if (!TextUtils.isEmpty(title) && TextUtils.isEmpty(getArguments().getString(WEB_TITLE))) {
                    tvTitle.setText(title);
                }
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                loadingView.setVisibility(View.GONE);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                view.loadUrl(String.valueOf(request.getUrl()));
                return true;
            }

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                handler.proceed();
            }

            @Override
            public void onReceivedError(WebView view, int errorCode,
                                        String description, String failingUrl) {
                // TODO Auto-generated method stub
                //  webView.loadUrl(url);
                loadingView.setVisibility(View.GONE);
                //todo 这里应该加载我们的404页面
//                webView.setVisibility(View.GONE);
            }
        });

        web.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onReceivedTitle(WebView view, String title) {
                if (!TextUtils.isEmpty(title) && TextUtils.isEmpty(getArguments().getString(WEB_TITLE))) {
                    tvTitle.setText(title);
                }
                super.onReceivedTitle(view, title);
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        long currentTime = System.currentTimeMillis();
        if (advertId != 0) {
            AdvertsUtil.getInstance().addData(advertId, AdvertsUtil.TAG_BROWSETIME, currentTime - startTime);
            AdvertsUtil.getInstance().addDataInfo(advertId, AdvertsUtil.TAG_BROWSETIME, startTime, currentTime);
        }
        if (web != null) {
            web.removeAllViews();
            web.destroy();
        }
    }
}
