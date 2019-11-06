package com.develop.http.demo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.develop.http.Request;
import com.develop.http.callback.HttpSimpleCallBack;
import com.develop.http.demo.api.HttpService;
import com.develop.http.demo.model.Pay;

import java.util.List;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.get_btn).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.get_btn:
                HttpService.get().getPayList(new HttpSimpleCallBack<List<Pay>>() {
                    @Override
                    public void onHttpSuccess(Request request, List<Pay> model) {
                        Toast.makeText(getBaseContext(), model == null ? "null" : model.size() + "", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onHttpFailed(Request request, int errorCode, String message) {
                        super.onHttpFailed(request, errorCode, message);
                        Toast.makeText(getBaseContext(), String.format("code=%d, message=%s", errorCode, message), Toast.LENGTH_LONG).show();
                    }
                });
                break;
        }
    }
}
