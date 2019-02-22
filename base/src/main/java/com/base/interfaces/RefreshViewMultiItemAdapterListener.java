package com.base.interfaces;

import com.base.model.MultiModel;
import com.chad.library.adapter.base.BaseViewHolder;

/**
 * Created by GaoTing on 2018/9/27.
 *
 * @TODO :多布局RecyclerView 布局监听
 */
public interface RefreshViewMultiItemAdapterListener {
    void setHolder(BaseViewHolder helper, MultiModel item, int itemType);
}
