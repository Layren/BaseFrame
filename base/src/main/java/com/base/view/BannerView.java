package com.base.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.base.R;
import com.base.adapter.BannerViewAdapter;
import com.base.config.BPConfig;
import com.base.interfaces.BannerItemOnClickListener;
import com.base.model.BannerData;
import com.base.net.MCBaseAPI;
import com.base.util.ActivityManager;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * 轮播图
 */
public class BannerView extends LinearLayout {

    private ViewPager pager;
    private LinearLayout layout;
    private Context context;
    private ImageView[] imgDots;
    private List<BannerData> pagesList = new ArrayList<>();// banner 数据
    private int num = 0;
    private int time = 5000;
    private float proportion = 0.4f;
    private BannerItemOnClickListener listener;
    private int selectId = R.drawable.page_dot_sel;
    private int unSelectId = R.drawable.page_dot_nor;
    private int index = 0;
    private int defaultImage = 0;
    private int multiple;

    private boolean isShowTitle;
    private Handler handler = new Handler(Looper.getMainLooper());
    private Runnable run = new Runnable() {
        @Override
        public void run() {
            pager.setCurrentItem(index++);
        }
    };


    public BannerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initView(attrs);
    }

    public BannerView(Context context, float proportion) {
        this(context, null);
        this.context = context;
        this.proportion = proportion;
    }

    public void initView(AttributeSet attrs) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.view_banner, this);
        pager = findViewById(R.id.vp_banner);
        layout = findViewById(R.id.layout_tab_banner);
        if (attrs != null) {
            TypedArray type = context.obtainStyledAttributes(attrs, R.styleable.RecyclerView);
            proportion = type.getFloat(R.styleable.BannerView_proportion, proportion);
        }
        pager.setLayoutParams(new RelativeLayout.LayoutParams(BPConfig.screenWidth, (int) (BPConfig.screenWidth * proportion)));
    }

    public void setOnItemClickListener(BannerItemOnClickListener listener) {
        this.listener = listener;
    }


    public void setData(List<BannerData> pagesList, boolean isShowTitle) {
        this.pagesList = pagesList;
        this.num = pagesList.size();
        this.isShowTitle = isShowTitle;
        init();
    }

    private void init() {
        if (num > 1) {
            setPage(num);
            new Thread(() -> {
                while (true) {
                    if (ActivityManager.getAppManager().isExist(context.getClass())) {
                        if (!ActivityManager.getAppManager().isExist(context.getClass())) {
                            return;
                        }
                        try {
                            Thread.sleep(time);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        handler.post(run);
                    }
                }
            }).start();
        } else {
            layout.setVisibility(View.GONE);
        }

        ArrayList<View> views = new ArrayList<>();
        if (num == 0) {
            ImageView img = new ImageView(context);
            img.setImageResource(defaultImage);
            img.setLayoutParams(new LayoutParams(BPConfig.screenWidth, (int) (BPConfig.screenWidth * proportion)));
            views.add(img);
        }

        if (pagesList.size() == 2) {
            pagesList.add(pagesList.get(0));
            pagesList.add(pagesList.get(1));
            multiple = 2;
        }
        if (pagesList.size() == 3) {
            pagesList.add(pagesList.get(0));
            pagesList.add(pagesList.get(1));
            pagesList.add(pagesList.get(2));
            multiple = 2;
        }
        int size = pagesList.size();
        for (int i = 0; i < size; i++) {
            BannerData data = pagesList.get(i);
            View itemView = LayoutInflater.from(context).inflate(R.layout.item_banner_view, null);
            itemView.setLayoutParams(new LayoutParams(BPConfig.screenWidth, (int) (BPConfig.screenWidth * proportion)));
            ImageView imgDot = itemView.findViewById(R.id.img_item_banner);
            itemView.setTag(i - (size - size / multiple));
            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onClick((int) v.getTag());
                }
            });
            String smallImg = data.getImageUrl();
            if (null != smallImg && !smallImg.equals("")) {
                if (!smallImg.startsWith("http"))
                    smallImg = MCBaseAPI.API_FILES + smallImg;
                Picasso.get().load(smallImg).centerCrop().resize(BPConfig.screenWidth, (int) (BPConfig.screenWidth * proportion)).into(imgDot);
            } else if (data.getImageId() != 0) {
                Picasso.get().load(data.getImageId()).centerCrop().resize(BPConfig.screenWidth, (int) (BPConfig.screenWidth * proportion)).into(imgDot);
            } else {
                imgDot.setImageResource(defaultImage);
            }
            if (isShowTitle) {
                itemView.findViewById(R.id.layout_title_item_banner).setVisibility(VISIBLE);
                ((TextView) itemView.findViewById(R.id.tv_title_item_banner)).setText(data.getTitle());
                int position = i + size / multiple - size + 1;
                ((TextView) itemView.findViewById(R.id.tv_index_item_banner)).setText(String.format("%s/%s", position, num));
                layout.setVisibility(GONE);
            }
            views.add(itemView);
        }
        pager.setAdapter(new BannerViewAdapter(views));
        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                index = position;
                setCurPage(position % num);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    private void setPage(int pageNumber) {
        layout.removeAllViews();
        imgDots = new ImageView[pageNumber];
        for (int i = 0; i < pageNumber; i++) {
            ImageView imgDot = new ImageView(context);
            imgDot.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
            imgDot.setPadding(5, 0, 5, 0);
            imgDots[i] = imgDot;
            layout.addView(imgDots[i]);
        }
        setCurPage(0);
    }

    private void setCurPage(int index) {
        for (int i = 0; i < num; i++) {
            if (index == i) {
                imgDots[i].setImageResource(selectId);
            } else {
                imgDots[i].setImageResource(unSelectId);
            }
        }
    }


    public void setHintsImage(int selectId, int unSelectId) {
        this.selectId = selectId;
        this.unSelectId = unSelectId;
    }

    public void setDefaultImage(int defaultImage) {
        this.defaultImage = defaultImage;
    }

    public void setTime(int time) {
        this.time = time;
    }
}
