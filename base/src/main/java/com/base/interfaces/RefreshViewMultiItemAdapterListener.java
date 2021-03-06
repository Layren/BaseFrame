package com.base.interfaces;

import com.base.model.MultiModel;
import com.chad.library.adapter.base.BaseViewHolder;

/**
 * Created by GaoTing on 2018/9/27.
 *
 * Explain:多布局RecyclerView 布局监听
 */
public interface RefreshViewMultiItemAdapterListener {
    void setHolder(BaseViewHolder holder, MultiModel item, int itemType);
}
