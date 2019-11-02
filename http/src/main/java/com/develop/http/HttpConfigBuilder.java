package com.develop.http;

import android.content.Context;

import com.develop.http.callback.HttpParamsInterface;

import java.util.Map;

/**
 * 相关参数配置
 * @author Angus
 */
public class HttpConfigBuilder {
    private Context context;
    private String baseUrl;
    private Map<String, String> headers;
    private Map<String, Object> params;

    public static HttpConfigBuilder builder(Context context){
        return new HttpConfigBuilder().context(context);
    }

    public HttpConfigBuilder context(Context context) {
        this.context = context;
        return this;
    }

    public HttpConfigBuilder baseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
        return this;
    }

    public HttpConfigBuilder headers(Map<String, String> headers) {
        this.headers = headers;
        return this;
    }

    public HttpConfigBuilder params(Map<String, Object> params) {
        this.params = params;
        return this;
    }

    public HttpConfigBuilder config(HttpParamsInterface mHttpConfigInterface) {
        baseUrl(mHttpConfigInterface.getBaseUrl());
        headers(mHttpConfigInterface.getHeaders());
        params(mHttpConfigInterface.getParams());
        return this;
    }

    public Context getContext() {
        return context;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public Map<String, Object> getParams() {
        return params;
    }
}
