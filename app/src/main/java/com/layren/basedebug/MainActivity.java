package com.layren.basedebug;


import android.content.Intent;
import android.graphics.Color;
import android.media.SoundPool;
import android.net.Uri;
import android.util.Log;
import android.webkit.JsResult;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

import com.base.adapter.RefreshViewAdapter;
import com.base.baseClass.BaseActivity;
import com.base.interfaces.RefreshViewAdapterListener;
import com.base.model.MultiModel;
import com.base.pickphoto.PickPhoto;
import com.base.util.PermissionManager;
import com.base.view.ItemDecoration;
import com.base.view.NestedRecyclerView;
import com.base.view.RefreshRecyclerView;
import com.base.web.ProgressWebView;
import com.base.web.WebClient;
import com.base.web.WebLoadListener;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by GaoTing on 2018/9/26.
 *
 * @TODO :
 */
public class MainActivity extends BaseActivity {
    @BindView(R.id.recycler)
    RefreshRecyclerView recyclerView;
    @BindView(R.id.recyclers)
    NestedRecyclerView recyclerViews;
    private SoundPool soundPool;
    private int soundId;
    @BindView(R.id.p_web_view)
    ProgressWebView webView;

    private PickPhoto pickPhoto;

    @Override
    protected int getLayoutId() {

        return R.layout.activity_main;
    }


    @Override
    protected void initView() {
        Log.e("111111111", "1" + MainActivity.class);
        Log.e("111111111", "2" + getClass());
        boolean isStorage = PermissionManager.query(this, PermissionManager.STORAGE);
        if (!isStorage) {
            PermissionManager.granted(this, PermissionManager.STORAGE, 1);
        }
        findViewById(R.id.text_v).setOnClickListener(v -> {
        });
        recyclerView.setMultiAdapter(((holder, item, itemType) -> {
            MultiItemMudle mudle = (MultiItemMudle) item;
            switch (itemType) {
                case 0:
                    holder.setText(R.id.textRed, mudle.getText());
                    break;
                case 1:
                    holder.setText(R.id.textBlue, mudle.getText());
                    break;
            }
        }), R.layout.red, R.layout.blue);
        recyclerView.addItemDecoration(new ItemDecoration(1, Color.BLUE));
        List<Object> list = new ArrayList<>();
        list.add("11111111");
        list.add("12222222");
        list.add("12333333");
        list.add("12344444");
        list.add("12345555");
        list.add("12345666");
        list.add("12345677 ");
        List<MultiItemMudle> list2 = new ArrayList<>();
        for (int i = 0; i < 11; i++) {
            MultiItemMudle mudle = new MultiItemMudle();
            if (i < 1 || i == 5) {
                mudle.setItemType(0);
                mudle.setSpanSize(3);
            } else
                mudle.setItemType(1);
            mudle.setText("====" + i + "====");
            list2.add(mudle);
        }
        recyclerView.setMulitData(list2);
        recyclerView.setLoadMoreEnd();
        RefreshViewAdapter adapter = new RefreshViewAdapter(R.layout.blue, new RefreshViewAdapterListener() {
            @Override
            public void setHolder(int layoutResId, BaseViewHolder holder, Object item) {
                holder.setText(R.id.textBlue, (String) item);
                holder.setBackgroundColor(R.id.textBlue, Color.WHITE);
                holder.setTextColor(R.id.textBlue, Color.BLUE);
            }
        });
        recyclerViews.setAdapter(adapter);
        recyclerViews.addItemDecoration(new ItemDecoration(1, Color.RED));
        adapter.setNewData(list);
        extend = new WebViewExtend(this);
        pickPhoto = new PickPhoto(MainActivity.this);


        extend.setLoadListener(new WebLoadListener() {
            @Override
            public void onLoadUrlStart() {

            }

            @Override
            public void onLoadUrlFinish() {

            }

            @Override
            public void setProgressChanged(int progress) {

            }
        });
        extend.setClient(new WebClient() {
            @Override
            public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, WebChromeClient.FileChooserParams fileChooserParams) {
                MainActivity.this.filePathCallback = filePathCallback;
                pickPhoto.albumSelect(psr -> extend.upLoadFiles(filePathCallback, psr.getUrls()));

                return true;
            }

            @Override
            public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
                Log.e("alert", url + "\n" + message);
                return super.onJsAlert(view, url, message, result);
            }
        });
        extend.deployWebView(webView.getWebView());
        webView.loadUrl("file:///android_asset/test.html");

    }

    private ValueCallback<Uri[]> filePathCallback;
    private List<String> results = new ArrayList<>();
    WebViewExtend extend;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        pickPhoto.setResult(requestCode, resultCode, data);
    }

    @Override
    public void setHolder(BaseViewHolder holder, MultiModel item, int itemType) {
        super.setHolder(holder, item, itemType);
        MultiItemMudle mudle = (MultiItemMudle) item;
        switch (itemType) {
            case 0:
                holder.setText(R.id.textRed, mudle.getText());
                break;
            case 1:
                holder.setText(R.id.textBlue, mudle.getText());
                break;
        }
    }


}
