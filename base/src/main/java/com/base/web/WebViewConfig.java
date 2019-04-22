package com.base.web;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.ValueCallback;
import android.webkit.WebSettings;
import android.webkit.WebView;

import java.util.List;

/**
 * Created by GaoTing on 2019/4/18.
 * <p>
 * Explain:webView配置.
 * 可继承并自定义配置webView 并添加JS方法
 */
public abstract class WebViewConfig {
    private WebClient client;
    private WebLoadListener listener;

    /**
     * 将配置添加到webView
     * 应于Listener和client设置完成后调用
     *
     * @param webView
     */
    @SuppressLint("SetJavaScriptEnabled")
    protected void deployWebView(WebView webView) {
        WebSettings webSettings = webView.getSettings();
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        webSettings.setJavaScriptEnabled(true);
        webSettings.setAllowFileAccess(true);
        webSettings.setSupportZoom(true);
        webSettings.setDefaultZoom(WebSettings.ZoomDensity.MEDIUM);
        webSettings.setBuiltInZoomControls(false);
        webSettings.setDefaultFontSize(16);
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setAppCacheEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setSaveFormData(true);// 保存表单数据
        webView.addJavascriptInterface(this, "android");
        webView.setScrollBarStyle(View.SCREEN_STATE_OFF);
        if (client != null) {
            webView.setWebViewClient(client.webViewClient);
            webView.setWebChromeClient(client.webChromeClient);
        }
        webView.setOnKeyListener((v, keyCode, event) -> {
            if (event.getAction() == KeyEvent.ACTION_DOWN) {
                if (keyCode == KeyEvent.KEYCODE_BACK && webView.canGoBack()) { // 表示按返回键
                    webView.goBack(); // 后退
                    return true; // 已处理
                }
            }
            return false;
        });
    }

    /**
     * 添加加载监听,
     * 此监听提供 开始加载,加载进度,加载完成三个回调方法
     * 无需监听可不添加
     *
     * @param listener
     */
    public void setLoadListener(WebLoadListener listener) {
        this.listener = listener;
        if (client != null)
            client.setLoadListener(listener);
    }

    /**
     * 添加 WebClient, 实现了WebViewClient和WebChromeClient
     *
     * @param client
     */
    public void setClient(WebClient client) {
        this.client = client;
        if (listener != null)
            this.client.setLoadListener(listener);
    }

    public void upLoadFiles(ValueCallback<Uri[]> filePathCallback, List<String> paths) {
        if (client != null)
            client.JSUpLoadFiles(filePathCallback,paths);
    }

    @JavascriptInterface
    public void test() {
        Log.e("WebViewConfig", "测试JS调取本地成功!");
    }
}
