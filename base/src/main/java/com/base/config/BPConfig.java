package com.base.config;

import android.graphics.Color;
import android.os.Environment;

import com.base.BPApplication;

public class BPConfig {
    public static final String CACHE_PATH;
    public static final String CACHE_IMG_PATH;
    public static final String CACHE_FILE_PATH;
    public static final String CACHE_LOG_PATH;
    public static final String CACHE_APKDOWN_PATH;
    public static int APP_THEME_COLOR = Color.WHITE;
    public static String APP_THEME_COLOR_VALUE = "0xffffffff";

    public static int SCREEN_WIDTH;// px
    public static int SCREEN_HEIGHT;// px
    public static float DENSITY;
    //默认7天有效期
    public static long EFFECTIVE_TIME = 7 * 86400000l;

    public static boolean IS_WHITE_HEADER = false;

    public static String APPLICATION_ID;

    public static final int PIC_SELECT = 0x1101;
    public static final int PHOTO_ALBUM = 0x1102;
    public static final int BACK_CODE = 0X1104;
    public static final int PHONTO_GRAPH = 0X1105;

    public static final String STATE_POSITION = "STATE_POSITION";
    public static final String EXTRA_IMAGE_INDEX = "image_index";
    public static final String EXTRA_IMAGE_URLS = "image_urls";
    public static final String EXTRA_IMAGE_PATHTYPE = "path_type";
    public static final String HTTP_URL = "path_url";
    public static final String LOCAL_FILE = "path_file";
    public static String CAMERA_IMG_PATH = Environment.getExternalStorageDirectory().getPath() + "/DCIM/Camera/";


    static {
        CACHE_PATH = BPApplication.getCACHE_PATH();
        CACHE_IMG_PATH = CACHE_PATH + "/img";
        CACHE_FILE_PATH = CACHE_PATH + "/file";
        CACHE_APKDOWN_PATH = CACHE_PATH + "/apk";
        CACHE_LOG_PATH = CACHE_PATH + "/log";
    }

}
