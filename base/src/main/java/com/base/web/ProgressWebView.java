package com.base.web;

/**
 * Created by rentianjituan on 2016/8/23.
 */


import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.webkit.WebView;
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

    Context context;
    WebView mWebView;
    ProgressBar mProgressBar;

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
    }


    public void loadUrl(String url) {
        if (url == null) {
            url = "";
        }
        mWebView.loadUrl(url);
    }

    public ProgressBar getProgressBar() {
        return mProgressBar;
    }

    public WebView getWebView() {
        return mWebView;
    }

    public void loadData(String data) {
        if (data != null) {
            mWebView.loadData(data, "text/html;charset=UTF-8", null);
        }
    }

}
