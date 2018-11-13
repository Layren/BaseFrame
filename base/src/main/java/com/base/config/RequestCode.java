package com.base.config;

/**
 * Created by Layren on 2017/8/17.
 * 类：跳转Code
 */

public class RequestCode {
    protected static final int API = 10000;
    protected static final int API_R = 20000;
    /**
     * 添加图片
     */
    public static final int ADD_PHOTO = API + 1001;
    public static final int ADD_PHOTO_RESULT = API_R + 1001;
    /**
     * 二维码扫描
     */
    public static final int OPEN_SCAN = API + 1002;
    public static final int OPEN_SCAN_RESULT = API_R + 1002;
    /**
     * 添加音频
     */
    public static final int ADD_AUDIO = API + 1003;
    public static final int ADD_AUDIO_RESULT = API_R + 1003;
    /**
     * 添加视频
     */
    public static final int ADD_VIDEO = API + 1004;
    public static final int ADD_VIDEO_RESULT = API_R + 1004;
}
