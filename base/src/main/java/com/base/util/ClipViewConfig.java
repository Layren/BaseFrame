package com.base.util;

import java.io.Serializable;

/**
 * Created by GaoTing on 2019/4/22.
 * <p>
 * Explain:图片裁剪配置
 */
public class ClipViewConfig implements Serializable {
    private int type;//裁剪类型
    private int padding;//裁剪区域边距
    private float ratio;// 宽高比(type为 矩形的时候使用)

    public ClipViewConfig(int type, int padding, float ratio) {
        this.type = type;
        this.padding = padding;
        this.ratio = ratio;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getPadding() {
        return padding;
    }

    public void setPadding(int padding) {
        this.padding = padding;
    }

    public float getRatio() {
        return ratio;
    }

    public void setRatio(float ratio) {
        this.ratio = ratio;
    }
}
