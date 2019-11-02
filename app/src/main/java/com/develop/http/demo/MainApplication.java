package com.develop.http.demo;

import android.app.Application;

import com.develop.http.RetrofitHttp;
import com.develop.http.demo.api.common.HttpParams;

/**
 * @author Angus
 */
public class MainApplication extends Application {

    private static MainApplication instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;

        RetrofitHttp.init(this, new HttpParams());
    }

    public static MainApplication getInstance() {
        return instance;
    }
}
