package com.develop.http.callback;

import com.develop.http.Request;

/**
 * 网络请求回调， app业务使用此回调接收结果
 * @author Angus
 */

public interface HttpCallBack<M>  {

    void onHttpStart(Request request);

    void onHttpSuccess(Request request, M model);

    void onHttpFailed(Request request, int errorCode, String message);

    void onHttpCompleted(Request request);
}
