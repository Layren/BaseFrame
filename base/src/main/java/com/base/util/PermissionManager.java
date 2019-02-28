package com.base.util;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

/**
 * Created by GaoTing on 2018/3/23.
 *
 * Explain :权限管理
 */

public class PermissionManager {
    public static final String[] STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};
    public static final String[] CALENDAR = {
            Manifest.permission.READ_CALENDAR,
            Manifest.permission.WRITE_CALENDAR};
    public static final String[] CAMERA = {
            Manifest.permission.CAMERA};
    public static final String[] CONTACTS = {
            Manifest.permission.READ_CONTACTS,
            Manifest.permission.WRITE_CONTACTS,
            Manifest.permission.GET_ACCOUNTS};
    public static final String[] LOCATION = {
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION};
    public static final String[] MICROPHONE = {
            Manifest.permission.RECORD_AUDIO};
    public static final String[] PHONE = {
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.CALL_PHONE,
            Manifest.permission.READ_CALL_LOG,
            Manifest.permission.WRITE_CALL_LOG,
            Manifest.permission.ADD_VOICEMAIL,
            Manifest.permission.USE_SIP,
            Manifest.permission.PROCESS_OUTGOING_CALLS};
    public static final String[] SENSORS = {
            Manifest.permission.BODY_SENSORS};
    public static final String[] SMS = {
            Manifest.permission.SEND_SMS,
            Manifest.permission.RECEIVE_SMS,
            Manifest.permission.READ_SMS,
            Manifest.permission.RECEIVE_WAP_PUSH,
            Manifest.permission.RECEIVE_MMS};

    private PermissionManager() {
    }

    /**
     * 权限授予
     *
     * @param activity
     */
    public static void granted(Activity activity, String[] permissions, int requestCode) {
        if (Build.VERSION.SDK_INT < 23)
            return;
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(activity, permission) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(activity, permissions, requestCode);
                return;
            }
        }
    }

    /**
     * 查询权限
     */
    public static boolean query(Activity activity, String[] permissions) {
        if (Build.VERSION.SDK_INT < 23)
            return true;
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(activity, permission) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }
}
