package com.base.web;

/**
 * Created by GaoTing on 2019/4/22.
 * <p>
 * Explain:webView 加载监听
 */
public interface WebLoadListener {
    void onLoadUrlStart();

    void onLoadUrlFinish();

    void setProgressChanged(int progress);
}
