package com.develop.http;

import android.content.Context;

import androidx.annotation.NonNull;

import com.develop.http.api.CommonApi;
import com.develop.http.callback.HttpCallBack;
import com.develop.http.callback.HttpParamsInterface;
import com.develop.http.interceptor.HttpCommonParamInterceptor;
import com.develop.http.interceptor.HttpLogInterceptor;
import com.develop.http.interceptor.NetworkCacheInterceptor;
import com.develop.http.utils.NetworkUtils;

import java.io.File;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * @author Angus
 */
public class RetrofitHttp {
    /** 请求超时 */
    public static final int CONNECT_TIME_OUT = 15 * 1000;
    /** 等待数据超时 */
    public static final int SO_TIME_OUT = 30 * 1000;
    /**
     * retrofit缓存文件夹大小：10M
     */
    private static final int RETROFIT_CACHEDIR_SIZE = 10 * 1024 * 1024;

    private HttpConfigBuilder mHttpConfigBuilder;
    private OkHttpClient mOkHttpClient;
    private HashMap<String, Object> mServiceMap = new HashMap<>();
    private CommonApi mCommonApi;

    private volatile static RetrofitHttp mRetrofitHttp;

    public static RetrofitHttp get(){
        synchronized (RetrofitHttp.class){
            if (mRetrofitHttp == null) {
                mRetrofitHttp = new RetrofitHttp();
            }
        }
        return mRetrofitHttp;
    }

    public RetrofitHttp builder(){
        mCommonApi = getApi(CommonApi.class);
        return this;
    }

    public static RetrofitHttp init(HttpConfigBuilder mHttpConfigBuilder) {
        return RetrofitHttp.get().httpConfigBuilder(mHttpConfigBuilder).builder();
    }

    public static RetrofitHttp init(Context context, HttpParamsInterface httpParamsInterface) {
        HttpConfigBuilder builder = HttpConfigBuilder.builder(context).config(httpParamsInterface);
        return RetrofitHttp.init(builder);
    }

    public RetrofitHttp httpConfigBuilder(HttpConfigBuilder mHttpConfigBuilder) {
        this.mHttpConfigBuilder = mHttpConfigBuilder;
        return this;
    }

    public OkHttpClient getOkHttpClient() {
        if (mOkHttpClient == null) {
            OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
            // timeout
            httpClient.connectTimeout(CONNECT_TIME_OUT, TimeUnit.MILLISECONDS);
            httpClient.writeTimeout(SO_TIME_OUT, TimeUnit.MILLISECONDS);
            httpClient.readTimeout(SO_TIME_OUT, TimeUnit.MILLISECONDS);
            // cache
            File httpCacheDirectory = new File(mHttpConfigBuilder.getContext().getCacheDir(), "okhttp_cache");
            httpClient.cache(new Cache(httpCacheDirectory, RETROFIT_CACHEDIR_SIZE));
            // param
            httpClient.addInterceptor(new HttpCommonParamInterceptor(mHttpConfigBuilder));
            // network
            httpClient.addInterceptor(new NetworkCacheInterceptor(mHttpConfigBuilder));
            // log
            if (BuildConfig.LOG_ENABLE) {
                httpClient.addInterceptor(new HttpLogInterceptor());
            }
            //retry when fail
            httpClient.retryOnConnectionFailure(true);

            //todo SSL证书
//            httpClient.sslSocketFactory(HttpsFactroy.getSSLSocketFactory());
//            httpClient.hostnameVerifier(org.apache.http.conn.ssl.SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

            mOkHttpClient = httpClient.build();
        }
        return mOkHttpClient;
    }

    @SuppressWarnings("unchecked")
    public <S> S getApi(Class<S> serviceClass) {
        if (mServiceMap.containsKey(serviceClass.getName())) {
            return (S) mServiceMap.get(serviceClass.getName());
        } else {
            Object obj = createApi(serviceClass);
            mServiceMap.put(serviceClass.getName(), obj);
            return (S) obj;
        }
    }

    @SuppressWarnings("unchecked")
    public <S> S getApi(Class<S> serviceClass, OkHttpClient client) {
        if (mServiceMap.containsKey(serviceClass.getName())) {
            return (S) mServiceMap.get(serviceClass.getName());
        } else {
            Object obj = createApi(serviceClass, client);
            mServiceMap.put(serviceClass.getName(), obj);
            return (S) obj;
        }
    }

    private <S> S createApi(Class<S> serviceClass) {
        return createApi(serviceClass, getOkHttpClient());
    }


    private <S> S createApi(Class<S> serviceClass, OkHttpClient client) {
        String baseUrl = "";
        if (mHttpConfigBuilder != null) {
            baseUrl = mHttpConfigBuilder.getBaseUrl();
        }
        /*
        try {
            Field field1 = serviceClass.getField("baseUrl");
            baseUrl = (String) field1.get(serviceClass);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.getMessage();
            e.printStackTrace();
        }
        */

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(client)
                .build();

        return retrofit.create(serviceClass);
    }

    private static class HttpResponseFunc<T> implements Func1<Throwable, Observable<T>> {
        @Override public Observable<T> call(Throwable t) {
            return Observable.error(t);
        }
    }

    /**
     * @param <T> 用来统一处理Http的resultCode,并将HttpResult的Data部分剥离出来返回给subscriber
     * @param <T> Subscriber真正需要的数据类型，也就是Data部分的数据类型
     *
     * @author Angus
     */
    public static class HttpResultFunc<T> implements Func1<AbsHttpResult<T>, T> {
        @Override
        public T call(AbsHttpResult<T> absHttpResult) {
            if (!absHttpResult.isSuccess()) {
                throw new AbsHttpExceptionHandle.ServerException(HttpErrorCode.CODE_FAILURE, "");
            }
            return absHttpResult.getData();
        }
    }


    public static <T> Observable.Transformer<AbsHttpResult<T>, T> transformer() {

        return new Observable.Transformer() {

            @Override
            public Object call(Object observable) {
                return ((Observable) observable).map(new HttpResultFunc<T>()).onErrorResumeNext(new HttpResponseFunc<T>());
            }
        };
    }

    public static Observable.Transformer schedulersTransformer() {
        return new Observable.Transformer() {

            @Override
            public Object call(Object observable) {
                return ((Observable)  observable).subscribeOn(Schedulers.io())
                        .unsubscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());
            }
        };
    }

    <T> Observable.Transformer<T, T> applySchedulers() {
        return (Observable.Transformer<T, T>) schedulersTransformer();
    }

    public static <T> void subscribe(Observable<T> observable, Subscriber<T> subscriber) {
        observable.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    public static <T> void subscribeCompose(Observable<T> observable, Subscriber<T> subscriber) {
        observable.compose(schedulersTransformer())
                .compose(transformer())
                .subscribe(subscriber);
    }

    public boolean isNetworkAvailable(){
        return NetworkUtils.isNetworkConnected(mHttpConfigBuilder.getContext());
    }

    /**
     * AbsHttpResult 数据结构解析模式
     * @param observable
     * @param callBack
     * @param <T>
     * @param <M>
     * @return
     */
    public static <T extends AbsHttpResult<M>, M> Request request(@NonNull Observable<T> observable, HttpCallBack<M> callBack){
        return Request.request(observable, callBack);
    }

    /**
     * 普通数据解析模式
     * @param observable
     * @param callBack
     * @param <M>
     * @return
     */
    public static <M> Request requestR(@NonNull Observable<M> observable, HttpCallBack<M> callBack){
        return Request.requestR(observable, callBack);
    }

}
