package com.base.util;

import android.view.Gravity;
import android.view.View;

public abstract class DialogStringInfo {

    private String title;
    private String content;
    private String leftBtnText;
    private String rightBtnText;
    private String middleText;
    private int type = Gravity.CENTER;

    public boolean isCancelable = false;

    public static final int TYPE_LEFT = Gravity.LEFT;
    public static final int TYPE_RIGHT = Gravity.RIGHT;

    public abstract void LeftBtnClick(View v);

    public abstract void RightBtnClick(View v, String string);

    public DialogStringInfo() {

    }

    public DialogStringInfo(boolean isCancelable) {
        this.isCancelable = isCancelable;
    }

    public DialogStringInfo(String title, String content, String leftBtnText, String rightBtnText) {
        this.title = title;
        this.content = content;
        this.leftBtnText = leftBtnText;
        this.rightBtnText = rightBtnText;
    }

    public DialogStringInfo(String title, String content, String middleText) {
        super();
        this.title = title;
        this.content = content;
        this.middleText = middleText;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getLeftBtnText() {
        return leftBtnText;
    }

    public void setLeftBtnText(String leftBtnText) {
        this.leftBtnText = leftBtnText;
    }

    public String getRightBtnText() {
        return rightBtnText;
    }

    public void setRightBtnText(String rightBtnText) {
        this.rightBtnText = rightBtnText;
    }

    public String getMiddleText() {
        return middleText;
    }

    public void setMiddleText(String middleText) {
        this.middleText = middleText;
    }

    public int getType() {
        return type;
    }

    /**
     * 设置信息显示方向（靠左，靠右）(默认居中)
     *
     * @param type TYPE_LEFT or TYPE_RIGHT
     */
    public void setType(int type) {
        this.type = type;
    }

    private void setCancelable(boolean isCancelable) {
        this.isCancelable = isCancelable;
    }

}
