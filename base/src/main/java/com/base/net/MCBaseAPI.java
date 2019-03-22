package com.base.net;

import android.util.Log;

import com.base.BPApplication;
import com.base.interfaces.SNRequestDataListener;
import com.base.model.Base;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.lang.reflect.Type;


public class MCBaseAPI extends SNNetAPI {

    private static final String API_SERVER;
    public static final String API_FILES;
    private Type type;

    static {
        API_SERVER = BPApplication.getNetPath();
        API_FILES = BPApplication.getNetFilePath();
    }

    public MCBaseAPI(SNRequestDataListener listener, Type type) {
        super(listener);
        this.type = type;
    }

    protected void get(UrlParameters params, int whichAPI) {
        super.getUrl(API_SERVER, params, whichAPI);
    }

    protected void post(UrlParameters params, int whichAPI) {
        super.postUrl(API_SERVER, params, whichAPI);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void parseData(String response, int whichAPI) {
        Gson gson = new Gson();
        JSONObject rootObject = null;
        String responseObj = null;
        try {
            rootObject = new JSONObject(response);
            responseObj = rootObject.getString("data");
            if (responseObj.startsWith("[")) {
                response = response.replaceFirst("data", "listData");
            }
            this.base = gson.fromJson(response, type);
            this.base.setDatas(responseObj);
        } catch (Exception e) {
            Log.e("JsonError：", "API:" + whichAPI
                    + "\n Response:" + response);
            e.printStackTrace();
            this.base = new Base();
        }

        this.base.setResultMsg(response);
    }

}
