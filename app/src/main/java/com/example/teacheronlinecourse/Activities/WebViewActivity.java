package com.example.teacheronlinecourse.Activities;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.appcompat.app.AppCompatActivity;

import com.example.teacheronlinecourse.R;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class WebViewActivity extends AppCompatActivity {

    private WebView mWebView;
    private String pdf_file,url;
    boolean flag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        savedInstanceState=getIntent().getExtras();
        url=savedInstanceState.getString("url_file");
        flag=savedInstanceState.getBoolean("flag");


        initView();
        Action();
    }

    private void initView() {
        mWebView = (WebView) findViewById(R.id.webview);
    }
    private void Action(){

        mWebView.setWebViewClient(new MyBrowser(this));
        mWebView.getSettings().setLoadsImagesAutomatically(true);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setBuiltInZoomControls(true);
        mWebView.getSettings().setDisplayZoomControls(false);
        mWebView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);

        try {
            pdf_file= URLEncoder.encode(url,"UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        if (flag){
            mWebView.loadUrl(url);
        }else {
            mWebView.loadUrl("http://drive.google.com/viewerng/viewer?embedded=true&url=" + pdf_file);
        }
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
