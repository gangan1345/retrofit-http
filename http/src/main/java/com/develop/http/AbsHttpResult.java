package com.develop.http;

/**
 * 处理网络数据解析， app具体业务 可继承此类扩展
 * @author Angus
 */

public class AbsHttpResult<T> {
    /*
     * app 定义HttpResult 继承此类 如需要定义其他解析字段来 解析code msg data 只需如下定义即可
     *
     *     @Expose
     *     @SerializedName(value = "code", alternate = {"status"})
     *     protected Integer code;
     *     @Expose
     *     @SerializedName(value = "message")
     *     protected String message;
     *     @Expose
     *     @SerializedName(value = "data", alternate = {"result"})
     *     protected T data;
     *
     */

    protected transient Integer code;
    protected transient String message;
    protected transient T data;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public boolean isSuccess(){
        return code == HttpErrorCode.CODE_SUCCESS;
    }
}
