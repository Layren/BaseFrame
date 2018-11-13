package com.base.baseClass;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

/**
 * Created by Administrator on 2017/11/23.
 */

public class BPBroadcastReceiver extends BroadcastReceiver {
    private boolean isLogin;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (context.getPackageName().equals(intent.getAction()))
            switch (intent.getIntExtra("brType", -1)) {
                case BroadType.APPEXP:
                    break;
                case BroadType.OTHER_LOGIN:
                    //帐号异地登录
                    ((Activity) context).finish();
                    break;
                case BroadType.TEST:
                    Toast.makeText(context, "收到了广播", Toast.LENGTH_SHORT).show();
                    break;
            }
    }

    public static class BroadType {
        //程序异常
        public static final int APPEXP = 101;
        //异地登录
        public static final int OTHER_LOGIN = 102;
        //测试
        public static final int TEST = 99;
    }
}
