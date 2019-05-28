package com.muju.note.launcher.app.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.muju.note.launcher.R;
import com.muju.note.launcher.base.BaseActivity;
import com.muju.note.launcher.base.LauncherApplication;
import com.muju.note.launcher.url.UrlUtil;
import com.muju.note.launcher.util.adverts.NewAdvertsUtil;
import com.muju.note.launcher.util.app.MobileInfoUtil;
import com.muju.note.launcher.util.log.LogUtil;
import com.muju.note.launcher.util.sign.Signature;
import com.muju.note.launcher.view.BackTitleView;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import butterknife.ButterKnife;

/**
 * 项目统一的web加载入口
 */
public class WebActivity extends BaseActivity {
    public static final String WEB_URL = "web_url";
    public static final String WEB_TITLE = "web_title";
    public static final String IS_FINISH = "isFinish";
    public static final String ADVERTID = "advertId";

    boolean isFinish = false;

    WebView webView;
    View loadingView;
    View emptyView;
    BackTitleView titleView;

    private String url = "https://www.baidu.com/";
    private int advertId = 0;
    private long startTime;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ButterKnife.bind(this);

       /* List<AdvertsBean> adverts = SPUtil.getDataList(AdvertsTopics.CODE_VERTICAL);
        if (adverts != null && adverts.size() > 0) {
            Log.e("zkpad", "竖条走缓存");
            NewAdvertsUtil.getInstance().showByBanner(adverts, banner);
        }*/

        initViews();
        setWeb();
        Log.e("zkpad", "url=" + getIntent().getStringExtra(WEB_URL));
        Log.e("zkpad", "title=" + getIntent().getStringExtra(WEB_TITLE));
        if (getIntent().getStringExtra(WEB_URL) != null) {
            url = getIntent().getStringExtra(WEB_URL);
            webView.loadUrl(url);
        }
        if (getIntent().getStringExtra(WEB_TITLE) != null) {
            titleView.setTitle(getIntent().getStringExtra(WEB_TITLE));
        }
        advertId = getIntent().getIntExtra(ADVERTID, 0);
        isFinish = getIntent().getBooleanExtra(IS_FINISH, false);
        if (advertId != 0) {
            startTime = System.currentTimeMillis();
            setStartProtection(false);
        }
    }

    @Override
    public int getLayout() {
        return R.layout.activity_web;
    }

    @Override
    public void initData() {

    }

    private void initViews() {
        webView = findViewById(R.id.web);
        loadingView = findViewById(R.id.loadingView);
        emptyView = findViewById(R.id.emptyView);
        titleView = findViewById(R.id.title);

        emptyView.setVisibility(View.GONE);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }


    private void setWeb() {
        WebSettings webSettings = webView.getSettings();
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
        webView.setWebViewClient(new WebViewClient() {

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                String title = view.getTitle();
                if (!TextUtils.isEmpty(title) && TextUtils.isEmpty(getIntent().getStringExtra
                        (WEB_TITLE))) {
                    titleView.setTitle(title);
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

        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onReceivedTitle(WebView view, String title) {
                //如果不传title过来就是用网页解析的title
                if (!TextUtils.isEmpty(title) && TextUtils.isEmpty(getIntent().getStringExtra
                        (WEB_TITLE))) {
                    titleView.setTitle(title);
                }
                super.onReceivedTitle(view, title);
            }
        });
    }


    @Override
    public void finish() {
        if (isFinish) {
            webView.removeAllViews();
            webView.destroy();
            super.finish();
            return;
        }
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            webView.removeAllViews();
            webView.destroy();
            super.finish();
        }
    }

    @Override
    protected void onDestroy() {
        if (getIntent().getBooleanExtra("isFlag", false)) {
            Map<String, String> params = new HashMap();
            params.put("xjId", "" + getIntent().getIntExtra("xjId", 0));
            String sign = Signature.getSign(params, MobileInfoUtil.getICCID(this));
            OkGo.<String>post(UrlUtil.updateReadFlag()).tag(this)
                    .headers("SIGN", sign)
                    .params(params)
                    .execute(new StringCallback() {
                        @Override
                        public void onSuccess(Response<String> response) {
                            LogUtil.d("标记已读成功");
                        }

                        @Override
                        public void onError(Response<String> response) {
                            super.onError(response);
                            LogUtil.d("标记已读失败");
                        }
                    });
        }

        if (advertId != 0) {
            NewAdvertsUtil.getInstance().addData(advertId, NewAdvertsUtil.TAG_BROWSETIME, System
                    .currentTimeMillis() - startTime);
        }

        super.onDestroy();
    }

    public static void launch(Context context, String webUrl, String title) {
        launch(context, false, webUrl, title);
    }

    public static void launchXJ(Context context, String webUrl, String title, int xjId) {
        Intent intent = new Intent(context, WebActivity.class);
        intent.putExtra(WebActivity.WEB_URL, webUrl);
        intent.putExtra(WebActivity.IS_FINISH, false);
        intent.putExtra(WebActivity.WEB_TITLE, title);
        intent.putExtra("isFlag", true);
        intent.putExtra("xjId", xjId);
        context.startActivity(intent);
    }

    public static void launch(Context context, boolean isFinish, String webUrl, String title) {
        Intent intent = new Intent(context, WebActivity.class);
        intent.putExtra(WebActivity.WEB_URL, webUrl);
        intent.putExtra(WebActivity.IS_FINISH, isFinish);
        intent.putExtra(WebActivity.WEB_TITLE, title);
        context.startActivity(intent);
    }

    public static void launch(Context context, boolean isFinish, String webUrl, String title, int
            advertId) {
        Intent intent = new Intent(context, WebActivity.class);
        intent.putExtra(WebActivity.WEB_URL, webUrl);
        intent.putExtra(WebActivity.IS_FINISH, isFinish);
        intent.putExtra(WebActivity.WEB_TITLE, title);
        intent.putExtra(WebActivity.ADVERTID, advertId);
        context.startActivity(intent);
    }

    /**
     * 清除WebView缓存
     */
    public void clearWebViewCache() {

        LogUtil.i("清除webview缓存");

        //清理Webview缓存数据库
        try {
            deleteDatabase("webview.db");
            deleteDatabase("webviewCache.db");
        } catch (Exception e) {
            e.printStackTrace();
        }

        //WebView 缓存文件
        File appCacheDir = new File("/data/data/com,muju.note.launcher/app_webview");

        File webviewCacheDir = new File(getCacheDir().getAbsolutePath() + "/webviewCache");

        LogUtil.i("appCacheDir:" + appCacheDir);
        LogUtil.i("webviewCacheDir:" + webviewCacheDir);

        //删除webview 缓存目录
        if (webviewCacheDir.exists()) {
            deleteFile(webviewCacheDir);
        }
        //删除webview 缓存 缓存目录
        if (appCacheDir.exists()) {
            deleteFile(appCacheDir);
        }
    }

    /**
     * 递归删除 文件/文件夹
     *
     * @param file
     */
    public void deleteFile(File file) {
        if (file.exists()) {
            if (file.isFile()) {
                file.delete();
            } else if (file.isDirectory()) {
                File files[] = file.listFiles();
                for (int i = 0; i < files.length; i++) {
                    deleteFile(files[i]);
                }
            }
            file.delete();
        } else {
        }
    }


    public void clearWebViewCookie() {
        LogUtil.i("清除webview的Cookie");
        CookieSyncManager.createInstance(LauncherApplication.getContext());
        CookieManager cookieManager = CookieManager.getInstance();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            cookieManager.removeSessionCookies(null);
            cookieManager.removeAllCookie();
            cookieManager.flush();
        } else {
            cookieManager.removeSessionCookies(null);
            cookieManager.removeAllCookie();
            CookieSyncManager.getInstance().sync();
        }
    }
}
