package com.example.teacheronlinecourse.Activities;

import android.content.Context;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.teacheronlinecourse.Commans.Commans;
import com.example.teacheronlinecourse.R;

public class MyBrowser extends WebViewClient {

    Context context;

    public MyBrowser(Context context) {
        this.context = context;
        Commans.Prograss(context,context.getString(R.string.waiting));
        Commans.progressDialog.show();
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        // TODO Auto-generated method stub
        view.loadUrl(url);
        return true;
    }

    @Override
    public void onPageFinished(WebView view, String url) {
        // TODO Auto-generated method stub
        super.onPageFinished(view, url);
        Commans.progressDialog.dismiss();
    }
}