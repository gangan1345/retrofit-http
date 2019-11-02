package com.develop.http.callback;

import com.develop.http.Request;

/**
 * @author Angus
 */

public abstract class HttpSimpleCallBack<M> implements HttpCallBack<M> {

    @Override
    public void onHttpStart(Request request) {

    }

    /*
    @Override
    public void onHttpSuccess(Request request, M model) {

    }
    */

    @Override
    public void onHttpFailed(Request request, int errorCode, String message) {

    }

    @Override
    public void onHttpCompleted(Request request) {

    }
}
