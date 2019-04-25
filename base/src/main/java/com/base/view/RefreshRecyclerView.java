package com.base.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.base.R;
import com.base.adapter.RefreshViewAdapter;
import com.base.adapter.RefreshViewMultiItemAdapter;
import com.base.interfaces.RefreshViewAdapterListener;
import com.base.interfaces.RefreshViewMultiItemAdapterListener;
import com.base.model.MultiModel;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.loadmore.LoadMoreView;

import java.util.List;


/**
 * Created by Administrator on 2017/12/4.
 */

public class RefreshRecyclerView extends LinearLayout {
    private View view;
    private SwipeRefreshLayout reLayout;
    private RecyclerView reView;

    private Context context;
    private FullyGridLayoutManager manager;
    private OnRefreshListener listener;
    private RefreshViewAdapter adapter;
    private RefreshViewMultiItemAdapter multiAdapter;
    private int count = 1;
    private boolean hideRefresh = false;

    public RefreshRecyclerView(Context context) {
        super(context);
        this.context = context;
        view = LayoutInflater.from(context).inflate(R.layout.include_recyclerview, this);
        init();
    }

    public RefreshRecyclerView(Context context, AttributeSet attr) {
        super(context, attr);
        this.context = context;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.include_recyclerview, this);
        TypedArray type = context.obtainStyledAttributes(attr, R.styleable.RecyclerView);
        count = type.getInteger(R.styleable.RecyclerView_counts, 1);
        hideRefresh = type.getBoolean(R.styleable.RecyclerView_hideRefresh, false);
        init();
    }


    public RecyclerView getRecyclerView() {
        return reView;
    }

    public BaseQuickAdapter getAdapter() {
        if (adapter != null)
            return adapter;
        if (multiAdapter != null)
            return multiAdapter;
        return null;
    }

    public void init() {
        reLayout = view.findViewById(R.id.SwipeRefreshLayout_RefreshRecyclerView);
        reView = view.findViewById(R.id.RecyclerView_RefreshRecyclerView);
        if (hideRefresh) hideRefreshView();
        reLayout.setColorSchemeColors(0xffffff00, 0xffff00ff, 0xff00ffff);
        reLayout.setOnRefreshListener(() -> {
            if (listener != null)
                listener.onRefresh();
        });
        manager = new FullyGridLayoutManager(context, count, adapter);
        manager.setOrientation(GridLayoutManager.VERTICAL);
        manager.setSmoothScrollbarEnabled(true);
        reView.setLayoutManager(manager);
    }

    public void setAdapter(int layoutId, RefreshViewAdapterListener listener) {
        adapter = new RefreshViewAdapter(layoutId, listener);
        setAdapter(adapter);
        adapter.setEnableLoadMore(true);
        adapter.setOnLoadMoreListener(() -> {
            if (RefreshRecyclerView.this.listener != null && adapter.isLoadMoreEnable())
                RefreshRecyclerView.this.listener.onLoadMore();
        }, reView);
    }

    /**
     * 必须在#init()后执行
     */
    public void setAdapter(RecyclerView.Adapter adapter) {
        reView.setAdapter(adapter);
    }

    /**
     * 必须在#init()后执行
     */
    public void setMultiAdapter(RefreshViewMultiItemAdapterListener listener, int... layoutIds) {
        multiAdapter = new RefreshViewMultiItemAdapter(listener, layoutIds);
        setAdapter(multiAdapter);
        multiAdapter.setEnableLoadMore(true);
        multiAdapter.setOnLoadMoreListener(() -> {
            if (RefreshRecyclerView.this.listener != null)
                RefreshRecyclerView.this.listener.onLoadMore();
        }, reView);
        multiAdapter.setSpanSizeLookup((gridLayoutManager, position) -> {
            MultiModel model = (MultiModel) getItem(position);
            if (model.getSpanSize() > gridLayoutManager.getSpanCount())
                return gridLayoutManager.getSpanCount();
            return model.getSpanSize();
        });
    }

    public void setOnRefreshListener(OnRefreshListener listener) {
        this.listener = listener;
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


    public void setRefreshing(final boolean refreshing) {
        if (refreshing && listener != null) {
            listener.onRefresh();
        }
        if (adapter != null)
            adapter.setEnableLoadMore(true);
        if (multiAdapter != null)
            multiAdapter.setEnableLoadMore(true);
        reLayout.post(() -> reLayout.setRefreshing(refreshing));
    }

    public void setLoadMoreEnd() {
        if (adapter != null)
            adapter.setEnableLoadMore(false);
        if (multiAdapter != null)
            multiAdapter.setEnableLoadMore(false);
    }

    public void setLoadMoreView(LoadMoreView view) {
        if (adapter != null)
            adapter.setLoadMoreView(view);
        if (multiAdapter != null)
            multiAdapter.setLoadMoreView(view);
    }

    public void setSchemeColor(int... colors) {
        reLayout.setColorSchemeColors(colors);
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
    public void addItemDecoration(ItemDecoration decoration) {
        reView.addItemDecoration(decoration);
    }

    public void setOnItemClickListener(BaseQuickAdapter.OnItemClickListener itemClickListener) {
        if (adapter != null)
            adapter.setOnItemClickListener(itemClickListener);
        if (multiAdapter != null)
            multiAdapter.setOnItemClickListener(itemClickListener);
    }

    public interface OnRefreshListener {
        void onRefresh();

        void onLoadMore();
    }

    public void hideRefreshView() {
        hideRefreshView(true);
    }

    public void hideRefreshView(boolean hide) {
        reLayout.setEnabled(!hide);
    }
}
