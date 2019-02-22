package com.base.adapter;

import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;

import com.base.interfaces.RefreshViewMultiItemAdapterListener;
import com.base.model.MultiModel;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.ArrayList;

/**
 * Created by GaoTing on 2018/9/27.
 *
 * @TODO :RecyclerView多布局Adapter
 */
public class RefreshViewMultiItemAdapter<T extends MultiModel> extends BaseMultiItemQuickAdapter<T, BaseViewHolder> {
    private RefreshViewMultiItemAdapterListener listener;

    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     */
    public RefreshViewMultiItemAdapter(@NonNull RefreshViewMultiItemAdapterListener listener, @NonNull @LayoutRes int... layoutIds) {
        super(new ArrayList());
        this.listener = listener;
        setViewType(layoutIds);
    }

    /**
     * 添加多布局，itemType对应layoutIds下标
     *
     * @param layoutIds
     */
    private void setViewType(@LayoutRes int... layoutIds) {
        for (int i = 0; i < layoutIds.length; i++) {
            addItemType(i, layoutIds[i]);
        }
    }

    @Override
    protected void convert(BaseViewHolder helper, MultiModel item) {
        if (listener != null) {
            listener.setHolder(helper, item, helper.getItemViewType());
        }
    }

}
