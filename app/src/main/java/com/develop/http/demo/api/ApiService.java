package com.develop.http.demo.api;


import com.develop.http.demo.api.common.HttpResult;
import com.develop.http.demo.model.Pay;

import java.util.List;
import java.util.Map;

import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import rx.Observable;

/**
 * @author Angus
 */
public interface ApiService {

    @POST("pay/add")
    Observable<HttpResult> addPay(@Body Map map);

    @GET("pay/list")
    Observable<HttpResult<List<Pay>>> getPayList();
}
