package com.base.galleryview.photoview.scrollerproxy;

import android.content.Context;
import android.widget.OverScroller;

public class GingerScroller implements ScrollerProxy {

    final OverScroller mScroller;
    private boolean mFirstScroll = false;

    GingerScroller(Context context) {
        mScroller = new OverScroller(context);
    }

    @Override
    public boolean computeScrollOffset() {
        if (mFirstScroll) {
            mScroller.computeScrollOffset();
            mFirstScroll = false;
        }
        return mScroller.computeScrollOffset();
    }

    @Override
    public void fling(ScrollerParams params) {
        mScroller.fling(params.getStartX(), params.getStartY(),
                params.getVelocityX(), params.getVelocityY(),
                params.getMinX(), params.getMaxX(),
                params.getMinY(), params.getMaxY(),
                params.getOverX(), params.getOverY());
    }

    @Override
    public void forceFinished(boolean finished) {
        mScroller.forceFinished(finished);
    }

    @Override
    public boolean isFinished() {
        return mScroller.isFinished();
    }

    @Override
    public int getCurrX() {
        return mScroller.getCurrX();
    }

    @Override
    public int getCurrY() {
        return mScroller.getCurrY();
    }
}