package com.base.util;

import android.content.Context;
import android.widget.Toast;

/**
 * 无重复Toast
 */
public class ToastAlone {

    private static Toast mToast = null;

    public static void showToast(Context context, String text, int duration) {
        if (mToast == null) {
            mToast = Toast.makeText(context, text, duration);
        } else {
            mToast.setText(text);
            mToast.setDuration(duration);
        }

        mToast.show();
    }

    public static void showToast(Context context, int textId, int duration) {
        if (mToast == null) {
            mToast = Toast.makeText(context, context.getResources().getString(textId), duration);
        } else {
            mToast.setText(context.getResources().getString(textId));
            mToast.setDuration(duration);
        }

        mToast.show();
    }

}
