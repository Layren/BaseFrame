package com.base.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * Created by Administrator on 2017/11/3.
 */

public class LoginManager {

    private static final String KEY = "userInfo";
    private static SharedPreferences preferences;
    private static LoginManager manager;

    private LoginManager() {
    }

    public static LoginManager getInstance() {
        if (manager == null)
            manager = new LoginManager();
        return manager;
    }

    public void setFileName(Context context, String fileName) {
        preferences = context.getSharedPreferences(fileName, 0);
    }

    public void clear() {
        preferences.edit().clear().apply();
    }

    public void saveUserInfo(Object obj) {
        try {
            //先将序列化结果写到byte缓存中，其实就分配一个内存空间
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream os = new ObjectOutputStream(bos);
            //将对象序列化写入byte缓存
            os.writeObject(obj);
            //将序列化的数据转为16进制保存
            String bytesToHexString = bytesToHexString(bos.toByteArray());
            //保存该16进制数组
            preferences.edit().putString(KEY, bytesToHexString).apply();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveCookies(String cookies) {
        preferences.edit().putString("cookies", cookies).apply();
    }

    public String readCookies() {
        return preferences.getString("cookies", "");
    }

    /**
     * desc:将数组转为16进制
     *
     * @param bArray
     * @return modified:
     */
    private static String bytesToHexString(byte[] bArray) {
        if (bArray == null) {
            return null;
        }
        if (bArray.length == 0) {
            return "";
        }
        StringBuilder sb = new StringBuilder(bArray.length);
        String sTemp;
        for (byte aBArray : bArray) {
            sTemp = Integer.toHexString(0xFF & aBArray);
            if (sTemp.length() < 2)
                sb.append(0);
            sb.append(sTemp.toUpperCase());
        }
        return sb.toString();
    }

    /**
     * desc:获取保存的Object对象
     *
     * @return modified:
     */
    public Object readUserInfo() {
        try {
            if (preferences.contains(KEY)) {
                String string = preferences.getString(KEY, "");
                if (TextUtils.isEmpty(string)) {
                    return null;
                } else {
                    //将16进制的数据转为数组，准备反序列化
                    byte[] stringToBytes = stringToBytes(string);
                    ByteArrayInputStream bis = new ByteArrayInputStream(stringToBytes);
                    ObjectInputStream is = new ObjectInputStream(bis);
                    //返回反序列化得到的对象
                    return is.readObject();
                }
            }
        } catch (ClassNotFoundException | IOException e) {
            e.printStackTrace();
        }
        return null;

    }

    private static byte[] stringToBytes(String data) {
        byte[] nullData = new byte[]{};
        String hexString = data.toUpperCase().trim();
        if (hexString.length() % 2 != 0) {
            return nullData;
        }
        byte[] retData = new byte[hexString.length() / 2];
        for (int i = 0; i < hexString.length(); i += 2) {
            int intCh;  // 两位16进制数转化后的10进制数
            char hexChar1 = hexString.charAt(i); ////两位16进制数中的第一位(高位*16)
            int intCh3;
            if (hexChar1 >= '0' && hexChar1 <= '9')
                intCh3 = (hexChar1 - 48) * 16;   //// 0 的Ascll - 48
            else if (hexChar1 >= 'A' && hexChar1 <= 'F')
                intCh3 = (hexChar1 - 55) * 16; //// A 的Ascll - 65
            else
                return nullData;
            char hexChar2 = hexString.charAt(i + 1); ///两位16进制数中的第二位(低位)
            int intCh4;
            if (hexChar2 >= '0' && hexChar2 <= '9')
                intCh4 = (hexChar2 - 48); //// 0 的Ascll - 48
            else if (hexChar2 >= 'A' && hexChar2 <= 'F')
                intCh4 = hexChar2 - 55; //// A 的Ascll - 65
            else
                return nullData;
            intCh = intCh3 + intCh4;
            retData[(i + 1) / 2] = (byte) intCh;//将转化后的数放入Byte里
        }
        return retData;
    }
}
