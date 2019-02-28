package com.base.galleryview.photoview.scrollerproxy;

import android.content.Context;

public interface ScrollerProxy {

    static ScrollerProxy getScroller(Context context) {
        return new IcsScroller(context);
    }

    boolean computeScrollOffset();

    void fling(ScrollerParams params);

    void forceFinished(boolean finished);

    boolean isFinished();

    int getCurrX();

    int getCurrY();

}
