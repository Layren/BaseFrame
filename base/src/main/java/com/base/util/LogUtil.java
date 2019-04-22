package com.base.util;

import android.util.Log;

import com.base.BPApplication;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by GaoTing on 2019/4/17.
 * <p>
 * Explain:Log工具
 */
public class LogUtil {
    private static final String LINE_SEPARATOR = System.getProperty("line.separator");

    public static void printJson(String tag, String msg, String headString) {
        if (!BPApplication.isDebug)
            return;
        String message;
        try {
            if (msg.startsWith("{")) {
                JSONObject jsonObject = new JSONObject(msg);
                message = jsonObject.toString(4);//最重要的方法，就一行，返回格式化的json字符串，其中的数字4是缩进字符数
            } else if (msg.startsWith("[")) {
                JSONArray jsonArray = new JSONArray(msg);
                message = jsonArray.toString(4);
            } else {
                message = msg;
            }
        } catch (JSONException e) {
            message = msg;
        }
        message = headString + LINE_SEPARATOR + message;
        String[] lines = message.split(LINE_SEPARATOR);
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("══════════════════Start═══════════════════════════════════════════");
        for (String line : lines) {
            if (stringBuilder.length() > 0)
                stringBuilder.append("\n");
            stringBuilder.append(line);
        }
        stringBuilder.append("\n═════════════════End════════════════════════════════════════════");
        Log.d(tag, stringBuilder.toString());
    }

}
