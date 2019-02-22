package com.base.model;

import com.chad.library.adapter.base.entity.MultiItemEntity;

/**
 * Created by GaoTing on 2018/9/27.
 *
 * @TODO :
 */
public class MultiModel implements MultiItemEntity {
    private int itemType;

    @Override
    public int getItemType() {
        return itemType;
    }

    public void setItemType(int itemType) {
        this.itemType = itemType;
    }
}
