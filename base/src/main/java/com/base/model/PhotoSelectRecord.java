package com.base.model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/***
 * 图片选择记录
 */
public class PhotoSelectRecord implements Serializable {
    private List<Object> itemList = new ArrayList<>();
    // 图片路径List；
    private ArrayList<String> urls = new ArrayList<>();
    //图片流List；
    private ArrayList<InputStream> streams = new ArrayList<>();

    public List<Object> getItemList() {
        return itemList;
    }

    public void setItemList(List<Object> itemList) {
        this.itemList.clear();
        this.itemList.addAll(itemList);
    }

    public List<String> getUrls() {
        return urls;
    }

    public void setUrls(Set<String> urls) {
        this.urls.clear();
        this.urls.addAll(urls);
    }

    public List<InputStream> getStreams() {
        return streams;
    }

    public void setStreams(List<InputStream> streams) {
        this.streams.clear();
        this.streams.addAll(streams);
    }

    public int getSize() {
        return urls.size();
    }

    public InputStream get(int index) {
        InputStream stream = streams.get(index);
        if (stream == null) {
            try {
                stream = new FileInputStream(new File(urls.get(index)));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        return stream;
    }

}

