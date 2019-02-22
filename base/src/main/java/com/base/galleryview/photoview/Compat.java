package com.base.galleryview.photoview;

import android.annotation.TargetApi;
import android.os.Build.VERSION_CODES;
import android.view.MotionEvent;
import android.view.View;

public class Compat {

    public static void postOnAnimation(View view, Runnable runnable) {
        postOnAnimationJellyBean(view, runnable);
    }

    @TargetApi(16)
    private static void postOnAnimationJellyBean(View view, Runnable runnable) {
        view.postOnAnimation(runnable);
    }

    public static int getPointerIndex(int action) {
        return getPointerIndexHoneyComb(action);
    }

    @TargetApi(VERSION_CODES.HONEYCOMB)
    private static int getPointerIndexHoneyComb(int action) {
        return (action & MotionEvent.ACTION_POINTER_INDEX_MASK) >> MotionEvent.ACTION_POINTER_INDEX_SHIFT;
    }

}
