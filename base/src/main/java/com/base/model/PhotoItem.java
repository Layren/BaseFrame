package com.base.model;

import java.io.Serializable;

public class PhotoItem implements Serializable {
    private String url;
    private boolean isNet;

    public PhotoItem() {
        super();
    }

    public PhotoItem(String url) {
        super();
        this.url = url;
    }

    public PhotoItem(String url, boolean isNet) {
        super();
        this.url = url;
        this.isNet = isNet;
    }


    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public boolean isNet() {
        return isNet;
    }

    public void setNet(boolean net) {
        isNet = net;
    }
}
