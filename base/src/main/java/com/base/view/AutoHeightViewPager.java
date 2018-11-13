package com.base.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

/**
 * 自适应子View高度的viewPager
 */
public class AutoHeightViewPager extends ViewPager {

	private int current;
	private int height = 0;

	private boolean scrollble = true;

	public AutoHeightViewPager(Context context) {
		super(context);
	}

	public AutoHeightViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

		if (getChildCount() > current) {
			View child = getChildAt(current);
			child.measure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
			int h = child.getMeasuredHeight();
			height = h;
		}

		heightMeasureSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY);

		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}

	public int resetHeight(int current) {
		this.current = current;
		if (getChildCount() > current) {
			LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) getLayoutParams();
			if (layoutParams == null) {
				layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, height);
			} else {
				layoutParams.height = height;
			}
			setLayoutParams(layoutParams);
		}
		return height;
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		if (!scrollble) {
			return true;
		}
		return super.onTouchEvent(ev);
	}

	public boolean isScrollble() {
		return scrollble;
	}

	public void setScrollble(boolean scrollble) {
		this.scrollble = scrollble;
	}
}
