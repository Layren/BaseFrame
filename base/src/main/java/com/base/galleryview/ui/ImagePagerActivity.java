package com.base.galleryview.ui;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.widget.TextView;

import com.base.baseClass.BaseActivity;
import com.base.config.BPConfig;
import com.base.model.PhotoItem;
import com.base.R;
import com.base.net.MCBaseAPI;

import java.util.ArrayList;

/**
 * 图片查看器
 */
public class ImagePagerActivity extends BaseActivity {

    private HackyViewPager mPager;
    private int pagerPosition;
    private TextView indicator;
    private String fromat = "%1$d/%2$d";

    @Override
    protected int getLayoutId() {
        return R.layout.image_detail_pager;
    }

    @Override
    protected void initView() {
        pagerPosition = getIntent().getIntExtra(BPConfig.EXTRA_IMAGE_INDEX, 0);
        ArrayList<PhotoItem> urls = (ArrayList<PhotoItem>) getIntent().getSerializableExtra(BPConfig.EXTRA_IMAGE_URLS);

        mPager = findViewById(R.id.pager);
        ImagePagerAdapter mAdapter = new ImagePagerAdapter(getSupportFragmentManager(), urls);
        mPager.setAdapter(mAdapter);
        indicator = findViewById(R.id.indicator);

        CharSequence text = String.format(fromat, 1, mPager.getAdapter().getCount());
        indicator.setText(text);
        mPager.addOnPageChangeListener(new OnPageChangeListener() {

            @Override
            public void onPageScrollStateChanged(int arg0) {
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            @Override
            public void onPageSelected(int arg0) {
                CharSequence text = String.format(fromat, arg0 + 1, mPager.getAdapter().getCount());
                indicator.setText(text);
            }

        });

    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        if (savedInstanceState != null) {
            pagerPosition = savedInstanceState.getInt(BPConfig.STATE_POSITION);
        }
        mPager.setCurrentItem(pagerPosition);
    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onSaveInstanceState(Bundle outState) {
//        super.onSaveInstanceState(outState);
        outState.putInt(BPConfig.STATE_POSITION, mPager.getCurrentItem());
    }

    private class ImagePagerAdapter extends FragmentStatePagerAdapter {

        public ArrayList<PhotoItem> fileList;

        public ImagePagerAdapter(FragmentManager fm, ArrayList<PhotoItem> fileList) {
            super(fm);
            this.fileList = fileList;
        }

        @Override
        public int getCount() {
            return fileList == null ? 0 : fileList.size();
        }

        @Override
        public Fragment getItem(int position) {
            PhotoItem item = fileList.get(position);
            if (item.isNet())
                if (item.getUrl().startsWith("http"))
                    return ImageDetailFragment.newInstance(item.getUrl(), BPConfig.HTTP_URL);
                else
                    return ImageDetailFragment.newInstance(MCBaseAPI.API_FILES + item.getUrl(), BPConfig.HTTP_URL);
            else
                return ImageDetailFragment.newInstance(item.getUrl(), BPConfig.LOCAL_FILE);
        }

    }
}
