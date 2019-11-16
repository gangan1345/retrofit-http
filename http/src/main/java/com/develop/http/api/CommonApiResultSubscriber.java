package com.develop.http.api;

import android.content.Context;

import com.develop.http.AbsHttpExceptionHandle;
import com.develop.http.HttpErrorCode;
import com.develop.http.callback.HttpSimpleCallBack;
import com.develop.http.utils.LogUtils;
import com.google.gson.Gson;

import java.lang.reflect.Type;

import okhttp3.ResponseBody;
import rx.Subscriber;

/**
 * @author Angus
 */
public class CommonApiResultSubscriber<M extends ResponseBody> extends Subscriber<M> {

    private Context mContext;
    private HttpSimpleCallBack mCallBack;

    public CommonApiResultSubscriber(Context mContext, HttpSimpleCallBack callback) {
        this.mContext = mContext;
        this.mCallBack = callback;
    }

    @Override
    public void onStart() {
        if (null != mCallBack) {
            mCallBack.onStart();
        }
    }

    @Override
    public void onCompleted() {
        if (null != mCallBack) {
            mCallBack.onCompleted();
        }
    }

    @Override
    public void onError(Throwable e) {
        AbsHttpExceptionHandle.ResponeThrowable res = AbsHttpExceptionHandle.handleException(e);
        if (null != mCallBack) {
            mCallBack.onFailed(res.code, res.message);
        }
    }

    @Override
    public void onNext(M model) {

        if (model.contentLength() == 0) {
            return;
        }
        if (mCallBack != null) {
            boolean returnJson = false;
            Type genericityType = mCallBack.getGenericityType();
            if (genericityType instanceof Class) {
                switch (((Class) genericityType).getSimpleName()) {
                    case "Object":
                    case "String":
                        returnJson = true;
                        break;
                    default:
                        break;
                }
            }

            if (returnJson) {
                try {
                    mCallBack.onSuccess(model.toString());
                } catch (Exception e) {
                    mCallBack.onFailed(HttpErrorCode.CODE_PARSE_ERROR, HttpErrorCode.getCodeMessage(HttpErrorCode.CODE_PARSE_ERROR));
                    e.printStackTrace();
                    String message = e.getClass().getCanonicalName() + "，" + (null != e.getMessage() ? e.getMessage() : "");
                    LogUtils.e("http exception response-->" + message);
                }
            } else {
                try {
                    mCallBack.onSuccess((new Gson()).fromJson(model.string(), genericityType));
                } catch (Exception e) {
                    mCallBack.onFailed(HttpErrorCode.CODE_PARSE_ERROR, HttpErrorCode.getCodeMessage(HttpErrorCode.CODE_PARSE_ERROR));
                    e.printStackTrace();
                    String message = e.getClass().getCanonicalName() + "，" + (null != e.getMessage() ? e.getMessage() : "");
                    LogUtils.e("http exception response-->" + message);
                }
            }
        }
    }

}
