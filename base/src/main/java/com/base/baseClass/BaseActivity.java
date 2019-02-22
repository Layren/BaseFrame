package com.base.baseClass;

import android.app.Dialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.StringRes;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.base.config.BPConfig;
import com.base.interfaces.RefreshViewAdapterListener;
import com.base.interfaces.RefreshViewMultiItemAdapterListener;
import com.base.interfaces.SNRequestDataListener;
import com.base.model.Base;
import com.base.model.MultiModel;
import com.base.net.NetStateCheck;
import com.base.util.ActivityManager;
import com.base.util.CustomDialog;
import com.base.util.DialogStringInfo;
import com.base.util.ToastAlone;
import com.base.view.RefreshRecyclerView;
import com.chad.library.adapter.base.BaseViewHolder;
import com.base.R;
import com.gyf.barlibrary.ImmersionBar;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;

import butterknife.ButterKnife;


public abstract class BaseActivity extends AppCompatActivity implements SNRequestDataListener, RefreshViewAdapterListener, RefreshViewMultiItemAdapterListener {

    protected int screenWidth;
    protected int screenHeight;
    public Dialog loadingDialog;
    protected int apiCurReturnCount;//
    protected int apiALLCount;//

    protected Dialog dialogVersion;

    protected BPBroadcastReceiver broadcastReceiver;
    protected LocalBroadcastManager localBroadcastManager;

    protected TextView title;
    protected RelativeLayout layout;
    protected ImageView back;
    protected TextView leftTv;
    protected TextView rightTv;
    protected TextView rightTv2;
    private boolean isFragment = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        loadingDialog = CustomDialog.LineDialog(this);
        DisplayMetrics dm = new DisplayMetrics();
        Log.e("curActivity : ", this.getClass().getName());
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        screenWidth = dm.widthPixels;
        screenHeight = dm.heightPixels;
        BPConfig.SCREEN_WIDTH = screenWidth;
        BPConfig.SCREEN_HEIGHT = screenHeight;
        BPConfig.DENSITY = this.getResources().getDisplayMetrics().density;
        ActivityManager.getAppManager().addActivity(this);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(getLayoutId());
        if (BPConfig.IS_WHITE_HEADER)
            setWhiteHeaderView();
        initView(savedInstanceState);
    }

    /**
     * 返回布局ID
     *
     * @return
     */
    protected abstract int getLayoutId();

    /**
     * 初始化
     */
    protected abstract void initView();

    protected void initView(Bundle savedInstanceState) {
        initView();
    }

    private void setWhiteHeaderView() {
        if (layout != null) {
            layout.setBackgroundColor(Color.WHITE);
        }
        if (title != null) {
            title.setTextColor(Color.BLACK);
        }
        if (back != null) {
            back.setImageResource(R.drawable.back_black);
        }
        if (leftTv != null) {
            leftTv.setTextColor(Color.BLACK);
        }
        if (rightTv != null) {
            rightTv.setTextColor(Color.BLACK);
        }
        if (rightTv2 != null) {
            rightTv2.setTextColor(Color.BLACK);
        }
    }

    /**
     * 设置单布局RefreshRecyclerView （若设置，需重写 #setHolder 来填充布局）
     *
     * @param recyclerView
     * @param ItemLayoutId
     */
    protected void setRecyclerViewAdapter(RefreshRecyclerView recyclerView, @LayoutRes int ItemLayoutId) {
        recyclerView.setAdapter(ItemLayoutId, this);
    }

    /**
     * 设置多布局RefreshRecyclerView （若设置，需重写 #setHolder(int ) 来填充布局）
     *
     * @param recyclerView
     * @param ItemLayoutIds
     */
    protected void setRecyclerViewMultiItemAdapter(RefreshRecyclerView recyclerView, @LayoutRes int... ItemLayoutIds) {
        recyclerView.setMultiAdapter(this, ItemLayoutIds);
    }

    /**
     * 布局填充，setRecyclerViewAdapter 需重写
     * (多RecyclerView 布局)
     *
     * @param holder
     * @param item
     */
    @Override
    public void setHolder(int layoutId, BaseViewHolder holder, Object item) {
        setHolder(holder, item);
    }

    /**
     * 布局填充，setRecyclerViewAdapter需重写
     * (单RecyclerView 布局)
     *
     * @param holder
     * @param item
     */
    public void setHolder(BaseViewHolder holder, Object item) {
    }

    /**
     * 布局填充，setRecyclerViewMultiItemAdapter 需重写
     * （多布局 RecyclerView）
     *
     * @param itemType 布局type (对应 layoutIds下标,type不同，holder的布局文件不同)
     * @param item     数据（item.getItemType()对应layoutIds下标）
     */
    @Override
    public void setHolder(BaseViewHolder holder, MultiModel item, int itemType) {

    }

    protected void setFragment() {
        isFragment = true;
        ImmersionBar.with(this).init();
    }

    @Override
    public void setContentView(View view) {
        super.setContentView(view);
        ButterKnife.bind(this, view);
        if (!isFragment && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ImmersionBar.with(this).statusBarColor(BPConfig.APP_THEME_COLOR_VALUE)
                    .fitsSystemWindows(true)
                    .init();
        }
        getSupportActionBar().hide();
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        ButterKnife.bind(this);
        if (!isFragment && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            ImmersionBar.with(this).statusBarColor(BPConfig.APP_THEME_COLOR_VALUE)
                    .fitsSystemWindows(true)
                    .init();
        getSupportActionBar().hide();
        title = findViewById(R.id.tv_title_include_header);
        layout = findViewById(R.id.layout_include_header);
        if (layout != null)
            layout.setBackgroundColor(BPConfig.APP_THEME_COLOR);
        back = findViewById(R.id.img_back_iclude_header);
        if (back != null) {
            back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finishAnim();
                }
            });
        }
        leftTv = findViewById(R.id.tv_left_include_header);
        if (leftTv != null) {
//            leftTv.setText("返回");
            leftTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finishAnim();
                }
            });
        }
        rightTv = findViewById(R.id.tv_right_include_header);
        rightTv2 = findViewById(R.id.tv_right2_include_header);
    }

    @Override
    protected void onResume() {
        broadcastReceiver = new BPBroadcastReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(getApplication().getPackageName());
        localBroadcastManager = LocalBroadcastManager.getInstance(this);
        localBroadcastManager.registerReceiver(broadcastReceiver, intentFilter);
        super.onResume();
    }

    @Override
    protected void onPause() {
        localBroadcastManager.unregisterReceiver(broadcastReceiver);
        super.onPause();
    }

    public void switchLanguage(Locale locale) {
        Resources resources = getResources();// 获得res资源对象
        Configuration config = resources.getConfiguration();// 获得设置对象
        DisplayMetrics dm = resources.getDisplayMetrics();// 获得屏幕参数：主要是分辨率，像素�??
        config.locale = locale; //
        resources.updateConfiguration(config, dm);
    }

    /**
     * 关闭dialog
     */
    public void closeLoadingDialog() {
        if (((++apiCurReturnCount) >= apiALLCount) && loadingDialog != null && loadingDialog.isShowing()) {
            loadingDialog.dismiss();
        }
    }

    /**
     * 弹出信息
     */
    protected void showToast(Object msg) {
        Toast.makeText(this, msg.toString(), Toast.LENGTH_LONG).show();
    }

    protected void showToast(String msg) {
        ToastAlone.showToast(this, msg, Toast.LENGTH_SHORT);
    }

    protected void showToast(@StringRes int resid) {
        ToastAlone.showToast(this, getResources().getString(resid), Toast.LENGTH_SHORT);
    }

    /**
     * 跳转&动画
     */
    public void goActivityForResult(Intent intent, int code) {
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivityForResult(intent, code);
        overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
    }

    /**
     * 跳转&动画
     */
    public void goActivity(Intent intent) {
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
    }

    protected void goActivity(Class<?> act) {
        Intent intent = new Intent(this, act);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
    }

    public void goActivity(Map<String, String> map, Class<?> act) {
        Intent intent = new Intent(this, act);
        for (Entry<String, String> entry : map.entrySet()) {
            intent.putExtra(entry.getKey(), entry.getValue());
        }
        startActivity(intent);
        overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
    }

    protected void finishAnim() {
        finish();
        overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
    }

    protected void onBackKey() {
        if (loadingDialog != null && loadingDialog.isShowing()) {
            loadingDialog.dismiss();
        } else {
            finishAnim();
        }

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            onBackKey();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onError(Exception e, int whichAPI) {
        if (loadingDialog != null && loadingDialog.isShowing())
            loadingDialog.dismiss();
        if (!NetStateCheck.isNetworkConnected(this)) {
            showToast("网络访问超时，请检查网络是否中断!");
        }
        e.printStackTrace();

    }

    /**
     * 提示Dialog
     */
    public void initReturnBack(String content) {
        initReturnBack("提示", content);
    }

    /**
     * 提示Dialog
     */
    public void initReturnBack(String title, String content) {
        initReturnBack(title, content, null);
    }

    /**
     * 提示Dialog
     */
    public void initReturnBack(String title, String content, DialogStringInfo info) {
        initReturnBack(title, content, info, 1);
    }

    /**
     * 提示Dialog
     */
    public void initReturnBack(String title, String content, DialogStringInfo info, int buttonNum) {
        checkDialog();
        DialogStringInfo dialogInfo;
        if (info != null)
            dialogInfo = info;
        else
            dialogInfo = new DialogStringInfo() {

                @Override
                public void LeftBtnClick(View v) {
                    checkDialog();
                }

                @Override
                public void RightBtnClick(View v, String string) {
                    checkDialog();
                }

            };
        dialogInfo.setTitle(title);
        dialogInfo.setContent(content);
        if (buttonNum == 1)
            dialogVersion = CustomDialog.SinglaBtnStringDialog(this, dialogInfo);
        else if (buttonNum == 2)
            dialogVersion = CustomDialog.TwoBtnStringDialog(this, dialogInfo);
        dialogVersion.show();
    }

    public void checkDialog() {
        if (dialogVersion != null && dialogVersion.isShowing()) {
            dialogVersion.dismiss();
        }
    }

    @Override
    public void onCompleteData(Base base, int whichAPI) {

    }


}
