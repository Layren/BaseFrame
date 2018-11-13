package com.base.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

import com.base.adapter.RefreshViewAdapter;
import com.chad.library.adapter.base.BaseQuickAdapter;


/**
 * Created by Administrator on 2018/4/16.
 */

public class FullyGridLayoutManager extends GridLayoutManager {
    private RefreshViewAdapter adapter;

    public FullyGridLayoutManager(Context context, int spanCount) {
        super(context, spanCount);
    }

    public FullyGridLayoutManager(Context context, int spanCount, RefreshViewAdapter adapter) {
        super(context, spanCount);
        this.adapter = adapter;
    }

    @Override
    public void onMeasure(RecyclerView.Recycler recycler, RecyclerView.State state, int widthSpec, int heightSpec) {
        try {
            if (adapter != null) {
                int measuredWidth = View.MeasureSpec.getSize(widthSpec);
                int line = adapter.getItemCount() / getSpanCount();
                if (adapter.getItemCount() % getSpanCount() > 0) line++;
                int measuredHeight = adapter.getEmptyView().getHeight();
                setMeasuredDimension(measuredWidth, measuredHeight);
            } else {
                super.onMeasure(recycler, state, widthSpec, heightSpec);
            }
        } catch (Exception e) {
            super.onMeasure(recycler, state, widthSpec, heightSpec);
        }

    }

}
