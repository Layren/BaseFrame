package com.base.net;


import android.os.Handler;
import android.os.Looper;

import com.base.interfaces.SNRequestDataListener;
import com.base.interfaces.SNRequestListener;
import com.base.model.Base;
import com.base.util.ThreadPoolProxy;

public abstract class SNNetAPI implements SNRequestListener {

    private Handler handler = new Handler(Looper.getMainLooper());
    private SNRequestDataListener requestDataListener;
    protected Base base;

    /**
     * http请求方式
     */
    private static final String HTTPMETHOD_POST = "POST";
    private static final String HTTPMETHOD_GET = "GET";

    SNNetAPI(SNRequestDataListener listener) {
        this.requestDataListener = listener;
    }

    /**
     * 该方法处理数据解析
     *
     * @param response api返回原始数据
     */
    public abstract void parseData(String response, int whichAPI);

    public void getUrl(String url, final UrlParameters params, int whichAPI) {
        request(url, params, HTTPMETHOD_GET, whichAPI);
    }

    public void postUrl(String url, final UrlParameters params, int whichAPI) {
        request(url, params, HTTPMETHOD_POST, whichAPI);
    }

    @Override
    public void onComplete(String response, final int whichAPI) {
        parseData(response, whichAPI);
        handler.post(() -> requestDataListener.onCompleteData(base, whichAPI));
    }

    @Override
    public void onError(final Exception e, final int whichAPI) {
        e.printStackTrace();
        handler.post(() -> requestDataListener.onError(e, whichAPI));

    }

    private void request(final String url, final UrlParameters params, final String httpMethod, final int whichAPI) {
        ThreadPoolProxy threadPoolProxy = ThreadPoolProxy.getInstance();
        threadPoolProxy.executeTask(() -> {
            try {
                String resp = null;
                if (httpMethod.equals(SNNetAPI.HTTPMETHOD_GET))
                    resp = HttpManager.getUrl(url, params);
                else {
                    resp = HttpManager.postUrl(url, params);
                }
                onComplete(resp, whichAPI);
            } catch (Exception e) {
                onError(e, whichAPI);
            }
        });
    }

}
