package com.base.net;

import android.text.TextUtils;

import com.base.util.Tool;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 在发起网络请求时，用来存放请求参数的容器类
 *
 * @author luopeng (luopeng@staff.sina.com.cn)
 */
public class UrlParameters {
    private String url = "";
    private boolean nullFile;

    private Map<String, String> paramsMap = new HashMap<>();
    private Map<String, List<Object>> fileMap = new HashMap<>();
    private Map<String, FileType> fileTypeMap = new HashMap<>();
    private Map<String, String> headerMap = new HashMap<>();


    public enum FileType {
        FILE, IMAGE, JSON
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
            paramsMap.put(key, value);
        }
    }

    public void add(String key, int value) {
        add(key, String.valueOf(value));
    }

    public void add(String key, boolean value) {
        add(key, String.valueOf(value));
    }

    public void add(String key, long value) {
        add(key, String.valueOf(value));
    }

    public void add(String key, File value) {
        add(key, String.valueOf(value));
    }

    List<String> getKeys() {
        return Tool.getIdFromMap(paramsMap);
    }

    String getValue(String key) {
        return paramsMap.get(key);
    }

    List<String> getFileKeys() {
        return Tool.getIdFromMap(fileMap);
    }

    public void addFile(String key, Object value, FileType fileType) {
        if (value instanceof String && fileType != FileType.JSON) {
            try {
                value = new FileInputStream(new File((String) value));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                return;
            }
        }
        List<Object> list = getFileValues(key);
        if (list == null || list.isEmpty()) {
            list = new ArrayList<>();
            list.add(value);
            fileMap.put(key, list);
            fileTypeMap.put(key, fileType);
        } else {
            list.add(value);
        }
    }

    List<Object> getFileValues(String key) {
        return fileMap.get(key);
    }

    FileType getFileType(String key) {
        return fileTypeMap.get(key);
    }

    public void addHeader(String key, String value) {
        headerMap.put(key, value);
    }

    List<String> getHanderKeys() {
        return Tool.getIdFromMap(headerMap);
    }

    String getHanderValue(String key) {
        return headerMap.get(key);
    }

    public void setNullFile() {
        nullFile = true;
    }

    boolean isNullFile() {
        return nullFile;
    }
}
