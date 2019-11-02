package com.develop.http.demo.api.common;

import com.develop.http.AbsHttpResult;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @author Angus
 */
public class HttpResult<T> extends AbsHttpResult<T> {
    @Expose
    @SerializedName(value = "code")
    protected Integer code;
    @Expose
    @SerializedName(value = "message")
    protected String message;
    @Expose
    @SerializedName(value = "data")
    protected T data;

    @Override
    public Integer getCode() {
        return code;
    }

    @Override
    public void setCode(Integer code) {
        this.code = code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public T getData() {
        return data;
    }

    @Override
    public void setData(T data) {
        this.data = data;
    }

    public boolean isSuccess(){
        return code == 1;
    }
}