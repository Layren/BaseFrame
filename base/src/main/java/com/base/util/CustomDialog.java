package com.base.util;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.base.config.BPConfig;
import com.base.R;


/**
 * 自定义对话框
 *
 * @author Sun
 */
public class CustomDialog {

    /**
     * 加载中
     */
    public static Dialog LineDialog(final Context context) {
        final Dialog curDialog = new Dialog(context, R.style.dialog);
        curDialog.setContentView(R.layout.dialog_common_loading_indicator);
        curDialog.setCanceledOnTouchOutside(false);
        return curDialog;
    }

    /**
     * 加载中
     */
    public static Dialog LineDialog(final Context context, String content) {
        Dialog dialog = new Dialog(context, R.style.dialog);
        dialog.setContentView(R.layout.dialog_common_loading_indicator);
        TextView textView = (TextView) dialog.findViewById(R.id.loading_info);
        textView.setText(content);
        dialog.setCancelable(true);
        return dialog;
    }

    public static Dialog TwoBtnStringDialog(final Context context, final DialogStringInfo dialogInfo) {
        final Dialog dialog = new Dialog(context, R.style.dialog);
        dialog.setContentView(R.layout.dialog_common_two_btn);
        TextView title = dialog.findViewById(R.id.dialog_two_btn_title);
        TextView content = dialog.findViewById(R.id.dialog_two_tv_content);
        if (null == dialogInfo.getTitle()) {
            title.setVisibility(View.GONE);
        } else {
            title.setText(dialogInfo.getTitle());
        }
        if (null == dialogInfo.getContent()) {
            content.setVisibility(View.GONE);
        } else {
            content.setText(dialogInfo.getContent());
        }
        content.setGravity(dialogInfo.getType());
        Button btnLeft = (Button) dialog.findViewById(R.id.dialog_two_btn_left);
        if (null == dialogInfo.getLeftBtnText()) {
            btnLeft.setText("取消");
        } else {
            btnLeft.setText(dialogInfo.getLeftBtnText());
        }

        btnLeft.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogInfo.LeftBtnClick(v);
            }
        });
        Button btnRight = (Button) dialog.findViewById(R.id.dialog_two_btn_right);
        btnRight.setTextColor(BPConfig.APP_THEME_COLOR);
        if (null == dialogInfo.getRightBtnText()) {
            btnRight.setText("确认");
        } else {
            btnRight.setText(dialogInfo.getRightBtnText());
        }

        btnRight.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogInfo.RightBtnClick(v, "");
            }
        });

        dialog.setCancelable(dialogInfo.isCancelable);

        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        lp.width = BPConfig.SCREEN_WIDTH; // 设置宽度
        dialog.getWindow().setAttributes(lp);

        return dialog;
    }

    public static Dialog SinglaBtnStringDialog(final Context context, final DialogStringInfo dialogInfo) {
        final Dialog dialog = new Dialog(context, R.style.dialog);
        dialog.setContentView(R.layout.dialog_common_two_btn);
        TextView title = dialog.findViewById(R.id.dialog_two_btn_title);
        TextView content = dialog.findViewById(R.id.dialog_two_tv_content);
        if (null == dialogInfo.getTitle()) {
            title.setVisibility(View.GONE);
        } else {
            title.setText(dialogInfo.getTitle());
        }
        if (null == dialogInfo.getContent()) {
            content.setVisibility(View.GONE);
        } else {
            content.setText(dialogInfo.getContent());
        }
        content.setGravity(dialogInfo.getType());

        Button btnLeft = (Button) dialog.findViewById(R.id.dialog_two_btn_left);
        btnLeft.setVisibility(View.GONE);

        View view = (View) dialog.findViewById(R.id.dialog_two_view_middle);
        view.setVisibility(View.GONE);

        Button btnRight = (Button) dialog.findViewById(R.id.dialog_two_btn_right);
        btnRight.setTextColor(BPConfig.APP_THEME_COLOR);
        if (null == dialogInfo.getMiddleText()) {
            btnRight.setText("确认");
        } else {
            btnRight.setText(dialogInfo.getMiddleText());
        }

        btnRight.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogInfo.RightBtnClick(v, "");
            }
        });

        dialog.setCancelable(dialogInfo.isCancelable);

        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        lp.width = BPConfig.SCREEN_WIDTH; // 设置宽度
        dialog.getWindow().setAttributes(lp);

        return dialog;
    }

}
