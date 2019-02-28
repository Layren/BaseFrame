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
            switch ((BroadType) intent.getSerializableExtra("brType")) {
                case APPEXP:
                    break;
                case OTHER_LOGIN:
                    //帐号异地登录
                    ((Activity) context).finish();
                    break;
                case TEST:
                    Toast.makeText(context, "收到了广播", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
    }

    public enum BroadType {
        //程序异常
        APPEXP,
        //异地登录
        OTHER_LOGIN,
        //测试
        TEST;
    }
}
