package com.develop.http.callback;

/**
 * 进度监听 提供给下载与上传进度，依赖主线程
 * @author Angus
 */
public interface TransformProgressListener {
    void onProgress(long progress, long total, boolean completed);
}
