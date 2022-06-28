package com.example.mytvdemo.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.mytvdemo.R;

public class WebActivity extends FragmentActivity {

    private static final String TAG = WebActivity.class.getSimpleName();
    ProgressDialog progressDialog = null;
    WebView webView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        final boolean[] isRedirected = {false};
        webView = findViewById(R.id.webView_id);
        webView.loadUrl("https://blog.csdn.net/itmop/article/details/83714957");
        webView.setWebViewClient(new WebViewClient(){

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                //使用WebView加载显示url
                view.loadUrl(url);
                //返回true
                return true;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {


                super.onPageStarted(view, url, favicon);

                if (!isRedirected[0]) {
                    Log.d(TAG, "onPageStarted: 发生了网页加载");
                    //Do something you want when starts loading
                    if (progressDialog == null) {
                        progressDialog = new ProgressDialog(WebActivity.this, ProgressDialog.STYLE_SPINNER);
                        progressDialog.setCancelable(false);//设置点击不消失
                    }
                    if (progressDialog.isShowing()) {
                        progressDialog.setMessage("message");
                    } else {
                        progressDialog.setMessage("message");
                        progressDialog.show();
                    }



                }

                isRedirected[0] = false;



            }


            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                isRedirected[0] = true;
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);

                if (!isRedirected[0]) {
                    if(progressDialog != null && progressDialog.isShowing()){
                        progressDialog.dismiss();
                    }
                    //Do something you want when finished loading
                }

            }
        });
        WebSettings webSettings = webView.getSettings();





    }
}