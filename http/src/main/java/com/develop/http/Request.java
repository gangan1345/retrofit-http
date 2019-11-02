package com.develop.http;

import androidx.annotation.NonNull;

import com.develop.http.callback.HttpCallBack;
import com.develop.http.utils.LogUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import okhttp3.Response;
import rx.Observable;
import rx.Subscriber;


/**
 * http 请求, app 主要调度HttpTask 来完成网络请求
 * @author Angus
 */

public class Request<M> {
    private Observable mObservable;
    private HttpCallBack mCallBack;

    private Request(Observable observable,
                    HttpCallBack callBack) {
        mObservable = observable;
        mCallBack = callBack;
    }

    public static <T extends AbsHttpResult<M>, M> Request request(@NonNull Observable<T> observable, HttpCallBack<M> callBack){
        return new Request(observable, callBack).execute();
    }

    public static <M> Request requestR(@NonNull Observable<M> observable, HttpCallBack<M> callBack){
        return new Request(observable, callBack).executeR();
    }

    public Request execute() {
        onHttpStart();

        if (!RetrofitHttp.get().isNetworkAvailable()) {
            onHttpFailed(HttpErrorCode.CODE_NO_NETWORK, "");
            return this;
        }

        if (mObservable == null){
            onHttpFailed(HttpErrorCode.CODE_FAILURE, "");
            return this;
        }
        // compose方式调用， AbsHttpResult数据结构解析
        RetrofitHttp.subscribeCompose(mObservable, new BaseResultSubscriber());
        return this;
    }

    public Request executeR() {
        onHttpStart();

        if (!RetrofitHttp.get().isNetworkAvailable()) {
            onHttpFailed(HttpErrorCode.CODE_NO_NETWORK, "");
            return this;
        }

        if (mObservable == null){
            onHttpFailed(HttpErrorCode.CODE_FAILURE, "");
            return this;
        }
        // 普通subscribe 调用， 不走AbsHttpResult逻辑
        RetrofitHttp.subscribe(mObservable, new BaseResultSubscriber());
        return this;
    }

    public void onHttpStart() {
        if (null != mCallBack) {
            mCallBack.onHttpStart(this);
        }
    }

    public void onHttpSuccess(final M model) {
        if (null != mCallBack) {
            mCallBack.onHttpSuccess(this, model);
        }
    }

    public void onHttpFailed(final int errorCode, final String message) {
        if (null != mCallBack) {
            mCallBack.onHttpFailed(this, errorCode, message);
        }
    }

    public void onHttpCompleted() {
        if (null != mCallBack) {
            mCallBack.onHttpCompleted(this);
        }
    }

    /**
     * 本地时间与服务器上时间的差值
     */
    private static long sTimeDiff = 0;

    private static void calculateTimeDiff(Response response) {
        String dateStr = response.header("Date");
        try {
            Date date = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz", Locale.US).parse(
                    dateStr);
            sTimeDiff = System.currentTimeMillis() - date.getTime();
            LogUtils.v(String.format("local and server time differ [%d]",sTimeDiff));
        } catch (Exception e) {
            sTimeDiff = 0;
            e.printStackTrace();
            LogUtils.e(e.getMessage());
        }
    }

    /**
     * 获取服务器当前的时间
     */
    public static long getServerTimeMillis() {
        return System.currentTimeMillis() - sTimeDiff;
    }

    /**
     * 网络请求结果回调
     * @author Angus
     */

    public class BaseResultSubscriber extends Subscriber<M> {
        @Override
        public void onStart() {
            onHttpStart();
        }

        @Override
        public void onCompleted() {
            onHttpCompleted();
        }

        @Override
        public void onError(Throwable e) {
            AbsHttpExceptionHandle.ResponeThrowable res = AbsHttpExceptionHandle.handleException(e);
            onHttpFailed(res.code, res.message);
        }

        @Override
        public void onNext(M model) {
            onHttpSuccess(model);
        }
    }
}
