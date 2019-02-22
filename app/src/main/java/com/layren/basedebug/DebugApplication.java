package com.layren.basedebug;

import android.annotation.SuppressLint;
import android.content.Context;

import com.base.BPApplication;

/**
 * Created by GaoTing on 2018/9/27.
 *
 * @TODO :
 */
public class DebugApplication extends BPApplication {
    private String NetPath = "";
    private String NetFilePath = "";
    private Context context;

    @SuppressLint("ResourceType")
    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        setCACHE_PATH("Layren");
        setNET_PATH(NetPath);
        setNET_FILE_PATH(NetFilePath);
        setAppThemeColor(getResources().getString(R.color.appThemeColor), getResources().getColor(R.color.appThemeColor));
        init();
    }
}
