package com.base.net;

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
import okhttp3.internal.Util;
import okio.BufferedSink;
import okio.Okio;
import okio.Source;


/**
 *
 */
public class HttpManager {
    private static OkHttpClient okHttpClient;
    private static List<Cookie> cookies;

    static {
        if (okHttpClient == null) {
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
                            return HttpManager.cookies != null ? HttpManager.cookies : new ArrayList<Cookie>();
                        }
                    });
            okHttpClient = builder.build();
        }
    }

    public static String postUrl(String url, UrlParameters params) throws Exception {
        MultipartBody.Builder mulitBuilder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        FormBody.Builder formBuilder = new FormBody.Builder();
        RequestBody fileBody = null;
        RequestBody body = null;
        Request request = null;
        boolean isJson = false;
        if (params.getFileKeys().size() > 0 || params.isNullFile()) {
            for (String key : params.getmKeys()) {
                mulitBuilder.addFormDataPart(key, params.getValue(key));
            }
            if (params.isNullFile()) {
                body = mulitBuilder.build();
            }
            for (String key : params.getFileKeys()) {
                if (params.getFileType(key) == null)
                    throw new Exception("Not set file Type!");
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
                if (isJson)
                    body = fileBody;
                else body = mulitBuilder.build();
            }
        } else {
            for (String key : params.getmKeys()) {
                formBuilder.add(key, params.getValue(key));
            }
            body = formBuilder.build();
        }
        Request.Builder builder = new Request.Builder().url(url + params.getUrl()).post(body);
        if (params.getHanderKeys().size() > 0) {
            for (String key : params.getHanderKeys()) {
                builder.addHeader(key, params.getHanderValue(key));
            }
        }
        request = builder.build();
        Call call = okHttpClient.newCall(request);
        try {
            Response response = call.execute();
            if (response.code() == 200)
                return response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String getUrl(String url, UrlParameters params) {

        url.replace("%", "");
        StringBuffer Url = new StringBuffer(url);
        Url.append(params.getUrl());
        if (params.getmKeys().size() > 0) {
            if (!Url.toString().contains("?"))
                Url.append("?");
            for (String str : params.getmKeys()) {
                Url.append(str + "=" + params.getValue(str));
                Url.append("&");
            }
        }

        Request.Builder builder = new Request.Builder().url(Url.toString().replace("%", ""));
        if (params.getHanderKeys().size() > 0) {
            for (String key : params.getHanderKeys()) {
                builder.addHeader(key, params.getHanderValue(key));
            }
        }
        Request request = builder.build();
        Call call = okHttpClient.newCall(request);
        try {
            Response response = call.execute();
            if (response.code() == 200)
                return response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

}
