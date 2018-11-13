package com.base.interfaces;

/**
 * Created by GaoTing on 2018/9/12.
 *
 * @TODO :二维码扫描接口
 */
public interface OnQRCodeBackCall {
    void Success(String content);

    void Error(String msg);
}
