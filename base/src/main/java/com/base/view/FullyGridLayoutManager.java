package com.base.view;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.base.adapter.RefreshViewAdapter;
import com.base.adapter.RefreshViewMultiItemAdapter;


/**
 * Created by Administrator on 2018/4/16.
 */

public class FullyGridLayoutManager extends GridLayoutManager {
    private RefreshViewAdapter adapter;
    private RefreshViewMultiItemAdapter multiAdapter;

    public FullyGridLayoutManager(Context context, int spanCount) {
        super(context, spanCount);
    }

    public FullyGridLayoutManager(Context context, int spanCount, RefreshViewAdapter adapter) {
        super(context, spanCount);
        this.adapter = adapter;
    }

    public FullyGridLayoutManager(Context context, int spanCount, RefreshViewMultiItemAdapter multiAdapter) {
        super(context, spanCount);
        this.multiAdapter = multiAdapter;
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

            if (multiAdapter != null) {
                int measuredWidth = View.MeasureSpec.getSize(widthSpec);
                int measuredHeight = multiAdapter.getEmptyView().getHeight();
                setMeasuredDimension(measuredWidth, measuredHeight);
            } else {
                super.onMeasure(recycler, state, widthSpec, heightSpec);
            }
        } catch (Exception e) {
            super.onMeasure(recycler, state, widthSpec, heightSpec);
        }

    }

}
