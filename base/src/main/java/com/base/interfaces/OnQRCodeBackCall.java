package com.base.interfaces;

/**
 * Created by GaoTing on 2018/9/12.
 *
 * Explain :二维码扫描接口
 */
public interface OnQRCodeBackCall {
    void success(String content);

    void error(String msg);
}
