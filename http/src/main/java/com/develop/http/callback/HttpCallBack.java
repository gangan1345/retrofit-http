package com.develop.http.callback;

/**
 * 网络请求回调， app业务使用此回调接收结果
 * @author Angus
 */

public interface HttpCallBack<M>  {

    void onStart();

    void onSuccess(M model);

    void onFailed(int errorCode, String message);

    void onCompleted();

    /**
     * 文件上传下载进度
     * @param progress
     * @param total
     * @param completed
     */
    void onProgress(long progress, long total, boolean completed);
}
