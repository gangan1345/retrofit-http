package com.develop.http.callback;

import com.develop.http.AbsHttpExceptionHandle;

/**
 * @author Angus
 */
public interface HttpHandleExceptionListener {

    /**
     * 自定义解析网络异常
     * @param e
     * @return
     */
    AbsHttpExceptionHandle.ResponeThrowable handleException(Throwable e);

    /**
     * 默认错误提示
     * 网络异常，请稍后重试
     * @return
     */
    String getDefaultErrorMessage();

    /**
     * String message = "未知错误";
     * 			case CODE_NO_NETWORK:
     * 				message = "无法连接服务器，请检查网络";
     * 				break;
     * 			case CODE_TIMEOUT:
     * 				message = "网络连接超时，请稍后重试";
     * 				break;
     * 			case CODE_PARSE_ERROR:
     * 				message = "数据解析错误，请稍后重试";
     * 				break;
     * @param code
     * @return
     */
    String getCodeMessage(int code);
}
