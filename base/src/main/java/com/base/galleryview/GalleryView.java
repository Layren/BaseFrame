package com.base.galleryview;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.base.config.BPConfig;
import com.base.galleryview.ui.ImagePagerActivity;
import com.base.model.PhotoItem;

import java.util.ArrayList;
import java.util.List;

public class GalleryView {

    private Context context;

    public GalleryView(Context context) {
        this.context = context;
    }

    /**
     * 图片点击查看
     *
     * @param list  图片list<PhotoItem>
     * @param index 点击图片 position
     */
    public void onClickImage(ArrayList<PhotoItem> list, int index) {
        Intent intent = new Intent(context, ImagePagerActivity.class);
        intent.putExtra(BPConfig.EXTRA_IMAGE_URLS, list);
        intent.putExtra(BPConfig.EXTRA_IMAGE_INDEX, index);
        context.startActivity(intent);
    }

    /**
     * 图片点击查看
     *
     * @param list  图片路径集合
     * @param index 点击图片 position
     */
    public void onClickImage(List<String> list, int index, boolean isNet) {
        ArrayList<PhotoItem> items = new ArrayList<>();
        for (String path : list) {
            items.add(new PhotoItem(path, isNet));
        }
        Intent intent = new Intent(context, ImagePagerActivity.class);
        intent.putExtra(BPConfig.EXTRA_IMAGE_URLS, items);
        intent.putExtra(BPConfig.EXTRA_IMAGE_INDEX, index);
        context.startActivity(intent);
    }
}
