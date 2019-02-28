package com.base.view;

/**
 * Created by rentianjituan on 2016/8/23.
 */


import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.ConsoleMessage;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.base.R;


/**
 * @author gyw
 * @version 1.0
 * @time: 2015-12-25 下午1:08:25
 * @fun:
 * @fix: 修改加载失败后的中午显示
 */
public class ProgressWebView extends LinearLayout {

    private static final String TAG = "ProgressWebView";
    Context context;
    WebView mWebView;
    ProgressBar mProgressBar;
    private String url;
    private String js = "";

    public ProgressWebView(Context context) {
        this(context, null);
    }

    public ProgressWebView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ProgressWebView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView(context);
    }

    private void initView(Context context) {
        View view = View.inflate(context, R.layout.view_web_progress, this);
        mWebView = view.findViewById(R.id.web_view);
        mProgressBar = view.findViewById(R.id.progress_bar);
        this.context = context;
        initWebview();

    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void loadUrl(String url) {
        if (url == null) {
            url = "";
        }
        mWebView.loadUrl(url);
    }

    public void loadJS(String js) {
        this.js = js;
    }

    public void loadData(String data) {
        if (data != null) {
            mWebView.loadData(data, "text/html;charset=UTF-8", null);
        }
    }

    @SuppressLint("JavascriptInterface")
    private void initWebview() {
        mWebView.addJavascriptInterface(context, "android");
        WebSettings webSettings = mWebView.getSettings();
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        webSettings.setJavaScriptEnabled(true);
        // 设置可以访问文件
        webSettings.setAllowFileAccess(true);
        // 设置可以支持缩放
        webSettings.setSupportZoom(true);
        // 设置默认缩放方式尺寸是far
        webSettings.setDefaultZoom(WebSettings.ZoomDensity.MEDIUM);
        // 设置出现缩放工具
        webSettings.setBuiltInZoomControls(false);
        webSettings.setDefaultFontSize(16);
        mWebView.setScrollBarStyle(View.SCREEN_STATE_OFF);
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setAppCacheEnabled(true);
        mWebView.getSettings().setDomStorageEnabled(true);
        mWebView.getSettings().setSaveFormData(true);// 保存表单数据
        // 设置WebViewClient
        mWebView.setWebViewClient(new WebViewClient() {
            // url拦截
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            // 页面开始加载
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                mProgressBar.setVisibility(View.VISIBLE);
                super.onPageStarted(view, url, favicon);
            }

            // 页面加载完成
            @Override
            public void onPageFinished(WebView view, String url) {
                mProgressBar.setVisibility(View.GONE);
                super.onPageFinished(view, url);
                if (js != null && js.length() > 0)
                    mWebView.loadUrl(js);
            }

            // WebView加载的所有资源url
            @Override
            public void onLoadResource(WebView view, String url) {
                Log.i(TAG, "onLoadResource");
                super.onLoadResource(view, url);
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                Log.i(TAG, "onReceivedError");
                super.onReceivedError(view, errorCode, description, failingUrl);
            }

        });

        // 设置WebChromeClient
        mWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            // 处理javascript中的alert
            public boolean onJsAlert(WebView view, String url, String message, final JsResult result) {
                Log.d(TAG, "onJsAlert:" + message);
                return super.onJsAlert(view, url, message, result);
            }

            @Override
            // 处理javascript中的confirm
            public boolean onJsConfirm(WebView view, String url, String message, final JsResult result) {
                Log.d(TAG, "onJsConfirm:" + message);
                return super.onJsConfirm(view, url, message, result);
            }

            @Override
            public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
                Log.e(TAG, "JsConsole:" + consoleMessage.message());
                return super.onConsoleMessage(consoleMessage);
            }

            @Override
            // 处理javascript中的prompt
            public boolean onJsPrompt(WebView view, String url, String message, String defaultValue, JsPromptResult result) {
                Log.d(TAG, "onJsPrompt:" + message);
                return super.onJsPrompt(view, url, message, defaultValue, result);
            }

            // 设置网页加载的进度条
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                mProgressBar.setProgress(newProgress);
                super.onProgressChanged(view, newProgress);
            }

            // 设置程序的Title
            @Override
            public void onReceivedTitle(WebView view, String title) {
                Log.d(TAG, "onReceivedTitle:" + title);
                super.onReceivedTitle(view, title);
            }
        });
        mWebView.setOnKeyListener((v, keyCode, event) -> {
            if (event.getAction() == KeyEvent.ACTION_DOWN) {
                if (keyCode == KeyEvent.KEYCODE_BACK && mWebView.canGoBack()) { // 表示按返回键
                    mWebView.goBack(); // 后退
                    return true; // 已处理
                }
            }
            return false;
        });
    }

}
