package com.develop.http.demo;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.develop.http.RetrofitHttp;
import com.develop.http.callback.HttpSimpleCallBack;
import com.develop.http.demo.model.Version;


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
//                HttpService.get().getPayList(new HttpSimpleCallBack<List<Pay>>() {
//                    @Override
//                    public void onSuccess(Request request, List<Pay> model) {
//                        Toast.makeText(getBaseContext(), model == null ? "null" : model.size() + "", Toast.LENGTH_LONG).show();
//                    }
//
//                    @Override
//                    public void onFailed(Request request, int errorCode, String message) {
//                        super.onHttpFailed(request, errorCode, message);
//                        Toast.makeText(getBaseContext(), String.format("code=%d, message=%s", errorCode, message), Toast.LENGTH_LONG).show();
//                    }
//                });

                RetrofitHttp.commonApi().getFullPath(this, "http://www.lebole5.com/systems/getVersionAndroid", new HttpSimpleCallBack<Version>() {
                    @Override
                    public void onSuccess(Version model) {
                        Toast.makeText(getBaseContext(), model == null ? "null" : model.version + "", Toast.LENGTH_LONG).show();
                    }
                });

//                RetrofitHttp.commonApi().download("http://file.gan.pub/test/201911/15/354032b5efd8499ba66073aed39c9629.png",
//                        Environment.getExternalStorageDirectory().getAbsolutePath() + "/A.png", new DownloadFileListener(){
//                            @Override
//                            public void onProgress(long progress, long total, boolean completed) {
//                                super.onProgress(progress, total, completed);
//                                LogUtils.i(String.format("progress=%s, total=%s", progress, total));
//                            }
//                        });
                break;
        }
    }
}
