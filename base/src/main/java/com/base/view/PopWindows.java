package com.base.view;

import android.annotation.SuppressLint;
import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.graphics.Rect;
import android.os.Build;
import android.support.annotation.LayoutRes;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.PopupWindow;

import com.base.R;

public class PopWindows {

    private PopupWindow popupWindow;
    private Activity activity;
    private View popupWindow_view;
    private int layoutId;

    /**
     * @param activity
     * @param layoutId 自定义popwindow布局ID
     */
    public PopWindows(Activity activity, @LayoutRes int layoutId) {
        this.activity = activity;
        this.layoutId = layoutId;
    }

    public PopWindows initPopWindows(PopWindowsViewOnCallk popWindowsViewOnCallk) {
        popupWindow_view = activity.getLayoutInflater().inflate(layoutId, null);
        popupWindow = new PopupWindow(popupWindow_view, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, true);
        popupWindow.setBackgroundDrawable(activity.getResources().getDrawable(R.drawable.touming));
        popupWindow_view.setOnTouchListener(new OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            public boolean onTouch(View v, MotionEvent event) {
                if (popupWindow != null && popupWindow.isShowing()) {
                    popupWindow.dismiss();
                }
                return false;
            }
        });
        popWindowsViewOnCallk.initView(popupWindow_view);
        return this;
    }


    /***
     * @param gravity
     *            { Gravity.LEFT or Gravity.TOP or Gravity.RIGHT
     *            orGravity.BOTTOM}
     */
    public void show(int gravity) {
        switch (gravity) {
            case Gravity.LEFT:
                popupWindow.setAnimationStyle(R.style.AnimationFadeLeft);
                break;
            case Gravity.TOP:
                popupWindow.setAnimationStyle(R.style.AnimationFadeTop);
                break;
            case Gravity.RIGHT:
                popupWindow.setAnimationStyle(R.style.AnimationFadeRight);
                break;
            case Gravity.BOTTOM:
                popupWindow.setAnimationStyle(R.style.AnimationFadeBottom);
                break;

        }
        popupWindow.showAtLocation(activity.getWindow().getDecorView(), gravity, 0, 0);
    }
    public void showView(View anchorView){
        popupWindow.setOutsideTouchable(false);
        popupWindow.setFocusable(false);
        if (Build.VERSION.SDK_INT >= 24) {
            Rect visibleFrame = new Rect();
            anchorView.getGlobalVisibleRect(visibleFrame);
            int height = anchorView.getResources().getDisplayMetrics().heightPixels - visibleFrame.bottom;
            popupWindow.setHeight(height);
            popupWindow.showAsDropDown(anchorView, 0, 0);
        } else {
            popupWindow.showAsDropDown(anchorView, 0, 0);
        }
    }
    public void close() {
        if (popupWindow != null && popupWindow.isShowing()) {
            popupWindow.dismiss();
        }
    }

    public interface PopWindowsViewOnCallk {
        void initView(View view);
    }
}
