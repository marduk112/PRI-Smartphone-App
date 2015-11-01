package com.example.pulsometer;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.example.pulsometer.Logic.AsyncTasks.ExternalUserLoginTask;
import com.example.pulsometer.Logic.AsyncTasks.UserInfoTask;

public class LoginViaExternalProviderActivity extends Activity {

    private WebView webView;
    private final Context context = this;
    private final LoginViaExternalProviderActivity loginViaExternalProviderActivity = this;

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        finish();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        webView = new WebView(this);
        setContentView(webView);

        Bundle extras = getIntent().getExtras();
        String provider = extras.getString("provider");
        webView.getSettings().setJavaScriptEnabled(true);
        new ExternalUserLoginTask(provider, webView).execute();
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                System.out.println("url " + url);
                String fieldName = "access_token=";
                if (url.contains(fieldName)) {
                    webView.setVisibility(View.INVISIBLE);
                    Integer accessTokenIndex = url.indexOf(fieldName);
                    Integer ampersandTokenIndex = url.indexOf("&", accessTokenIndex);
                    String tokenField = url.substring(accessTokenIndex, ampersandTokenIndex);
                    String token = tokenField.substring(fieldName.length());
                    new UserInfoTask(token, context, loginViaExternalProviderActivity).execute();
                }
            }
        });
    }
}
