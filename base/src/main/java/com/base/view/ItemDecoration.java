package com.base.view;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;


/**
 * Created by GaoTing on 2018/1/2.
 *
 * @TODO :分割线
 */

public class ItemDecoration extends RecyclerView.ItemDecoration {
    private int dividerHeight;
    private int orientation;
    private Paint dividerPaint;
    private boolean isFrame = true;

    public ItemDecoration(int height, int orientation, int color) {
        dividerHeight = height;
        this.orientation = orientation;
        dividerPaint = new Paint();
        dividerPaint.setColor(color);
    }

    public ItemDecoration(int height, int color) {
        dividerHeight = height;
        orientation = Gravity.BOTTOM;
        dividerPaint = new Paint();
        dividerPaint.setColor(color);
    }

    public ItemDecoration(int height, int color, boolean isFrame) {
        dividerHeight = height;
        orientation = Gravity.BOTTOM;
        dividerPaint = new Paint();
        dividerPaint.setColor(color);
        this.isFrame = isFrame;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        GridLayoutManager layoutManager = (GridLayoutManager) parent.getLayoutManager();
        int spanCount = layoutManager.getSpanCount();//列数
        if (spanCount == 1) {
            switch (orientation) {
                case Gravity.TOP:
                    outRect.top = dividerHeight;
                    break;
                case Gravity.LEFT:
                    outRect.left = dividerHeight;
                    break;
                case Gravity.RIGHT:
                    outRect.right = dividerHeight;
                    break;
                case Gravity.BOTTOM:
                    outRect.bottom = dividerHeight;
                    break;
                case Gravity.CENTER:
                    outRect.left = dividerHeight;
                    outRect.top = dividerHeight;
                    outRect.right = dividerHeight;
                    outRect.bottom = dividerHeight;
                    break;
            }
        } else {
            final int position = parent.getChildAdapterPosition(view);
            final int count = parent.getAdapter().getItemCount();
            if (isFrame) {
                if ((position % spanCount) == 0) {
                    //最左边
                    outRect.left = dividerHeight;
                }
                if (position < spanCount) {
                    // 第一行
                    outRect.top = dividerHeight;
                }
                outRect.right = dividerHeight;
                outRect.bottom = dividerHeight;
            } else {
                if ((position == count - 1) && (position % spanCount) == (spanCount - 1)) {
                    // 最后一个，如果也是最右边，那么就不需要偏移
                } else if (position >= (count - (count % spanCount))) {
                    // 最下面一行，只要右边偏移就行
                    outRect.right = dividerHeight;
                } else if ((position % spanCount) == (spanCount - 1)) {
                    // 最右边一列，只要下面偏移就行
                    outRect.bottom = dividerHeight;
                } else {
                    // 其他的话，右边和下面都要偏移
                    outRect.set(0, 0, dividerHeight, dividerHeight);
                }
            }
        }
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);
        GridLayoutManager layoutManager = (GridLayoutManager) parent.getLayoutManager();
        int spanCount = layoutManager.getSpanCount();//列数
        int childCount = parent.getChildCount();
        if (childCount == 0) {
            return;
        }
        if (spanCount == 1) {
            for (int i = 0; i < childCount - 1; i++) {
                View view = parent.getChildAt(i);
                float top = view.getBottom();
                float bottom = view.getBottom() + dividerHeight;
                c.drawRect(0, top, parent.getRight(), bottom, dividerPaint);
            }
        } else {
            for (int i = 0; i < childCount; i++) {
                View view = parent.getChildAt(i);
                final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) view.getLayoutParams();
                //将带有颜色的分割线处于中间位置
                final float centerLeft = (layoutManager.getLeftDecorationWidth(view)) / 2;
                final float centerTop = (layoutManager.getTopDecorationHeight(view)) / 2;

                float top = view.getTop();
                float bottom = view.getBottom();
                float left = view.getLeft();
                float right = view.getRight();
                int laseCount = childCount % spanCount;
                if (isFrame) {
                    if (i < spanCount) {
                        //第一行
                        c.drawRect(left, top - dividerHeight, right, top, dividerPaint);
                    }
                    if (i % spanCount == 0) {
                        //第一列
                        c.drawRect(left - dividerHeight, top, left, bottom, dividerPaint);
                        //左上角
                        c.drawRect(left - dividerHeight, top - dividerHeight, left, top, dividerPaint);
                    }
                    //下边线
                    c.drawRect(left, bottom, right, bottom + dividerHeight, dividerPaint);
                    //右边线
                    c.drawRect(right, top, right + dividerHeight, bottom, dividerPaint);
                    //右下角
                    c.drawRect(right, bottom, right + dividerHeight, bottom + dividerHeight, dividerPaint);
                } else {
                    if (childCount - i > (laseCount == 0 ? spanCount : laseCount)) {
                        //下边线
                        c.drawRect(left, bottom, right, bottom + dividerHeight, dividerPaint);
                    }
                    if ((i + 1) % spanCount != 0) {
                        //右边线
                        c.drawRect(right, top, right + dividerHeight, bottom, dividerPaint);
                        //右下角
                        c.drawRect(right, bottom, right + dividerHeight, bottom + dividerHeight, dividerPaint);
                    }
                }
            }
        }

    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDrawOver(c, parent, state);
    }


}
