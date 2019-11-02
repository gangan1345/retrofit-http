package com.develop.http.demo.api;

import com.develop.http.RetrofitHttp;
import com.develop.http.callback.HttpSimpleCallBack;
import com.develop.http.demo.model.Pay;

import java.util.List;
import java.util.Map;

/**
 * @author Angus
 */
public class HttpService {

    private static HttpService instance;
    private ApiService apiService;

    public static HttpService get(){
        synchronized (HttpService.class){
            if (instance == null) {
                instance = new HttpService();
            }
            return instance;
        }
    }

    private ApiService getApi(){
        if (apiService == null) {
            apiService = RetrofitHttp.get().getApi(ApiService.class);
        }
        return apiService;
    }

    public void addPay(Map map, HttpSimpleCallBack callBack) {
        RetrofitHttp.request(getApi().addPay(map), callBack);
    }

    public void getPayList(HttpSimpleCallBack<List<Pay>> callBack) {
        RetrofitHttp.request(getApi().getPayList(), callBack);
    }
}
