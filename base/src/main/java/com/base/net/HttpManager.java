package com.base.net;

import com.base.config.TypeNullException;
import com.base.util.Tool;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.internal.Util;
import okio.BufferedSink;
import okio.Okio;
import okio.Source;


/**
 *
 */
class HttpManager {
    private static OkHttpClient okHttpClient;
    private static List<Cookie> cookies;

    private HttpManager() {
    }

    static {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.writeTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .connectTimeout(15, TimeUnit.SECONDS)
                .cookieJar(new CookieJar() {
                    @Override
                    public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
                        HttpManager.cookies = cookies;
                    }

                    @Override
                    public List<Cookie> loadForRequest(HttpUrl url) {
                        return HttpManager.cookies != null ? HttpManager.cookies : new ArrayList<>();
                    }
                });
        okHttpClient = builder.build();
    }

    static String postUrl(String url, UrlParameters params) throws TypeNullException {
        FormBody.Builder formBuilder = new FormBody.Builder();
        RequestBody body;
        Request request;
        if (params.isNullFile() || !params.getFileKeys().isEmpty()) {
            body = addFile(params);
        } else {
            for (String key : params.getmKeys()) {
                formBuilder.add(key, params.getValue(key));
            }
            body = formBuilder.build();
        }
        Request.Builder builder = new Request.Builder().url(url + params.getUrl()).post(body);
        if (!params.getHanderKeys().isEmpty()) {
            for (String key : params.getHanderKeys()) {
                builder.addHeader(key, params.getHanderValue(key));
            }
        }
        request = builder.build();
        Call call = okHttpClient.newCall(request);
        try {
            Response response = call.execute();
            if (response.code() == 200) {
                ResponseBody rBody = response.body();
                return rBody != null ? rBody.string() : "";
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    private static RequestBody addFile(UrlParameters params) throws TypeNullException {
        MultipartBody.Builder mulitBuilder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        RequestBody fileBody = null;
        boolean isJson = false;
        for (String key : params.getmKeys()) {
            mulitBuilder.addFormDataPart(key, params.getValue(key));
        }
        for (String key : params.getFileKeys()) {
            if (params.getFileType(key) == null) {
                throw new TypeNullException("Not set file Type!");
            }
            switch (params.getFileType(key)) {
                case FILE:
                    final MediaType fileMediaType = MediaType.parse("application/octet-stream");
                    for (Object object : params.getFileValues(key)) {
                        final InputStream in = (InputStream) object;
                        fileBody = new RequestBody() {
                            @Override
                            public MediaType contentType() {
                                return fileMediaType;
                            }

                            @Override
                            public void writeTo(BufferedSink sink) throws IOException {
                                Source source = null;
                                try {
                                    source = Okio.source(in);
                                    sink.writeAll(source);
                                } finally {
                                    Util.closeQuietly(source);
                                }
                            }

                        };
                        mulitBuilder.addFormDataPart(key, Tool.getRandom(Integer.parseInt(Tool.getRandomNum(2))), fileBody);
                    }
                    break;
                case IMAGE:
                    final MediaType imageMediaType = MediaType.parse("multipart/form-data;charset=utf-8");
                    for (Object object : params.getFileValues(key)) {
                        final InputStream in = (InputStream) object;
                        fileBody = new RequestBody() {
                            @Override
                            public MediaType contentType() {
                                return imageMediaType;
                            }

                            @Override
                            public long contentLength() throws IOException {
                                return in.available();
                            }

                            @Override
                            public void writeTo(BufferedSink sink) throws IOException {
                                Source source = null;
                                try {
                                    source = Okio.source(in);
                                    sink.writeAll(source);
                                } finally {
                                    Util.closeQuietly(source);
                                }
                            }

                        };
                        mulitBuilder.addFormDataPart(key, Tool.getRandom(Integer.parseInt(Tool.getRandomNum(2))), fileBody);
                    }
                    break;
                case JSON:
                    MediaType jsonMediaType = MediaType.parse("application/json; charset=utf-8");
                    Object object = params.getFileValues(key).get(0);
                    String json = new Gson().toJson(object);
                    fileBody = RequestBody.create(jsonMediaType, json);
                    isJson = true;
                    break;
            }

        }
        return isJson ? fileBody : mulitBuilder.build();
    }

    static  String  getUrl(String url, UrlParameters params) {

        StringBuilder buffer = new StringBuilder();
        buffer.append(url);
        buffer.append(params.getUrl());
        if (!params.getmKeys().isEmpty()) {
            if (!buffer.toString().contains("?"))
                buffer.append("?");
            for (String str : params.getmKeys()) {
                buffer.append(str);
                buffer.append("=");
                buffer.append(params.getValue(str));
                buffer.append("&");
            }
        }

        Request.Builder builder = new Request.Builder().url(buffer.toString().replace("%", ""));
        if (!params.getHanderKeys().isEmpty()) {
            for (String key : params.getHanderKeys()) {
                builder.addHeader(key, params.getHanderValue(key));
            }
        }
        Request request = builder.build();
        Call call = okHttpClient.newCall(request);
        try {
            Response response = call.execute();
            if (response.code() == 200) {
                ResponseBody rBody = response.body();
                return rBody != null ? rBody.string() : "";
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

}
