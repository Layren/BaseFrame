package com.base.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.base.config.BPConfig;
import com.base.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 横向ListView
 *
 * @author 高厅
 * @date 2016年3月16日
 * @time 上午11:14:14
 */
public class HorizontalListView extends LinearLayout {
    private View view;
    private btnListener listener;
    private List<TextView> textViews = new ArrayList<TextView>();
    private Context myContext;
    private LinearLayout layout;
    private int textColor = BPConfig.APP_THEME_COLOR;
    private int selectIndex = 0;
    private float textsize = -1;
    private int screenWidth;
    private HorizontalScrollView hsView;
    private View parentView;
    private int lrInterval;
    private int tbInterval;

    public HorizontalListView(Context context) {
        super(context);
        myContext = context;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.view_orizontal_listview, this);
        layout = view.findViewById(R.id.layout_view_select_btn);
        hsView = view.findViewById(R.id.my_HorizontalScrollView);
        parentView = getRootView();
    }

    public HorizontalListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        myContext = context;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.view_orizontal_listview, this);
        layout = view.findViewById(R.id.layout_view_select_btn);
        hsView = view.findViewById(R.id.my_HorizontalScrollView);
        parentView = getRootView();
    }

    /**
     * 设置字体Size；
     */
    public void setSize(int size) {
        textsize = size;
    }

    private TextView getTextView(String title, final int position) {
        TextView titleView = new TextView(myContext);
        titleView.setGravity(Gravity.CENTER);
        if (textsize > 0)
            titleView.setTextSize(textsize);
        if (position == 0) {
            titleView.setTextColor(textColor);
        } else {
            titleView.setTextColor(getResources().getColor(R.color.text));
        }
        titleView.setText(title);
        LayoutParams params = new
                LayoutParams(LayoutParams.WRAP_CONTENT,
                LayoutParams.MATCH_PARENT);
        params.setMargins(lrInterval, 0, lrInterval, 0);
        titleView.setLayoutParams(params);

        titleView.setPadding(0, tbInterval, 0, tbInterval);
        titleView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                setOnclick(position, true);
            }

        });
        return titleView;
    }

    public void setDate(List<String> list, btnListener listener) {
        this.listener = listener;
        layout.removeAllViews();
        textViews.clear();
        for (int i = 0; i < list.size(); i++) {
            TextView _tv = getTextView(list.get(i), i);
            layout.addView(_tv);
            textViews.add(_tv);
        }
    }

    /**
     * 点击或选中事件处理（选中 是ViewPager滑动选中）
     *
     * @param isOnclick 是否为点击
     */
    public void setOnclick(int index, boolean isOnclick) {
        if (index == selectIndex)
            return;
        if (screenWidth > 0) {
            viewScroll(index);
        } else if (parentView != null) {
            screenWidth = parentView.getMeasuredWidth();
        }
        if (isOnclick)
            listener.btnOnclick(index);
        selectIndex = index;
        layout.removeAllViews();
        for (int i = 0; i < textViews.size(); i++) {
            TextView _view = textViews.get(i);
            if (i == index) {
                _view.setTextColor(textColor);
                _view.setTextSize(16);
            } else {
                _view.setTextColor(0xff333333);
                _view.setTextSize(14);
            }
            layout.addView(_view);
        }
    }

    public void setTextColor(int color) {
        this.textColor = color;
    }

    public interface btnListener {
        void btnOnclick(int no);
    }

    private int viewScroll(int mark) {
        float viewWidth1 = 0;
        float viewWidth2 = 0;
        for (int i = 0; i < mark + 1; i++) {
            viewWidth1 += textViews.get(i).getWidth() + 2 * lrInterval;
            if (i < mark) {
                viewWidth2 += textViews.get(i).getWidth() + 2 * lrInterval;
            }
        }
        if (viewWidth1 > screenWidth + hsView.getScrollX()) {
            hsView.scrollTo((int) (viewWidth1 - screenWidth), 0);
        } else if (viewWidth2 < hsView.getScrollX()) {
            hsView.scrollTo((int) (viewWidth2), 0);
        }
        return mark;
    }

    public void setInterval(int lrInterval, int tbInterval) {
        this.lrInterval = lrInterval;
        this.tbInterval = tbInterval;
    }
}
