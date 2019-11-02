package com.develop.http.interceptor;

import com.develop.http.HttpConfigBuilder;

import java.io.IOException;
import java.util.Map;

import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 网络请求公共参数配置
 * @author Angus
 */

public class HttpCommonParamInterceptor implements Interceptor {
    private HttpConfigBuilder mHttpConfigBuilder;

    public HttpCommonParamInterceptor(HttpConfigBuilder httpConfigBuilder) {
        this.mHttpConfigBuilder = httpConfigBuilder;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request original = chain.request();
        original = addParams(original);
        Request.Builder requestBuilder = original.newBuilder();
        addHeader(requestBuilder);
        Request request = requestBuilder.build();
        return chain.proceed(request);
    }

    private void addHeader(Request.Builder requestBuilder){
        if (mHttpConfigBuilder == null) {
            return;
        }
        Map<String, String> headerMap = mHttpConfigBuilder.getHeaders();
        if (headerMap != null) {
            for (Map.Entry<String, String> entry : headerMap.entrySet()) {
                requestBuilder.header(entry.getKey(), entry.getValue());
            }
        }
    }

    private Request addParams(Request request){
        if (mHttpConfigBuilder == null) {
            return request;
        }

        Map<String, Object> paramMap = mHttpConfigBuilder.getParams();
        if (paramMap != null) {
            switch (request.method()) {
                case "POST":
                    if (request.body() instanceof FormBody) {
                        FormBody.Builder bodyBuilder = new FormBody.Builder();
                        FormBody formBody = (FormBody) request.body();

                        for (int i = 0; i < formBody.size(); i++) {
                            bodyBuilder.addEncoded(formBody.encodedName(i), formBody.encodedValue(i));
                        }

                        for (Map.Entry<String, Object> entry : paramMap.entrySet()) {
                            bodyBuilder.addEncoded(entry.getKey(), entry.getValue().toString());
                        }

                        formBody = bodyBuilder.build();
                        request = request.newBuilder().post(formBody).build();
                    }
                    break;
                case "GET":
                default:
                    HttpUrl.Builder httpUrlBuilder = request.url().newBuilder();
                    for (Map.Entry<String, Object> entry : paramMap.entrySet()) {
                        httpUrlBuilder.addQueryParameter(entry.getKey(), entry.getValue().toString());
                    }
                    request = request.newBuilder().url(httpUrlBuilder.build()).build();
                    break;
            }
        }
        return request;
    }
}
