package com.base.adapter;


import android.support.annotation.NonNull;

import com.base.interfaces.RefreshViewAdapterListener;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

/**
 * Created by Administrator on 2017/12/4.
 */

public class RefreshViewAdapter extends BaseQuickAdapter<Object, BaseViewHolder> {
    private RefreshViewAdapterListener listener;
    private int layoutId;

    public RefreshViewAdapter(int layoutResId, @NonNull RefreshViewAdapterListener listener) {
        super(layoutResId);
        this.layoutId = layoutResId;
        this.listener = listener;

    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, Object o) {
        listener.setHolder(layoutId, baseViewHolder, o);
    }


}
