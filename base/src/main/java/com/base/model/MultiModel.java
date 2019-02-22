package com.base.model;

import com.chad.library.adapter.base.entity.MultiItemEntity;

/**
 * Created by GaoTing on 2018/9/27.
 *
 * @TODO :
 */
public class MultiModel implements MultiItemEntity {
    private int itemType;
    // item宽度.默认为1
    private int spanSize = 1;

    @Override
    public int getItemType() {
        return itemType;
    }

    public void setItemType(int itemType) {
        this.itemType = itemType;
    }

    public int getSpanSize() {
        return spanSize;
    }

    public void setSpanSize(int spanSize) {
        this.spanSize = spanSize;
    }
}
