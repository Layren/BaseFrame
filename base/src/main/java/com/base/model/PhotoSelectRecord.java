package com.base.model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

/***
 * 图片选择记录
 */
public class PhotoSelectRecord implements Serializable {

    private List<Object> itemList = new ArrayList<>();// PhotoItem List;
    private ArrayList<String> Urls = new ArrayList<>();// 图片路径List；
    private ArrayList<InputStream> streams = new ArrayList<>();//图片流List；

    public List<Object> getItemList() {
        return itemList;
    }

    public void setItemList(List<Object> itemList) {
        this.itemList.clear();
        this.itemList.addAll(itemList);
    }

    public ArrayList<String> getUrls() {
        return Urls;
    }

    public void setUrls(LinkedHashSet<String> urls) {
        Urls.clear();
        Urls.addAll(urls);
    }

    public ArrayList<InputStream> getStreams() {
        return streams;
    }

    public void setStreams(ArrayList<InputStream> streams) {
        this.streams.clear();
        this.streams.addAll(streams);
    }

    public int getSize() {
        return Urls.size();
    }

    public InputStream get(int index) {
        InputStream stream = streams.get(index);
        if (stream == null) {
            try {
                stream = new FileInputStream(new File(Urls.get(index)));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        return stream;
    }

}

