package com.base.baseClass;

import android.app.Dialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.base.config.BPConfig;
import com.base.interfaces.RefreshViewAdapterListener;
import com.base.interfaces.RefreshViewMultiItemAdapterListener;
import com.base.interfaces.SNRequestDataListener;
import com.base.model.Base;
import com.base.model.MultiModel;
import com.base.net.NetStateCheck;
import com.base.util.CustomDialog;
import com.base.util.DialogStringInfo;
import com.base.util.ToastAlone;
import com.base.view.RefreshRecyclerView;
import com.chad.library.adapter.base.BaseViewHolder;
import com.base.R;
import com.gyf.barlibrary.ImmersionBar;
import com.gyf.barlibrary.ImmersionFragment;

import java.util.Map;
import java.util.Map.Entry;

import butterknife.ButterKnife;
import butterknife.Unbinder;

public abstract class BaseFragment extends Fragment implements SNRequestDataListener, RefreshViewAdapterListener, RefreshViewMultiItemAdapterListener {

    protected Dialog loadingDialog;
    protected int screenWidth;
    protected int screenHeight;
    protected int apiALLCount;
    protected int apiCurReturnCount;

    protected Dialog dialogVersion;
    protected View fragmentView;

    private Unbinder unbinder;

    protected ImmersionBar mImmersionBar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadingDialog = CustomDialog.LineDialog(getActivity());
        screenWidth = BPConfig.SCREEN_WIDTH;
        screenHeight = BPConfig.SCREEN_HEIGHT;
        if (getImmersion())
            initImmersionBar();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if ((isVisibleToUser && isResumed())) {
            onResume();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (getUserVisibleHint()) {
            immersionInit();
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        fragmentView = inflater.inflate(getLayoutId(), container, false);
        View v = BindView(fragmentView);
        initView();
        return v;
    }

    public View BindView(View view) {
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden && mImmersionBar != null)
            mImmersionBar.init();
    }

    /**
     * 初始化沉浸式
     */
    protected void initImmersionBar() {
        mImmersionBar = ImmersionBar.with(this);
        mImmersionBar.keyboardEnable(true).navigationBarWithKitkatEnable(false).init();
    }

    protected boolean getImmersion() {
        return false;
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


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (unbinder != null)
            unbinder.unbind();
        if (mImmersionBar != null)
            mImmersionBar.destroy();
    }

    /**
     *
     */
    protected void closeLoadingDialog() {
        if (((++apiCurReturnCount) >= apiALLCount) && loadingDialog != null && loadingDialog.isShowing()) {
            loadingDialog.dismiss();
        }
    }

    public void goActivity(Map<String, String> map, Class<?> act) {
        Intent intent = new Intent(getActivity(), act);
        for (Entry<String, String> entry : map.entrySet()) {
            intent.putExtra(entry.getKey(), entry.getValue());
        }
        startActivity(intent);
        getActivity().overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
    }

    protected void goActivity(Class<?> act2) {
        Intent intent = new Intent(getActivity(), act2);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        getActivity().overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
    }

    /**
     * 跳转&动画
     */
    public void goActivity(Intent intent) {
        startActivity(intent);
        getActivity().overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
    }

    /**
     * 跳转&动画
     */
    public void goActivityForResult(Intent intent, int code) {
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivityForResult(intent, code);
        getActivity().overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
    }

    protected void immersionInit() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            ImmersionBar.with(getActivity())
                    .statusBarDarkFont(true)
                    .navigationBarColor(BPConfig.APP_THEME_COLOR_VALUE)
                    .init();
    }


    protected void showToast(String msg) {
        if (getActivity() != null)
            ToastAlone.showToast(getActivity(), msg, Toast.LENGTH_SHORT);
    }

    protected void showToast(@StringRes int msg) {
        if (getActivity() != null)
            ToastAlone.showToast(getActivity(), msg, Toast.LENGTH_SHORT);
    }

    @Override
    public void onError(Exception e, int whichAPI) {
        if (loadingDialog != null && loadingDialog.isShowing())
            loadingDialog.dismiss();
        if (!NetStateCheck.isNetworkConnected(getActivity())) {
            initReturnBack("网络访问超时，请检查网络是否中断!");
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
            dialogVersion = CustomDialog.SinglaBtnStringDialog(getActivity(), dialogInfo);
        else if (buttonNum == 2)
            dialogVersion = CustomDialog.TwoBtnStringDialog(getActivity(), dialogInfo);
        dialogVersion.show();
    }

    protected void checkDialog() {
        if (dialogVersion != null && dialogVersion.isShowing()) {
            dialogVersion.dismiss();
        }
    }

    @Override
    public void onCompleteData(Base base, int whichAPI) {

    }

}
