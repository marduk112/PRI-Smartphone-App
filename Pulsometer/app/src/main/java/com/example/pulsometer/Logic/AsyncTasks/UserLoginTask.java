package com.example.pulsometer.Logic.AsyncTasks;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.example.pulsometer.Logic.Extensions.GlobalVariables;
import com.example.pulsometer.LoginActivity;
import com.example.pulsometer.LoginSuccessfullActivity;
import com.example.pulsometer.Model.AuthenticationDataViewModel;
import com.google.gson.Gson;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.InputStream;
import java.io.StringWriter;
import java.security.spec.ECField;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Szymon Wójcik on 30.10.2015.
 */
public class UserLoginTask extends AsyncTask<Void, Void, HttpResponse> {

    private final String mEmail;
    private final String mPassword;
    private final LoginActivity mActivity;
    private Resources resources;
    private UserLoginTask mAuthTask;
    private View mLoginFormView;
    private View mProgressView;
    private ProgressBar progressBar;
    private static int prob = 0;

    public UserLoginTask(String email, String password, LoginActivity temp, ProgressBar progressBar) {
        mEmail = email;
        mPassword = password;
        mActivity = temp;
        this.progressBar = progressBar;
    }

    @Override
    protected void onPreExecute() {
        progressBar.setVisibility(View.VISIBLE);
        LoginActivity.setVisibility(View.INVISIBLE);
    }

    @Override
    protected HttpResponse doInBackground(Void... params) {
        try {
            HttpClient client = new DefaultHttpClient();
            String url = GlobalVariables.BaseUrlForRest + "Token";
            HttpPost post = new HttpPost(url);
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("grant_type", "password"));
            nameValuePairs.add(new BasicNameValuePair("username", mEmail));
            nameValuePairs.add(new BasicNameValuePair("password", mPassword));
            post.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
            HttpResponse response = client.execute(post);
            return response;
        } catch (Exception e) {
            Log.e("Login", e.getMessage(), e);
        }
        return null;
    }

    @Override
    protected void onPostExecute(HttpResponse result) {
        try {
            prob++;
            if (prob == 10) {
                prob=0;
                new AlertDialog.Builder(mActivity)
                        .setTitle("Error")
                        .setMessage("Problem with connection")
                        .setPositiveButton("OK", null)
                        .show();
                progressBar.setVisibility(View.INVISIBLE);
                LoginActivity.setVisibility(View.VISIBLE);
            } else {
                progressBar.setVisibility(View.INVISIBLE);
                LoginActivity.setVisibility(View.VISIBLE);
                StatusLine status = result.getStatusLine();
                HttpEntity entity = result.getEntity();
                InputStream in = entity.getContent();
                StringWriter writer = new StringWriter();
                if (status.getStatusCode() == 200) {
                    IOUtils.copy(in, writer, "UTF-8");
                    Gson g = new Gson();
                    AuthenticationDataViewModel auth = g.fromJson(writer.toString(), AuthenticationDataViewModel.class);
                    Intent intent = new Intent(mActivity, LoginSuccessfullActivity.class);
                    intent.putExtra("authData", auth);
                    mActivity.startActivity(intent);
                    //mActivity.finish();
                } else {
                    new AlertDialog.Builder(mActivity)
                            .setTitle("Error")
                            .setMessage("Authentication error\n" + status.getReasonPhrase() + " " + status.getStatusCode())
                            .setPositiveButton("OK", null)
                            .show();
                }
            }
        }catch(Exception e){
            Log.e("Login", e.getMessage(), e);
            new UserLoginTask(mEmail, mPassword, mActivity, progressBar).execute();
        }
    }

    //@Override
    /*protected void onCancelled() {
        mAuthTask = null;
    }*/
}
