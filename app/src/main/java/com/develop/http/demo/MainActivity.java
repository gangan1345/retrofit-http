package com.develop.http.demo;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.develop.http.RetrofitHttp;
import com.develop.http.callback.HttpSimpleCallBack;
import com.develop.http.utils.LogUtils;

import java.util.HashMap;
import java.util.Map;


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

//                RetrofitHttp.commonApi().getFullPath(this, "http://www.lebole5.com/systems/getVersionAndroid", new HttpSimpleCallBack<Version>() {
//                    @Override
//                    public void onSuccess(Version model) {
//                        Toast.makeText(getBaseContext(), model == null ? "null" : model.version + "", Toast.LENGTH_LONG).show();
//                    }
//                });

//                RetrofitHttp.commonApi().download("http://file.gan.pub/test/201911/15/354032b5efd8499ba66073aed39c9629.png",
//                        Environment.getExternalStorageDirectory().getAbsolutePath() + "/A.png", new DownloadFileListener(){
//                            @Override
//                            public void onProgress(long progress, long total, boolean completed) {
//                                super.onProgress(progress, total, completed);
//                                LogUtils.i(String.format("progress=%s, total=%s", progress, total));
//                            }
//                        });

                // 截图
                View mImageView = getWindow().getDecorView();
                mImageView.setDrawingCacheEnabled(true);
                mImageView.buildDrawingCache(true);
                Bitmap cache = mImageView.getDrawingCache();
                Bitmap mViewBitmap = Bitmap.createBitmap(cache);
                mImageView.setDrawingCacheEnabled(false);

                Map<String, Object> map = new HashMap<>();
                map.put("appid", "ossm48g7u");
                map.put("appsecret", "63g06wg3");
                String fileName = System.currentTimeMillis() + ".jpg";
                RetrofitHttp.commonApi().uploadFileFullPath(getBaseContext(), "http://oss.gan.pub//oss/material/test/uploadMaterial", mViewBitmap,
                        fileName, fileName, map, new HttpSimpleCallBack() {
                            @Override
                            public void onSuccess(Object model) {

                            }

                            @Override
                            public void onProgress(long progress, long total, boolean completed) {
                                super.onProgress(progress, total, completed);
                                LogUtils.i(String.format("progress=%d, total=%d, completed=%s", progress, total, completed+""));
                            }
                        });
                break;
        }
    }
}
