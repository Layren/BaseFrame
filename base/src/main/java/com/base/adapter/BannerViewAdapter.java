package com.base.adapter;

import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

public class BannerViewAdapter extends PagerAdapter {
    private ArrayList<View> mListViews;
    private int count = Integer.MAX_VALUE;

    public BannerViewAdapter(ArrayList<View> mListViews) {
        this.mListViews = mListViews;
    }

    @Override
    public int getCount() {
        return count;
    }

    public Object instantiateItem(ViewGroup arg0, int arg1) {
        int size = mListViews.size();
        int location = arg1 % size;
        View view = mListViews.get(location);
        ViewGroup p = (ViewGroup) view.getParent();
        if (p != null) {
            p.removeView(view);
        }
        arg0.addView(mListViews.get(location), 0);
        return mListViews.get(location);

    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
//        super.destroyItem(container, position, object);
        container.removeView((View) object);
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {

        return arg0 == (arg1);
    }

    @Override
    public void restoreState(Parcelable arg0, ClassLoader arg1) {
    }

    @Override
    public Parcelable saveState() {
        return null;
    }


}
