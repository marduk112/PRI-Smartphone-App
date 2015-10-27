package com.example.pulsometer;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.pulsometer.Logic.AsyncTasks.ExternalUserLoginTask;
import com.example.pulsometer.Logic.GlobalVariables;
import com.example.pulsometer.Model.AuthenticationDataViewModel;
import com.example.pulsometer.Model.UserInfoViewModel;
import com.google.gson.Gson;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;

import java.io.InputStream;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

public class LoginViaExternalProviderActivity extends Activity {

    private WebView webView;
    private final Context context = this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        webView = new WebView(this);
        //setContentView(R.layout.activity_login_via_google);
        setContentView(webView);
        //webView = (WebView) findViewById(R.id.webView);
        Bundle extras = getIntent().getExtras();
        String provider = extras.getString("provider");
        new ExternalUserLoginTask(provider, webView).execute();
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageFinished(WebView view, String url) {
                System.out.println("url " + url);
                String fieldName = "access_token=";
                if (url.contains(fieldName)) {
                    Integer accessTokenIndex = url.indexOf(fieldName);
                    Integer ampersandTokenIndex = url.indexOf("&", accessTokenIndex);
                    String tokenField = url.substring(accessTokenIndex, ampersandTokenIndex);
                    String token = tokenField.substring(fieldName.length());
                    new UserInfoTask(token).execute();
                }
            }
        });
    }

    private class UserInfoTask extends AsyncTask<Void, Void, HttpResponse> {

        private String token;
        public UserInfoTask(String token) {
            this.token = token;
        }

        @Override
        protected HttpResponse doInBackground(Void... params) {
            try {
                HttpClient client = new DefaultHttpClient();
                String url = GlobalVariables.BaseUrlForRest + "api/Account/UserInfo";
                HttpGet get = new HttpGet(url);
                get.addHeader("Authorization", "Bearer " + token);
                HttpResponse response = client.execute(get);
                return response;
            } catch (Exception e) {
                Log.e("Login via Google", e.getMessage(), e);
            }
            return null;
        }

        @Override
        protected void onPostExecute(HttpResponse result) {
            try {
                HttpEntity entity = result.getEntity();
                InputStream in = entity.getContent();
                StringWriter writer = new StringWriter();
                IOUtils.copy(in, writer, "UTF-8");
                Gson g = new Gson();
                UserInfoViewModel userInfoViewModel = g.fromJson(writer.toString(), UserInfoViewModel.class);
                if (!userInfoViewModel.HasRegistered) {
                    new RegisterUserTask(userInfoViewModel.Email, token).execute();
                }
                else {
                    GlobalVariables.AccessToken = token;
                }

            }catch(Exception e){
                Log.e("Login via Google", e.getMessage(), e);
            }
        }
    }
    private class RegisterUserTask extends AsyncTask<Void, Void, HttpResponse> {

        private String email, token;

        public RegisterUserTask(String email, String token){
            this.email = email;
            this.token = token;
        }
        @Override
        protected HttpResponse doInBackground(Void... params) {
            try {
                HttpClient client = new DefaultHttpClient();
                String url = GlobalVariables.BaseUrlForRest + "api/Account/RegisterExternal";
                HttpPost post = new HttpPost(url);
                post.addHeader("Authorization", "Bearer " + token);
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                //Email must be changed
                nameValuePairs.add(new BasicNameValuePair("Email", "szymon2wojcik@gmail.com"));
                post.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
                HttpResponse response = client.execute(post);
                return response;
            } catch (Exception e) {
                Log.e("Login via Google", e.getMessage(), e);
            }
            return null;
        }

        @Override
        protected void onPostExecute(HttpResponse result) {
            try {
                //HttpEntity entity = result.getEntity();
                int status = result.getStatusLine().getStatusCode();
                new AlertDialog.Builder(context)
                        .setTitle("Error")
                        .setMessage(/*"You're account hasn't been saved " +*/ status
                                + "\n" + result.getStatusLine().getReasonPhrase() + "\n" + result.getAllHeaders())
                        .setPositiveButton("OK", null)
                        .show();
                //if (status == 200) {

                //}
            }catch(Exception e){
                Log.e("Login via Google", e.getMessage(), e);
            }
        }
    }

}
