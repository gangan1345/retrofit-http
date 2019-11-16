package com.develop.http;


/**
 * HTTP返回code基类
 * @author Angus
 */
public class HttpErrorCode {

	/** 成功 */
	public static final int CODE_SUCCESS = 0;
	/** 失败*/
	public static final int CODE_FAILURE = -1;

	/** 无网络*/
	public static final int CODE_NO_NETWORK = 9000;
	/** timeout */
	public static final int CODE_TIMEOUT = 9001;
	/** 解析异常 */
	public static final int CODE_PARSE_ERROR = 9002;


	public static String getCodeMessage(int code){
		String message = "未知错误";
		switch (code) {
			case CODE_NO_NETWORK:
				message = "无法连接服务器，请检查网络";
				break;
			case CODE_TIMEOUT:
				message = "网络连接超时，请稍后重试";
				break;
			case CODE_PARSE_ERROR:
				message = "数据解析错误，请稍后重试";
				break;
		}
		return message;
	}
}
