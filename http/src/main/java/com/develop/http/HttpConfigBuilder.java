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

    private HttpParamsInterface mHttpConfigInterface;

    public static HttpConfigBuilder builder(Context context){
        return new HttpConfigBuilder().context(context);
    }

    public HttpConfigBuilder context(Context context) {
        this.context = context;
        return this;
    }

    public HttpConfigBuilder config(HttpParamsInterface mHttpConfigInterface) {
        this.mHttpConfigInterface = mHttpConfigInterface;
        return this;
    }

    public Context getContext() {
        return context;
    }

    public String getBaseUrl() {
        if (mHttpConfigInterface != null) {
            return mHttpConfigInterface.getBaseUrl();
        }
        return null;
    }

    public Map<String, String> getHeaders() {
        if (mHttpConfigInterface != null) {
            return mHttpConfigInterface.getHeaders();
        }
        return null;
    }

    public Map<String, Object> getParams() {
        if (mHttpConfigInterface != null) {
            return mHttpConfigInterface.getParams();
        }
        return null;
    }
}
