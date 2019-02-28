package com.base;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;

import com.base.config.BPConfig;
import com.base.db.BaseDBManager;
import com.base.util.LoginManager;
import com.base.util.ActivityManager;
import com.base.util.CrashHandler;
import com.squareup.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 先设置 CACHE_PATH 然后实现init
 */
public class BPApplication extends Application {
    private static String cachePath;// 缓存文件夹
    private static String netPath;// 网络接口
    private static String netFilePath;// 网络文件接口
    private static String dbName = "-1";//数据库文件名
    private static int dbId = -1; // 数据库文件名
    private int mFinalCount;
    private Timer timer;
    private int time;
    private static String packgeName = "";

    @Override
    public void onCreate() {
        super.onCreate();
        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
            }

            @Override
            public void onActivityStarted(Activity activity) {
                mFinalCount++;
                if (mFinalCount == 1) {
                    time = 0;
                    if (timer != null)
                        timer.cancel();
                }
            }

            @Override
            public void onActivityResumed(Activity activity) {

            }

            @Override
            public void onActivityPaused(Activity activity) {

            }

            @Override
            public void onActivityStopped(Activity activity) {
                mFinalCount--;
                if (mFinalCount == 0) {
                    timer = new Timer();
                    timer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            time++;
                            if (time > 600) {
                                new Handler(Looper.getMainLooper()).post(() -> ActivityManager.getAppManager().appExit());
                            }
                        }
                    }, 1, 1000);
                }
            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

            }

            @Override
            public void onActivityDestroyed(Activity activity) {

            }
        });
    }

    public void init() {
        packgeName = getPackageName();
        initFilePath();
        initPicasso();
        initCrash();
        initDb();
        LoginManager.setFileName(packgeName);
        BPConfig.appplcaitonId = packgeName;
    }

    protected void initPath(String... paths) {
        for (String path : paths) {
            File fileDir = new File(path);
            if (!fileDir.exists()) {
                fileDir.mkdirs();
            }
        }
    }

    private void initDb() {
        BaseDBManager.getInstance(this).setDbName(dbName, dbId);
    }

    protected void initCrash() {
        CrashHandler.getInstance(BPConfig.CACHE_LOG_PATH).init(this);
    }

    /**
     * 设置异常信息文件的有效时间（单位为天）
     */
    protected void setCrashEffectiveTime(int day) {
        if (day > 0)
            BPConfig.effectiveTime = day * 86400000l;
    }

    /**
     * 删除 过期的异常信息文件
     */
    @SuppressLint("SimpleDateFormat")
    protected void deleteUselessFile(File root) {
        if (BPConfig.effectiveTime < 0)
            return;
        File[] files = root.listFiles();
        long curTime = System.currentTimeMillis();
        if (files != null) {
            for (File f : files) {
                if (f.exists()) { // 判断是否存在
                    String name = f.getName();
                    String fileTime = name.substring(4, name.indexOf('.'));
                    DateFormat formatter = new SimpleDateFormat("yyyyMMdd_HHmmss");
                    try {
                        Date date = formatter.parse(fileTime);
                        long createTime = date.getTime();
                        // 判断是否过期（一个月的有效期）
                        if (curTime - createTime > BPConfig.effectiveTime) {
                            f.delete();
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    protected void initFilePath() {
        initPath(BPConfig.CACHE_PATH, BPConfig.CACHE_IMG_PATH, BPConfig.CACHE_FILE_PATH, BPConfig.CACHE_LOG_PATH, BPConfig.CACHE_APKDOWN_PATH);
        deleteUselessFile(new File(BPConfig.CACHE_LOG_PATH));
    }

    /**
     * 文件缓存地址
     */
    public static String getCachePath() {
        if (TextUtils.isEmpty(cachePath)) {
            return Environment.getExternalStorageDirectory().getPath() + packgeName;
        } else {
            return cachePath;
        }
    }

    /**
     * 设置文件缓存地址
     */
    protected static void setCachePath(String cachePaths) {
        cachePath = Environment.getExternalStorageDirectory().getPath() + "/" + cachePaths;
    }

    /**
     * 获取网络地址
     */
    public static String getNetPath() {
        return netPath;
    }

    /**
     * 设置网络地址
     */
    protected static void setNetPath(String netPaths) {
        netPath = netPaths;
    }

    /**
     * 设置网络文件地址
     */
    protected static void setNetFilePath(String netFilePaths) {
        netFilePath = netFilePaths;
    }

    /**
     * 设置数据库文件名
     */
    protected static void setDbName(String dbNames, int dbIds) {
        dbName = dbNames;
        dbId = dbIds;
    }


    /**
     * 设置图片缓存地址
     */
    private void initPicasso() {
        try {
            Picasso picasso = new Picasso.Builder(this).downloader(new OkHttp3Downloader(new File(BPConfig.CACHE_IMG_PATH), 1024 * 1024 * 100)).build();
            Picasso.setSingletonInstance(picasso);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取网络文件路径
     *
     * @return
     */
    public static String getNetFilePath() {
        return netFilePath;
    }

    public void setAppThemeColor(String colorVlaue, int color) {
        BPConfig.appThemeColor = color;
        BPConfig.appThemeColorValue = colorVlaue;
    }

    /**
     * 重写 getResource 方法，防止系统字体影响
     */
    @Override
    public Resources getResources() {//禁止app字体大小跟随系统字体大小调节
        Resources resources = super.getResources();
        if (resources != null && resources.getConfiguration().fontScale != 1.0f) {
            android.content.res.Configuration configuration = resources.getConfiguration();
            configuration.fontScale = 1.0f;
            resources.updateConfiguration(configuration, resources.getDisplayMetrics());
        }
        return resources;
    }
}
