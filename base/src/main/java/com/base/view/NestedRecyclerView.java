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
import com.base.adapter.RefreshViewMultiItemAdapter;
import com.base.interfaces.RefreshViewAdapterListener;
import com.base.interfaces.RefreshViewMultiItemAdapterListener;
import com.base.model.MultiModel;
import com.chad.library.adapter.base.BaseQuickAdapter;

import java.util.List;

/**
 * Created by GaoTing on 2018/11/1.
 * <p>
 * Explain :嵌套 RecyclerView
 */
public class NestedRecyclerView extends RecyclerView {
    private Context context;
    private int count = 1;
    private RefreshViewAdapter adapter;
    private RefreshViewMultiItemAdapter multiAdapter;
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

    public void setAdapter(RefreshViewAdapter adapter) {
        this.adapter = adapter;
        super.setAdapter(adapter);
    }

    @Override
    public BaseQuickAdapter getAdapter() {
        if (adapter != null)
            return adapter;
        if (multiAdapter != null)
            return multiAdapter;
        return null;
    }

    public void setAdapter(int layoutId, RefreshViewAdapterListener listener) {
        adapter = new RefreshViewAdapter(layoutId, listener);
        setAdapter(adapter);
    }

    public void setMultiAdapter(RefreshViewMultiItemAdapterListener listener, int... layoutIds) {
        multiAdapter = new RefreshViewMultiItemAdapter(listener, layoutIds);
        setAdapter(multiAdapter);
        multiAdapter.setSpanSizeLookup((gridLayoutManager, position) -> {
            MultiModel model = (MultiModel) getItem(position);
            if (model.getSpanSize() > gridLayoutManager.getSpanCount())
                return gridLayoutManager.getSpanCount();
            return model.getSpanSize();
        });
    }

    public void addHeader(View view) {
        if (adapter != null)
            adapter.addHeaderView(view);
        if (multiAdapter != null)
            multiAdapter.addHeaderView(view);
    }

    public void addFooter(View view) {
        if (adapter != null)
            adapter.addFooterView(view);
        if (multiAdapter != null)
            multiAdapter.addFooterView(view);
    }

    public void notifyDataSetChanged() {
        if (adapter != null)
            adapter.notifyDataSetChanged();
        if (multiAdapter != null)
            multiAdapter.notifyDataSetChanged();
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
        if (multiAdapter != null)
            return multiAdapter.getItem(position);
        return null;
    }

    public Object getLastItem() {
        int last = 0;
        if (adapter != null)
            last = adapter.getItemCount() - 1;
        if (multiAdapter != null)
            last = multiAdapter.getItemCount() - 1;
        if (last >= 0)
            return getItem(last);
        return null;
    }

    public void setMulitData(List<? extends MultiModel> list) {
        multiAdapter.setNewData(list);
    }

    public void addMulitData(List<? extends MultiModel> list) {
        multiAdapter.addData(list);
    }

    public void removeData(int position) {
        if (adapter != null) adapter.remove(position);
        if (multiAdapter != null) multiAdapter.remove(position);
    }

    public void removeData(Object object) {
        if (multiAdapter != null) multiAdapter.getData().remove(object);
        if (adapter != null) adapter.getData().remove(object);
    }

    //添加分割线
    public void addItemDecoration(com.base.view.ItemDecoration decoration) {
        super.addItemDecoration(decoration);
    }

    public void setOnItemClickListener(BaseQuickAdapter.OnItemClickListener itemClickListener) {
        if (adapter != null)
            adapter.setOnItemClickListener(itemClickListener);
        if (multiAdapter != null)
            multiAdapter.setOnItemClickListener(itemClickListener);
    }

}
