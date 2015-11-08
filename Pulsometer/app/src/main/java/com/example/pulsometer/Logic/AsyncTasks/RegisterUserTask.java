package com.example.pulsometer.Logic.AsyncTasks;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;
import android.view.View;

import com.example.pulsometer.Logic.Extensions.GlobalVariables;
import com.example.pulsometer.RegisterActivity;

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
public class RegisterUserTask extends AsyncTask<Void, Void, HttpResponse> {

    private final String mEmail;
    private final String mPassword;
    private final String mConfirmPassword;
    private  RegisterUserTask authTask;
    private RegisterActivity registerActivity;
    private View registerFormView;
    private View progressView;
    private Resources resources;

    public RegisterUserTask(String email, String password, String confirmPassword, RegisterUserTask authTask, RegisterActivity registerActivity, View registerFormView, View progressView, Resources resources) {
        mEmail = email;
        mPassword = password;
        mConfirmPassword = confirmPassword;
    }

    @Override
    protected HttpResponse doInBackground(Void... params) {
        try {
            HttpClient client = new DefaultHttpClient();
            String url = GlobalVariables.BaseUrlForRest + "api/Account/Register";
            HttpPost post = new HttpPost(url);
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("Email", mEmail));
            nameValuePairs.add(new BasicNameValuePair("Password", mPassword));
            nameValuePairs.add(new BasicNameValuePair("ConfirmPassword", mConfirmPassword));
            post.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
            HttpResponse response = client.execute(post);
            return response;
        } catch (Exception e) {
            Log.e("MainActivity", e.getMessage(), e);
        }
        return null;
    }

    @Override
    protected void onPostExecute(final HttpResponse success) {


        if (success.getStatusLine().getStatusCode() == 200) {
            registerActivity.finish();
        } else {
            new AlertDialog.Builder(registerActivity)
                    .setTitle("Error")
                    .setMessage(success.getStatusLine().getReasonPhrase())
                    .setPositiveButton("OK", null)
                    .show();
        }
    }
}
