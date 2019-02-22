package com.base.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

import com.base.R;
import com.base.adapter.RefreshViewAdapter;
import com.base.interfaces.RefreshViewAdapterListener;
import com.chad.library.adapter.base.BaseQuickAdapter;

import java.util.List;

/**
 * Created by GaoTing on 2018/11/1.
 *
 * @TODO :嵌套 RecyclerView
 */
public class NestedRecyclerView extends RecyclerView {
    private Context context;
    private int count = 1;
    private RefreshViewAdapter adapter;
    private FullyGridLayoutManager manager;

    public NestedRecyclerView(Context context) {
        super(context);
        this.context = context;
        init();
    }

    public NestedRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        TypedArray type = context.obtainStyledAttributes(attrs, R.styleable.RecyclerView);
        count = type.getInteger(R.styleable.RecyclerView_counts, 1);
        init();
    }

    private void init() {
        manager = new FullyGridLayoutManager(context, count);
        manager.setOrientation(GridLayoutManager.VERTICAL);
        manager.setSmoothScrollbarEnabled(true);
        setLayoutManager(manager);
        setNestedScrollingEnabled(false);
    }

        public void setAdapter(int layoutId, RefreshViewAdapterListener listener) {
        adapter = new RefreshViewAdapter(layoutId, listener);
        setAdapter(adapter);
    }

    public void addHeader(View view) {
        if (adapter != null)
            adapter.addHeaderView(view);
    }

    public void addFooter(View view) {
        if (adapter != null)
            adapter.addFooterView(view);
    }

    public void notifyDataSetChanged() {
        adapter.notifyDataSetChanged();
    }

    public void setData(List<Object> list) {
        adapter.setNewData(list);
    }

    public void addData(List<Object> list) {
        adapter.addData(list);
    }

    public Object getItem(int position) {
        if (adapter != null)
            return adapter.getItem(position);
        return null;
    }

    public Object getLastItem() {
        int last = 0;
        if (adapter != null)
            last = adapter.getItemCount() - 1;
        if (last >= 0)
            return getItem(last);
        return null;
    }

    public void removeData(int position) {
        if (adapter != null) adapter.remove(position);
    }

    public void removeData(Object object) {
        for (int i = 0; i < adapter.getData().size(); i++) {
            if (object.equals(adapter.getData().get(i))) {
                removeData(i);
            }
        }
    }

        public void setOnItemClickListener(BaseQuickAdapter.OnItemClickListener itemClickListener) {
        if (adapter != null)
            adapter.setOnItemClickListener(itemClickListener);
    }

}
