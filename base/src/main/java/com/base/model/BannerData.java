package com.base.model;

import java.io.Serializable;

/**
 * Created by Administrator on 2018/3/31.
 */

public class BannerData implements Serializable {
    private String imageUrl;
    private String title;
    private int imageId;

    public BannerData(String imageUrl, String title) {
        this.imageUrl = imageUrl;
        this.title = title;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }
}
