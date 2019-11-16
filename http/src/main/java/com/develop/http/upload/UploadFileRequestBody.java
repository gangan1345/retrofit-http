package com.develop.http.upload;


import com.develop.http.callback.TransformProgressListener;

import java.io.File;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okio.Buffer;
import okio.BufferedSink;
import okio.ForwardingSink;
import okio.Okio;
import okio.Sink;

/**
 * 重写request 用于监听读取进度
 *
 * @author Angus
 */
public class UploadFileRequestBody extends RequestBody {

    private RequestBody mRequestBody;
    private TransformProgressListener mProgressListener;

    private BufferedSink mBufferedSink;

    public UploadFileRequestBody(File file, TransformProgressListener progressListener) {
        this.mRequestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        this.mProgressListener = progressListener;
    }

    public UploadFileRequestBody(RequestBody requestBody, TransformProgressListener progressListener) {
        this.mRequestBody = requestBody;
        this.mProgressListener = progressListener;
    }

    /**
     * 返回了requestBody的类型，想什么form-data/MP3/MP4/png等等等格式
     *
     * @return
     */
    @Override
    public MediaType contentType() {
        return mRequestBody.contentType();
    }

    /**
     * 返回了本RequestBody的长度，也就是上传的totalLength
     *
     * @return
     * @throws IOException
     */
    @Override
    public long contentLength() throws IOException {
        return mRequestBody.contentLength();
    }

    @Override
    public void writeTo(BufferedSink sink) throws IOException {
        if (mBufferedSink == null) {
            mBufferedSink = Okio.buffer(sink(sink));
        }
        mRequestBody.writeTo(mBufferedSink);
        mBufferedSink.flush();
    }

    private Sink sink(Sink sink) {
        return new ForwardingSink(sink) {
            long bytesWritten = 0L;
            long contentLength = 0L;

            @Override
            public void write(Buffer source, long byteCount) throws IOException {
                super.write(source, byteCount);
                if (contentLength == 0) {
                    contentLength = contentLength();
                }
                bytesWritten += byteCount;
                mProgressListener.onProgress(bytesWritten, contentLength, bytesWritten == contentLength);
            }
        };
    }
}