package com.develop.http.demo.api.common;

import com.develop.http.callback.HttpParamsInterface;
import com.develop.http.demo.BuildConfig;

import java.util.HashMap;
import java.util.Map;

/**
 * 配置app http请求公共参数
 * @author Angus
 */
public class HttpParams implements HttpParamsInterface {
    Map<String, String> headers;

    public HttpParams() {
        headers = new HashMap<>();
        headers.put("platfrom", "android");
    }

    @Override
    public String getBaseUrl() {
        return BuildConfig.API_SERVER_URL;
    }

    @Override
    public Map<String, String> getHeaders() {
        return headers;
    }

    @Override
    public Map<String, Object> getParams() {
        return null;
    }
}
