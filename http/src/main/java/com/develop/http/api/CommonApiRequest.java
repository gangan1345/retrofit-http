package com.develop.http.api;

import android.content.Context;
import android.graphics.Bitmap;

import com.develop.http.HttpErrorCode;
import com.develop.http.RetrofitHttp;
import com.develop.http.callback.HttpSimpleCallBack;
import com.develop.http.callback.TransformProgressListener;
import com.develop.http.download.DownloadFileListener;
import com.develop.http.download.DownloadInfo;
import com.develop.http.download.DownloadManager;
import com.develop.http.upload.UploadFileRequestBody;
import com.develop.http.utils.BitmapUtils;
import com.google.gson.Gson;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import rx.Observable;

/**
 * @author Angus
 */
public class CommonApiRequest {
    private CommonApi mCommonApi;
    private Context mContext;
    private Observable.Transformer schedulerTransformer = RetrofitHttp.schedulersTransformer();

    public CommonApiRequest(CommonApi mCommonApi) {
        this.mCommonApi = mCommonApi;
    }

    private void request(Context context, Observable observable, HttpSimpleCallBack callBack) {
        this.mContext = context;
        observable.compose(schedulerTransformer).subscribe(new CommonApiResultSubscriber(context, callBack));
    }

    public <M> void get(Context context, String url, Map<String, Object> parameters, HttpSimpleCallBack<M> callBack) {
        request(context, mCommonApi.doGet(url, parameters), callBack);
    }

    public <M, T> void get(Context context, String url, T body, HttpSimpleCallBack<M> callBack) {
        String parameters = new Gson().toJson(body);
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), parameters);
        request(context, mCommonApi.doGet(url, requestBody), callBack);
    }

    public <M> void get(Context context, String url, HttpSimpleCallBack<M> callBack) {
        request(context, mCommonApi.doGet(url), callBack);
    }

    public <M> void post(Context context, String url, Map<String, Object> parameters, HttpSimpleCallBack<M> callBack) {
        request(context, mCommonApi.doPost(url, parameters), callBack);
    }

    public <M, T> void post(Context context, String url, T body, HttpSimpleCallBack<M> callBack) {
        String parameters = new Gson().toJson(body);
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), parameters);
        request(context, mCommonApi.doPost(url, requestBody), callBack);
    }

    public <M> void put(Context context, String url, Map<String, Object> parameters, HttpSimpleCallBack<M> callBack) {
        request(context, mCommonApi.doPut(url, parameters), callBack);
    }

    public <M, T> void put(Context context, String url, T body, HttpSimpleCallBack<M> callBack) {
        String parameters = new Gson().toJson(body);
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), parameters);
        request(context, mCommonApi.doPut(url, requestBody), callBack);
    }

    public <M> void delete(Context context, String url, HttpSimpleCallBack<M> callBack) {
        request(context, mCommonApi.doDelete(url), callBack);
    }

    public <M> void delete(Context context, String url, Map<String, Object> parameters, HttpSimpleCallBack<M> callBack) {
        request(context, mCommonApi.doDelete(url, parameters), callBack);
    }

    public <M, T> void delete(Context context, String url, T body, HttpSimpleCallBack<M> callBack) {
        String parameters = new Gson().toJson(body);
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), parameters);
        request(context, mCommonApi.doDelete(url, requestBody), callBack);
    }

    public <M> void getFullPath(Context context, String url, HttpSimpleCallBack<M> callBack) {
        request(context, mCommonApi.doGetFullPath(url), callBack);
    }

    public <M> void getFullPath(Context context, String url, Map<String, Object> parameters, HttpSimpleCallBack<M> callBack) {
        if (parameters == null || parameters.size() == 0) {
            request(context, mCommonApi.doGetFullPath(url), callBack);
        } else {
            request(context, mCommonApi.doGetFullPath(url, parameters), callBack);
        }
    }

    public <M> void postFullPath(Context context, String url, Map<String, Object> parameters, HttpSimpleCallBack<M> callBack) {
        request(context, mCommonApi.doPostFullPath(url, parameters), callBack);
    }

    public <M> void uploadFile(Context context, String url, Bitmap bitmap, String fileName, String fileDes, Map<String, Object> queryMap, final HttpSimpleCallBack<M> callBack) {
        byte[] file = BitmapUtils.bitmap2Bytes(bitmap);
        uploadFile(context, url, file, fileName, fileDes, queryMap, false, callBack);
    }

    public <M> void uploadFileFullPath(Context context, String url, Bitmap bitmap, String fileName, String fileDes, Map<String, Object> queryMap, final HttpSimpleCallBack<M> callBack) {
        byte[] file = BitmapUtils.bitmap2Bytes(bitmap);
        uploadFile(context, url, file, fileName, fileDes, queryMap, true, callBack);
    }

    public <M> void uploadFile(Context context, String url, byte[] file, String fileName, String fileDes, Map<String, Object> queryMap, final HttpSimpleCallBack<M> callBack) {
        uploadFile(context, url, file, fileName, fileDes, queryMap, false, callBack);
    }

    public <M> void uploadFileFullPath(Context context, String url, byte[] file, String fileName, String fileDes, Map<String, Object> queryMap, final HttpSimpleCallBack<M> callBack) {
        uploadFile(context, url, file, fileName, fileDes, queryMap, true, callBack);
    }

    private <M> void uploadFile(Context context, String url, final byte[] file, String fileName, String fileDes, Map<String, Object> queryMap, boolean isFullPath, final HttpSimpleCallBack<M> callBack) {
        RequestBody description = RequestBody.create(MediaType.parse("multipart/form-data; charset=utf-8"), fileDes);
        // RequestBody requestBody = UploadFileRequestBody.create(MediaType.parse("multipart/form-data"), file);
        UploadFileRequestBody requestBody = new UploadFileRequestBody(file, new TransformProgressListener() {

            @Override
            public void onProgress(long progress, long total, boolean completed) {
                if (completed) {
                    progress = total;
                }
                callBack.onProgress(progress, total, completed);
            }
        });
        MultipartBody.Part part = MultipartBody.Part.createFormData("file", fileName, requestBody);
        if (queryMap == null) {
            queryMap = new HashMap<>();
        }
        if (isFullPath) {
            request(context, mCommonApi.uploadFileFullPath(url, queryMap, description, part), callBack);
        } else {
            request(context, mCommonApi.uploadFile(url, queryMap, description, part), callBack);
        }
    }

    public <M> void uploadFile(Context context, String url, String filePath, String fileDes, final HttpSimpleCallBack<M> callBack) {
        uploadFile(context, url, filePath, fileDes, null, false, callBack);
    }

    public <M> void uploadFileFullPath(Context context, String url, String filePath, String fileDes, final HttpSimpleCallBack<M> callBack) {
        uploadFile(context, url, filePath, fileDes, null, true, callBack);
    }

    public <M> void uploadFile(Context context, String url, String filePath, String fileDes, Map<String, Object> queryMap, final HttpSimpleCallBack<M> callBack) {
        uploadFile(context, url, filePath, fileDes, queryMap, false, callBack);
    }

    public <M> void uploadFileFullPath(Context context, String url, String filePath, String fileDes, Map<String, Object> queryMap, final HttpSimpleCallBack<M> callBack) {
        uploadFile(context, url, filePath, fileDes, queryMap, true, callBack);
    }

    private <M> void uploadFile(Context context, String url, String filePath, String fileDes, Map<String, Object> queryMap, boolean isFullPath, final HttpSimpleCallBack<M> callBack) {
        final File file = new File(filePath);
        if (!file.exists()) {
            return;
        }
        RequestBody description = RequestBody.create(MediaType.parse("multipart/form-data; charset=utf-8"), fileDes);
//        RequestBody requestBody = UploadFileRequestBody.create(MediaType.parse("multipart/form-data"), file);
        UploadFileRequestBody requestBody = new UploadFileRequestBody(file, new TransformProgressListener() {

            @Override
            public void onProgress(long progress, long total, boolean completed) {
                if (completed) {
                    progress = total;
                }
                callBack.onProgress(progress, total, completed);
            }
        });
        MultipartBody.Part part = MultipartBody.Part.createFormData("file", file.getName(), requestBody);
        if (queryMap == null) {
            queryMap = new HashMap<>();
        }
        if (isFullPath) {
            request(context, mCommonApi.uploadFileFullPath(url, queryMap, description, part), callBack);
        } else {
            request(context, mCommonApi.uploadFile(url, queryMap, description, part), callBack);
        }
    }

    public <M> void uploadFiles(Context context, String url, List<String> filePathList, final HttpSimpleCallBack<M> callBack) {
        uploadFiles(context, url, filePathList, null, false, callBack);
    }

    public <M> void uploadFilesFullPath(Context context, String url, List<String> filePathList, final HttpSimpleCallBack<M> callBack) {
        uploadFiles(context, url, filePathList, null, true, callBack);
    }

    public <M> void uploadFiles(Context context, String url, List<String> filePathList, Map<String, Object> queryMap, final HttpSimpleCallBack<M> callBack) {
        uploadFiles(context, url, filePathList, queryMap, false, callBack);
    }

    public <M> void uploadFilesFullPath(Context context, String url, List<String> filePathList, Map<String, Object> queryMap, final HttpSimpleCallBack<M> callBack) {
        uploadFiles(context, url, filePathList, queryMap, true, callBack);
    }

    private long curUploadProgress = 0;

    private <M> void uploadFiles(Context context, String url, List<String> filePathList, Map<String, Object> queryMap, boolean isFullPath, final HttpSimpleCallBack<M> callBack) {
        curUploadProgress = 0;
        if (filePathList == null || filePathList.size() == 0) {
            return;
        }
        List<File> fileList = new ArrayList<>();
        long totalSize = 0;
        for (String filePath : filePathList) {
            File file = new File(filePath);
            if (!file.exists()) {
                continue;
            }
            totalSize += file.length();
            fileList.add(file);
        }

        HashMap<String, RequestBody> params = new HashMap<>();
        for (int i = 0; i < fileList.size(); i++) {
            File file = fileList.get(i);
//            RequestBody body = RequestBody.create(MediaType.parse("multipart/form-data"), file);

            final long finalTotalSize = totalSize;
            UploadFileRequestBody body = new UploadFileRequestBody(file, new TransformProgressListener() {

                @Override
                public void onProgress(long progress, long total, boolean completed) {
                    if (completed) {
                        curUploadProgress += total;
                    }
                    callBack.onProgress(completed ? curUploadProgress : (curUploadProgress + progress), finalTotalSize, curUploadProgress == finalTotalSize);
                }
            });
            params.put("file[]\"; filename=\"" + file.getName(), body);
        }
        if (queryMap == null) {
            queryMap = new HashMap<>();
        }
        if (isFullPath) {
            request(context, mCommonApi.uploadFilesFullPath(url, queryMap, params), callBack);
        } else {
            request(context, mCommonApi.uploadFiles(url, queryMap, params), callBack);
        }
    }

    public void syncGet(Context context, String url, Map<String, Object> parameters, HttpSimpleCallBack callback) {
        this.mContext = context;
        StringBuilder sb = new StringBuilder();
        sb.append(url);
        if (parameters != null && parameters.size() > 0) {
            sb.append("?");
            for (Map.Entry<String, Object> map : parameters.entrySet()) {
                sb.append(map.getKey()).append("=").append(map.getValue()).append("&");
            }
            sb.deleteCharAt(sb.lastIndexOf("&"));
        }
        Request request = new Request.Builder().url(sb.toString()).build();
        Call call = RetrofitHttp.get().getOkHttpClient().newCall(request);
        try {
            Response response = call.execute();
            if (response.isSuccessful()) {
                callback.onSuccess(response.body().string());
            } else {
                callback.onFailed(HttpErrorCode.CODE_FAILURE, response.message());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void syncPost(Context context, String url, Map<String, Object> parameters, HttpSimpleCallBack callBack) {
        this.mContext = context;
        FormBody.Builder builder = new FormBody.Builder();
        if (parameters != null && parameters.size() > 0) {
            for (Map.Entry<String, Object> map : parameters.entrySet()) {
                builder.add(map.getKey(), String.valueOf(map.getValue()));
            }
        }
        Request request = new Request.Builder().url(url).post(builder.build()).build();
        Call call = RetrofitHttp.get().getOkHttpClient().newCall(request);
        try {
            Response response = call.execute();
            if (response.isSuccessful()) {
                callBack.onSuccess(response.body().string());
            } else {
                callBack.onFailed(HttpErrorCode.CODE_FAILURE, response.message());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void download(String url, String savePath, DownloadFileListener listener) {
        DownloadInfo info = new DownloadInfo(url, savePath);
        info.setState(DownloadInfo.START);
        info.setListener(listener);
        DownloadManager downloadManager = DownloadManager.getInstance();
        downloadManager.startDown(info);
    }

    public DownloadManager getDownloadManager() {
        return DownloadManager.getInstance();
    }
}
