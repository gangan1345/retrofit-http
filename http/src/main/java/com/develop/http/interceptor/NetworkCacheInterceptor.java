package com.develop.http.interceptor;

import com.develop.http.HttpConfigBuilder;
import com.develop.http.utils.NetworkUtils;

import java.io.IOException;

import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * http 缓存配置
 * @author Angus
 */
public class NetworkCacheInterceptor implements Interceptor {
    private HttpConfigBuilder mHttpConfigBuilder;

    public NetworkCacheInterceptor(HttpConfigBuilder mHttpConfigBuilder) {
        this.mHttpConfigBuilder = mHttpConfigBuilder;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        boolean isNetConnected = NetworkUtils.isNetworkConnected(mHttpConfigBuilder.getContext());
        if (!isNetConnected) {//没网强制从缓存读取(必须得写，不然断网状态下，退出应用，或者等待一分钟后，就获取不到缓存）
            request = request.newBuilder()
                    .cacheControl(CacheControl.FORCE_CACHE)
                    .build();
        }
        Response response = chain.proceed(request);
        String cacheControl;
        if (isNetConnected) {
            // 有网的时候读接口上的@Headers里的配置，你可以在这里进行统一的设置
            cacheControl = request.cacheControl().toString();
            //cacheControl =  "public, max-age=" + 60;// 失效一分钟
        } else {
            cacheControl = "public, only-if-cached, max-stale=" + Integer.MAX_VALUE;
        }
        return response.newBuilder()
                .removeHeader("Pragma")
                .removeHeader("Cache-Control")
                .header("Cache-Control", cacheControl)
                .build();
    }
}