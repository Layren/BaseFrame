package com.base.baseClass;

import android.app.Dialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.StringRes;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.base.R;
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
import com.base.util.LoginManager;
import com.base.util.ToastAlone;
import com.base.view.RefreshRecyclerView;
import com.chad.library.adapter.base.BaseViewHolder;
import com.gyf.barlibrary.ImmersionBar;

import java.util.Map;
import java.util.Map.Entry;

import butterknife.ButterKnife;


public abstract class BaseActivity extends AppCompatActivity implements SNRequestDataListener, RefreshViewAdapterListener, RefreshViewMultiItemAdapterListener {

    protected int screenWidth;
    protected int screenHeight;
    protected Dialog loadingDialog;
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
    protected boolean isFragment = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadingDialog = CustomDialog.lineDialog(this);
        DisplayMetrics dm = new DisplayMetrics();
        Log.e("curActivity : ", this.getClass().getName());
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        screenWidth = dm.widthPixels;
        screenHeight = dm.heightPixels;
        BPConfig.screenWidth = screenWidth;
        BPConfig.screenHeight = screenHeight;
        BPConfig.density = this.getResources().getDisplayMetrics().density;
        ActivityManager.getAppManager().addActivity(this);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(getLayoutId());
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

    /**
     * 返回状态栏沉浸ViewId 默认为0
     *
     * @return
     */
    protected int getStatusBarView() {
        return 0;
    }

    protected void initView(Bundle savedInstanceState) {
        initView();
    }

    protected void setWhiteHeaderView() {
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
     * @param itemLayoutId
     */
    protected void setRecyclerViewAdapter(RefreshRecyclerView recyclerView, @LayoutRes int itemLayoutId) {
        recyclerView.setAdapter(itemLayoutId, this);
    }

    /**
     * 设置多布局RefreshRecyclerView （若设置，需重写 #setHolder(int ) 来填充布局）
     *
     * @param recyclerView
     * @param itemLayoutIds
     */
    protected void setRecyclerViewMultiItemAdapter(RefreshRecyclerView recyclerView, @LayoutRes int... itemLayoutIds) {
        recyclerView.setMultiAdapter(this, itemLayoutIds);
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

    @Override
    public void setContentView(View view) {
        if (isFragment) {
            ImmersionBar.with(this).init();
        }
        if (getSupportActionBar() != null)
            getSupportActionBar().hide();
        super.setContentView(view);
        if (!isFragment && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ImmersionBar immersionBar = ImmersionBar.with(this);
            if (getStatusBarView() != 0)
                immersionBar.titleBar(getStatusBarView());
            else
                immersionBar.statusBarColor(BPConfig.appThemeColorValue).fitsSystemWindows(true);

            immersionBar.keyboardEnable(true)
                    .init();
        } else {
            ImmersionBar.with(this).init();
        }
        ButterKnife.bind(this, view);
    }

    @Override
    public void setContentView(int layoutResID) {
        if (isFragment) {
            ImmersionBar.with(this).init();
        }
        if (getSupportActionBar() != null)
            getSupportActionBar().hide();
        super.setContentView(layoutResID);
        if (!isFragment && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ImmersionBar immersionBar = ImmersionBar.with(this);
            if (getStatusBarView() != 0)
                immersionBar.titleBar(getStatusBarView());
            else
                immersionBar.statusBarColor(BPConfig.appThemeColorValue).fitsSystemWindows(true);

            immersionBar.keyboardEnable(true)
                    .init();
        }
        ButterKnife.bind(this);
        title = findViewById(R.id.tv_title_include_header_activity);
        layout = findViewById(R.id.layout_include_header_activity);
        if (layout != null)
            layout.setBackgroundColor(BPConfig.appThemeColor);
        back = findViewById(R.id.img_back_include_header_activity);
        if (back != null) {
            back.setOnClickListener(v -> finishAnim());
        }
        leftTv = findViewById(R.id.tv_left_include_header_activity);
        if (leftTv != null) {
            leftTv.setOnClickListener(v -> finishAnim());
        }
        rightTv = findViewById(R.id.tv_right_include_header_activity);
        rightTv2 = findViewById(R.id.tv_right2_include_header_activity);
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
        ActivityManager.getAppManager().finishActivity();
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
                public void leftBtnClick(View v) {
                    checkDialog();
                }

                @Override
                public void rightBtnClick(View v, String string) {
                    checkDialog();
                }

            };
        dialogInfo.setTitle(title);
        dialogInfo.setContent(content);
        if (buttonNum == 1)
            dialogVersion = CustomDialog.singlaBtnStringDialog(this, dialogInfo);
        else if (buttonNum == 2)
            dialogVersion = CustomDialog.twoBtnStringDialog(this, dialogInfo);
        dialogVersion.show();
    }

    public void checkDialog() {
        if (dialogVersion != null && dialogVersion.isShowing()) {
            dialogVersion.dismiss();
        }
    }

    @Override
    public void onCompleteData(Base base, int whichAPI) {
        if (BPConfig.LoginActivity != null
                && base.getCode() != null
                && base.getCode().equals(BPConfig.invalidLogin + "")) {
            checkDialog();
            LoginManager.getInstance().clear();
            goActivity(BPConfig.LoginActivity);
            return;
        }
    }


}
