package com.base.web;

import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.webkit.ConsoleMessage;
import android.webkit.CookieManager;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.base.util.LoginManager;

import java.io.File;
import java.util.List;

/**
 * Created by GaoTing on 2019/4/22.
 * <p>
 * Explain:自定义WebViewClient
 */
public class WebClient {
    protected WebViewClient webViewClient = new WebViewClient() {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            return WebClient.this.shouldOverrideUrlLoading(view, request.getUrl().toString());
        }

        @Override
        public void onLoadResource(WebView view, String url) {
            WebClient.this.onLoadResource(view, url);
            super.onLoadResource(view, url);
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            if (listener != null) {
                listener.onLoadUrlStart();
            }
            synCookies(url, LoginManager.getInstance().readCookies());
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            if (listener != null) {
                listener.onLoadUrlFinish();
            }
            LoginManager.getInstance().saveCookies(CookieManager.getInstance().getCookie(url));
            super.onPageFinished(view, url);
        }

        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
            WebClient.this.onReceivedError(view, request, error);
            super.onReceivedError(view, request, error);
        }

        @Nullable
        @Override
        public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
            WebResourceResponse response = WebClient.this.shouldInterceptRequest(view, request);
            return response;
        }
    };


    protected WebChromeClient webChromeClient = new WebChromeClient() {
        @Override
        public boolean onJsAlert(WebView view, String url, String message, final JsResult result) {
            return WebClient.this.onJsAlert(view, url, message, result);
        }

        @Override
        public boolean onJsConfirm(WebView view, String url, String message, final JsResult result) {
            return WebClient.this.onJsConfirm(view, url, message, result);
        }

        @Override
        public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
            return WebClient.this.onConsoleMessage(consoleMessage);
        }

        @Override
        public boolean onJsPrompt(WebView view, String url, String message, String defaultValue, final JsPromptResult result) {
            return WebClient.this.onJsPrompt(view, url, message, defaultValue, result);
        }

        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            if (listener != null)
                listener.setProgressChanged(newProgress);
            super.onProgressChanged(view, newProgress);
        }

        // 设置程序的Title
        @Override
        public void onReceivedTitle(WebView view, String title) {
            WebClient.this.onReceivedTitle(view, title);
            super.onReceivedTitle(view, title);
        }

        @Override
        public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams) {
            return WebClient.this.onShowFileChooser(webView, filePathCallback, fileChooserParams);
        }
    };
    private WebLoadListener listener;


    protected void setLoadListener(WebLoadListener listener) {
        this.listener = listener;
    }

    /**
     * 加载拦截
     * 若添加处理,则返回true
     * {@link android.webkit.WebViewClient#shouldOverrideUrlLoading(WebView, String)}
     */
    protected boolean shouldOverrideUrlLoading(WebView view, String url) {
        return false;
    }

    /**
     * 加载资源文件
     * {@link android.webkit.WebViewClient#onLoadResource(WebView, String)}
     */
    protected void onLoadResource(WebView webView, String url) {
    }


    /**
     * 资源加载出错
     * {@link android.webkit.WebViewClient#onReceivedError(WebView, WebResourceRequest, WebResourceError)}
     */
    protected void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
    }

    /**
     * 拦截请求资源并返回数据
     * {@link android.webkit.WebViewClient#shouldInterceptRequest(WebView, WebResourceRequest)}
     */
    protected WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
        return null;
    }

    /**
     * 接收文档标题
     * {@link android.webkit.WebChromeClient#onReceivedTitle(WebView, String)}
     */
    public void onReceivedTitle(WebView view, String title) {
    }

    /**
     * 显示一个alert对话框
     * {@link android.webkit.WebChromeClient#onJsAlert(WebView, String, String, JsResult)}
     */
    public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
        return false;
    }

    /**
     * 显示一个confirm对话框
     * {@link android.webkit.WebChromeClient#onJsConfirm(WebView, String, String, JsResult)}
     */
    public boolean onJsConfirm(WebView view, String url, String message, JsResult result) {
        return false;
    }

    /**
     * 显示一个prompt对话框
     * {@link android.webkit.WebChromeClient#onJsPrompt(WebView, String, String, String, JsPromptResult)}
     */
    public boolean onJsPrompt(WebView view, String url, String message, String defaultValue, JsPromptResult result) {
        return false;
    }


    /**
     * 为'<input type="file" />'显示文件选择器，返回false使用默认处理
     * 重写此方法获取文件并调用 JSUpLoadFiles方法上传文件 需返回true
     * {@link android.webkit.WebChromeClient#onShowFileChooser(WebView, ValueCallback, WebChromeClient.FileChooserParams)}
     */
    public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, WebChromeClient.FileChooserParams fileChooserParams) {
        return false;
    }

    /**
     * 接收JavaScript控制台消息
     * {@link android.webkit.WebChromeClient#onConsoleMessage(ConsoleMessage)}
     */
    public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
        return false;
    }

    /**
     * JS上传文件
     *
     * @param paths 文件路径集合
     */
    public void JSUpLoadFiles(ValueCallback<Uri[]> filePathCallback, List<String> paths) {
        Uri[] results = new Uri[paths.size()];
        for (int i = 0; i < results.length; i++) {
            results[i] = Uri.fromFile(new File(paths.get(i)));
        }
        if (filePathCallback != null) {
            filePathCallback.onReceiveValue(results);
        }
    }

    /**
     * 同步cookie
     */
    public void synCookies(String url, String cookies) {
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setAcceptCookie(true);
        cookieManager.removeSessionCookies(value -> {
        });//移除
        if (!TextUtils.isEmpty(cookies)) {
            String[] cookieArray = cookies.split(";");// 多个Cookie是使用分号分隔的
            for (int i = 0; i < cookieArray.length; i++) {
                int position = cookieArray[i].indexOf("=");// 在Cookie中键值使用等号分隔
                String cookieName = cookieArray[i].substring(0, position);// 获取键
                String cookieValue = cookieArray[i].substring(position + 1);// 获取值
                // 解码使用 URLEncoder.encode(str, "UTF-8");
                cookieManager.setCookie(url, cookieName + "=" + cookieValue);
            }
        }

        CookieManager.getInstance().flush();
    }
}
