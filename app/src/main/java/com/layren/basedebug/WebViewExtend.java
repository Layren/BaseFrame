package com.layren.basedebug;

import android.content.Context;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.widget.Toast;

import com.base.util.ToastAlone;
import com.base.web.WebViewConfig;

/**
 * Created by GaoTing on 2019/4/18.
 * <p>
 * Explain:WebViewConfig扩展
 */
public class WebViewExtend extends WebViewConfig {
    private Context context;

    WebViewExtend(Context context) {
        this.context = context;
    }

    /**
     * 重写自定义 webView;
     *
     * @param webView
     */
    @Override
    protected void deployWebView(WebView webView) {
        super.deployWebView(webView);
    }

    //测试Js调用本地方法
    @JavascriptInterface
    public void test2() {
        ToastAlone.showToast(context, "test2.............", Toast.LENGTH_SHORT);
    }

}
