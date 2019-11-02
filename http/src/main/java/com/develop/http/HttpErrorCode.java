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
	public static final int CODE_TIMEOUT = 9002;

}
