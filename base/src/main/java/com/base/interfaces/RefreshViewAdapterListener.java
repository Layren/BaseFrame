package com.base.interfaces;

import com.chad.library.adapter.base.BaseViewHolder;

/**
 * Created by GaoTing on 2018/9/27.
 *
 * @TODO : 单布局RecyclerView 监听
 */
public interface RefreshViewAdapterListener {
    void setHolder(int layoutResId, BaseViewHolder holder, Object item);
}
