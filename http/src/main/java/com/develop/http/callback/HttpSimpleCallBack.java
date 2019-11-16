package com.develop.http.callback;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * @author Angus
 */

public abstract class HttpSimpleCallBack<M> implements HttpCallBack<M> {

    protected Type genericityType;

    public HttpSimpleCallBack() {
        Type genericSuperclass = getClass().getGenericSuperclass();
        if (genericSuperclass instanceof ParameterizedType) {
            this.genericityType = ((ParameterizedType) genericSuperclass).getActualTypeArguments()[0];
        } else {
            this.genericityType = Object.class;
        }
    }

    public Type getGenericityType() {
        return genericityType;
    }

    @Override
    public void onStart() {

    }

    /*
    @Override
    public void onSuccess(M model) {

    }
    */

    @Override
    public void onFailed(int errorCode, String message) {

    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onProgress(long progress, long total, boolean completed) {

    }
}
