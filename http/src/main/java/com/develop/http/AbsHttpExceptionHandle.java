package com.develop.http;

import com.develop.http.utils.LogUtils;

import org.apache.http.conn.ConnectTimeoutException;

import java.net.ConnectException;
import java.net.SocketException;

import retrofit2.adapter.rxjava.HttpException;

/**
 * 网络异常处理
 * @author Angus
 */
public class AbsHttpExceptionHandle {
    private static final int UNAUTHORIZED = 401;
    private static final int FORBIDDEN = 403;
    private static final int NOT_FOUND = 404;
    private static final int REQUEST_TIMEOUT = 408;
    private static final int INTERNAL_SERVER_ERROR = 500;
    private static final int BAD_GATEWAY = 502;
    private static final int SERVICE_UNAVAILABLE = 503;
    private static final int GATEWAY_TIMEOUT = 504;

    public static ResponeThrowable handleException(Throwable e) {
        String message = e.getClass().getCanonicalName() + "，" + (null != e.getMessage() ? e.getMessage() : "");
        String defaultMsg = "网络异常，请稍后重试";
        ResponeThrowable ex;
        if (e instanceof HttpException) {
            HttpException httpException = (HttpException) e;
            ex = new ResponeThrowable(e, HttpErrorCode.CODE_FAILURE);
            switch (httpException.code()) {
                case UNAUTHORIZED:
                case FORBIDDEN:
                case NOT_FOUND:
                case REQUEST_TIMEOUT:
                case GATEWAY_TIMEOUT:
                case INTERNAL_SERVER_ERROR:
                case BAD_GATEWAY:
                case SERVICE_UNAVAILABLE:
                default:
                    ex.message = defaultMsg;
                    break;
            }
            message = "["+httpException.code()+"]" + message;
        } else if (e instanceof ServerException) {
            ServerException resultException = (ServerException) e;
            ex = new ResponeThrowable(resultException, resultException.code);
            ex.message = resultException.message;
            message = "["+resultException.code+"]" + message;
        } else if (e instanceof ConnectException || e instanceof SocketException) {
            ex = new ResponeThrowable(e, HttpErrorCode.CODE_NO_NETWORK);
            ex.message = "无法连接服务器，请检查网络";
        } else if (e instanceof java.net.SocketTimeoutException || e instanceof ConnectTimeoutException) {
            ex = new ResponeThrowable(e, HttpErrorCode.CODE_TIMEOUT);
            ex.message = defaultMsg;
        } else {
            ex = new ResponeThrowable(e, HttpErrorCode.CODE_FAILURE);
            ex.message = defaultMsg;
        }
        LogUtils.e("http exception response-->" + message);
        return ex;
    }

    public static class ResponeThrowable extends Exception {
        public int code;
        public String message;

        public ResponeThrowable(Throwable throwable, int code) {
            super(throwable);
            this.code = code;
        }
    }

    public static class ServerException extends RuntimeException {
        public int code;
        public String message;

        public ServerException(int code, String message){
            super();
            this.code = code;
            this.message = message;
        }
    }
}
