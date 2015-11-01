package com.example.pulsometer.Logic.AsyncTasks;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import com.example.pulsometer.Logic.Extensions.GlobalVariables;
import com.example.pulsometer.LoginSuccessfullActivity;
import com.example.pulsometer.LoginViaExternalProviderActivity;
import com.example.pulsometer.Model.UserInfoViewModel;
import com.google.gson.Gson;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.InputStream;
import java.io.StringWriter;

/**
 * Created by Szymon Wójcik on 30.10.2015.
 */
public class UserInfoTask extends AsyncTask<Void, Void, HttpResponse> {

    private String token;
    private Context context;
    private LoginViaExternalProviderActivity loginViaExternalProviderActivity;

    public UserInfoTask(String token, Context context, LoginViaExternalProviderActivity loginViaExternalProviderActivity) {
        this.token = token;
        this.context = context;
        this.loginViaExternalProviderActivity = loginViaExternalProviderActivity;
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
                new RegisterExternalUserTask(userInfoViewModel.Email, token, context, loginViaExternalProviderActivity).execute();
            }
            else {
                GlobalVariables.AccessToken = token;
                Intent intent = new Intent(context, LoginSuccessfullActivity.class);
                context.startActivity(intent);
                loginViaExternalProviderActivity.finish();
            }

        }catch(Exception e){
            Log.e("Login via Google", e.getMessage(), e);
        }
    }
}
