package com.base.net;

import android.text.TextUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;


/**
 * 在发起网络请求时，用来存放请求参数的容器类
 *
 * @author luopeng (luopeng@staff.sina.com.cn)
 */
public class UrlParameters {
    private ArrayList<String> mKeys = new ArrayList<String>();
    private ArrayList<String> mValues = new ArrayList<String>();
    private ArrayList<String> fileKeys = new ArrayList<String>();
    private ArrayList<List<Object>> fileValues = new ArrayList<>();
    private ArrayList<FileType> fileTypes = new ArrayList<>();
    private ArrayList<String> HeaderKeys = new ArrayList<String>();
    private ArrayList<String> HeaderValues = new ArrayList<String>();
    private String url = "";
    private boolean NullFile;


    public enum FileType {
        FILE, IMAGE, JSON;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public UrlParameters() {
    }

    public UrlParameters(String url) {
        this.url = url;
    }

    public void add(String key, String value) {
        if (!TextUtils.isEmpty(key) && (null != value)) {
            this.mKeys.add(key);
            mValues.add(value);
        }
    }

    public void add(String key, int value) {
        this.mKeys.add(key);
        this.mValues.add(String.valueOf(value));
    }

    public void add(String key, boolean value) {
        this.mKeys.add(key);
        this.mValues.add(value ? "true" : "false");
    }

    public void add(String key, long value) {
        this.mKeys.add(key);
        this.mValues.add(String.valueOf(value));
    }

    public void add(String key, File value) {
        this.mKeys.add(key);
        this.mValues.add(String.valueOf(value));
    }

    ArrayList<String> getmKeys() {
        return mKeys;
    }



    private int getLocation(String key) {
        if (this.mKeys.contains(key)) {
            return this.mKeys.indexOf(key);
        }
        return -1;
    }

    private String getKey(int location) {
        if (location >= 0 && location < this.mKeys.size()) {
            return this.mKeys.get(location);
        }
        return "";
    }


    String getValue(String key) {
        int index = getLocation(key);
        if (index >= 0 && index < this.mKeys.size()) {
            return this.mValues.get(index);
        } else {
            return null;
        }
    }

    private String getValue(int location) {
        if (location >= 0 && location < this.mKeys.size()) {
            String rlt = this.mValues.get(location);
            return rlt;
        } else {
            return null;
        }
    }


    public int size() {
        return mKeys.size();
    }

    public void addAll(UrlParameters parameters) {
        for (int i = 0; i < parameters.size(); i++) {
            this.add(parameters.getKey(i), parameters.getValue(i));
        }

    }

    public void clear() {
        this.mKeys.clear();
        this.mValues.clear();
    }

    ArrayList<String> getFileKeys() {
        return fileKeys;
    }

    public void addFile(String key, Object value, FileType fileType) {
        if (value instanceof String &&
                fileType != FileType.JSON) {
            try {
                value = new FileInputStream(new File((String) value));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                return;
            }
        }
        List<Object> list = getFileValues(key);
        if (list == null) {
            list = new ArrayList<>();
            list.add(value);
            fileKeys.add(key);
            fileValues.add(list);
            fileTypes.add(fileType);
        } else {
            list.add(value);
        }
    }

    List<Object> getFileValues(String key) {
        if (this.fileKeys.contains(key)) {
            int index = this.fileKeys.indexOf(key);
            if (index >= 0 && index < this.fileKeys.size())
                return this.fileValues.get(index);
        }
        return null;
    }

    FileType getFileType(String key) {
        if (this.fileKeys.contains(key)) {
            int index = this.fileKeys.indexOf(key);
            if (index >= 0 && index < this.fileKeys.size())
                return this.fileTypes.get(index);
        }
        return null;
    }

    public void addHeader(String key, String value) {
        HeaderKeys.add(key);
        HeaderValues.add(value);
    }

    ArrayList<String> getHanderKeys() {
        return HeaderKeys;
    }

    String getHanderValue(String key) {
        if (this.HeaderKeys.contains(key)) {
            int index = this.HeaderKeys.indexOf(key);
            if (index >= 0 && index < this.HeaderKeys.size())
                return this.HeaderValues.get(index);
        }
        return "";
    }

    public void setNullFile() {
        NullFile = true;
    }

    boolean isNullFile() {
        return NullFile;
    }
}
