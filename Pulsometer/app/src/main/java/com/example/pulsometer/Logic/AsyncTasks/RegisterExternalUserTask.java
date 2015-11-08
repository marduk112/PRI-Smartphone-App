package com.example.pulsometer.Logic.AsyncTasks;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import com.example.pulsometer.ExternalProvidersActivity;
import com.example.pulsometer.Logic.Extensions.GlobalVariables;
import com.example.pulsometer.LoginViaExternalProviderActivity;
import com.example.pulsometer.Model.AuthenticationDataViewModel;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Szymon Wójcik on 30.10.2015.
 */
public class RegisterExternalUserTask extends AsyncTask<Void, Void, HttpResponse> {

    private String email, token;
    private Context context;
    private LoginViaExternalProviderActivity loginViaExternalProviderActivity;

    public RegisterExternalUserTask(String email, String token, Context context, LoginViaExternalProviderActivity loginViaExternalProviderActivity){
        this.email = email;
        this.token = token;
        this.context = context;
        this.loginViaExternalProviderActivity = loginViaExternalProviderActivity;
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
            nameValuePairs.add(new BasicNameValuePair("Email", email));
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
            Intent intent = new Intent(loginViaExternalProviderActivity, ExternalProvidersActivity.class);
            intent.putExtra("authData", new AuthenticationDataViewModel(token));
            loginViaExternalProviderActivity.startActivity(intent);
            //loginViaExternalProviderActivity.finish();
        }catch(Exception e){
            Log.e("Login via Google", e.getMessage(), e);
        }
    }
}
