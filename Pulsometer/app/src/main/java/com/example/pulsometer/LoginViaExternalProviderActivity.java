package com.example.pulsometer;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.webkit.WebViewDatabase;
import android.widget.TextView;

import com.example.pulsometer.Logic.AsyncTasks.ExternalUserLoginTask;
import com.example.pulsometer.Logic.AsyncTasks.UserInfoTask;

public class LoginViaExternalProviderActivity extends Activity {

    private WebView webView;
    private final Context context = this;
    private final LoginViaExternalProviderActivity loginViaExternalProviderActivity = this;
    private boolean hasAccessToken = false;

    /*@Override
    public void onBackPressed() {
        //super.onBackPressed();
        finish();
    }*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        webView = new WebView(this);
        webView.clearCache(true);
        webView.clearFormData();
        webView.clearHistory();
        webView.clearMatches();
        webView.getSettings().setSaveFormData(false);
        setContentView(webView);
        webView.getSettings().setJavaScriptEnabled(true);

        Bundle extras = getIntent().getExtras();
        String provider = extras.getString("provider");
        new ExternalUserLoginTask(provider, webView).execute();
        webView.setWebViewClient(new WebViewClient() {

            @Override
            public void onPageFinished(WebView view, String url) {
                String fieldName = "access_token=";
                if (url.contains(fieldName) && !hasAccessToken) {
                    hasAccessToken = true;
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
