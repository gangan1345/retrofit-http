package com.develop.http.interceptor;

import com.develop.http.utils.LogUtils;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.UnsupportedCharsetException;

import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.internal.http.HttpHeaders;
import okio.Buffer;
import okio.BufferedSource;

/**
 * http log 打印配置
 * @author Angus
 */

public class HttpLogInterceptor implements Interceptor {
    private static final Charset UTF8 = Charset.forName("UTF-8");
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();

        String headerStr = "";
        Headers headers = request.headers();
        for (int i = 0, count = headers.size(); i < count; i++) {
            String name = headers.name(i);
            if (!"Content-Type".equalsIgnoreCase(name) && !"Content-Length".equalsIgnoreCase(name)) {
                if (i > 0) {
                    headerStr += "&";
                }
                headerStr += name + "=" + headers.value(i);
            }
        }

        String requestBodyStr ="";
        RequestBody requestBody = request.body();
        boolean hasRequestBody = requestBody != null;
        if (hasRequestBody){
            Buffer buffer = new Buffer();
            requestBody.writeTo(buffer);

            Charset charset = UTF8;
            MediaType contentType = requestBody.contentType();
            if (contentType != null) {
                charset = contentType.charset(UTF8);
            }

            requestBodyStr = buffer.readString(charset);
        }
        LogUtils.i(String.format("http request-->[%s] %s headers=[%s] params=[%s]",
                request.method(), request.url(), headerStr, requestBodyStr));


        long t1 = System.nanoTime();
        Response response = chain.proceed(request);
        long t2 = System.nanoTime();

        String responseStr = "";
        ResponseBody responseBody = response.body();
        long contentLength = responseBody.contentLength();
        if (HttpHeaders.promisesBody(response)) {
            BufferedSource source = responseBody.source();
            source.request(Long.MAX_VALUE); // Buffer the entire body.
            Buffer buffer = source.buffer();

            Charset charset = UTF8;
            MediaType contentType = responseBody.contentType();
            if (contentType != null) {
                try {
                    charset = contentType.charset(UTF8);
                } catch (UnsupportedCharsetException e) {
                    LogUtils.i(String.format("http response-->(%d) %s in %.1fms ",
                            response.code(),response.request().url(), (t2 - t1) / 1e6d) +
                            "UnsupportedCharsetException--> Couldn't decode the response body; charset is likely malformed.");
                    return response;
                }
            }

            if (contentLength != 0) {
                responseStr = buffer.clone().readString(charset);
            }
        }
        LogUtils.i(String.format("http response-->(%d) %s in %.1fms ",
                response.code(),response.request().url(), (t2 - t1) / 1e6d) + responseStr);

        return response;
    }
}
