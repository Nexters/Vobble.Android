package com.nexters.vobble.activity;


import android.os.Bundle;
import android.webkit.WebView;
import com.nexters.vobble.network.URL;

public class EventActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initWebView();
    }

    private void initWebView() {
        WebView webview = new WebView(this);
        setContentView(webview);
        webview.loadUrl(URL.getBaseUrl() + "/events");
    }
}
