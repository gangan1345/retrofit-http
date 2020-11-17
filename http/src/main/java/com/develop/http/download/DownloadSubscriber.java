package com.develop.http.download;

import com.develop.http.HttpErrorCode;
import com.develop.http.callback.TransformProgressListener;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * @author Angus
 */
public class DownloadSubscriber<T> extends Subscriber<T> implements TransformProgressListener {

    private DownloadFileListener downloadListener;
    private DownloadInfo downloadInfo;

    public DownloadSubscriber(DownloadInfo downloadInfo) {
        this.downloadListener = downloadInfo.getListener();
        this.downloadInfo = downloadInfo;
    }

    public void setDownloadInfo(DownloadInfo downloadInfo) {
        this.downloadListener = downloadInfo.getListener();
        this.downloadInfo = downloadInfo;
    }

    @Override
    public void onStart() {
        if (downloadListener != null) {
            downloadListener.onStart();
        }
    }

    @Override
    public void onCompleted() {
        if (downloadListener != null) {
            downloadListener.onCompleted();
        }
        downloadInfo.setState(DownloadInfo.FINISH);
    }

    @Override
    public void onError(Throwable e) {
        if (downloadListener != null) {
            downloadListener.onFailed(HttpErrorCode.CODE_FAILURE, e.getMessage());
        }
        DownloadManager.getInstance().remove(downloadInfo);
        downloadInfo.setState(DownloadInfo.ERROR);
    }

    @Override
    public void onNext(T t) {
        if (downloadListener != null) {
            downloadListener.onSuccess(t);
        }
    }

    @Override
    public void onProgress(long progress, long total, final boolean completed) {
        if (downloadInfo.getContentLength() > total) {
            progress = downloadInfo.getContentLength() - total + progress;
        } else {
            downloadInfo.setContentLength(total);
        }
        downloadInfo.setReadLength(progress);
        if (downloadListener != null) {
            rx.Observable.just(progress).observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<Long>() {
                        @Override
                        public void call(Long aLong) {
                            if (downloadInfo.getState() == DownloadInfo.PAUSE || downloadInfo.getState() == DownloadInfo.STOP)
                                return;
                            downloadInfo.setState(DownloadInfo.DOWNLOAD);
                            if (downloadListener != null) {
                                downloadListener.onProgress(aLong, downloadInfo.getContentLength(), completed);
                            }
                        }
                    });
        }
    }

    public boolean isDownloading(){
        if (downloadInfo != null && DownloadInfo.DOWNLOAD == downloadInfo.getState()) {
            return true;
        }
        return false;
    }
}
