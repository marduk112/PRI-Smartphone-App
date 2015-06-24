package com.example.pulsometer;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.pulsometer.Logic.AuthenticationData;
import com.example.pulsometer.Model.Date;
import com.example.pulsometer.Model.Pulse;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.InputStream;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Szymon Wójcik on 2015-06-24.
 */
public class HistoryActivity extends Activity {
    private AuthenticationData auth;
    private final Context context = this;
    private List<Date> history = new ArrayList<>();

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        Bundle extras = getIntent().getExtras();
        auth = (AuthenticationData)extras.get("authData");
    }

    private class GetHistoryTask extends AsyncTask<Void, Void, HttpResponse> {

        @Override
        protected HttpResponse doInBackground(Void... params) {
            try {
                HttpClient client = new DefaultHttpClient();
                String url = "http://pulsometerrest.apphb.com/api/GetMeasurementsDates";
                HttpGet get = new HttpGet(url);
                get.addHeader("Authorization", "Bearer " + auth.access_token);
                HttpResponse response = client.execute(get);
                return response;
            } catch (Exception e) {
                Log.e("HistoryActivity", e.getMessage(), e);
            }
            return null;
        }

        @Override
        protected void onPostExecute(HttpResponse result) {
            try {
                if (result.getStatusLine().getStatusCode() == 200){
                    HttpEntity entity = result.getEntity();
                    StatusLine status = result.getStatusLine();
                    InputStream in = entity.getContent();
                    StringWriter writer = new StringWriter();
                    IOUtils.copy(in, writer, "UTF-8");
                    Gson g = new Gson();
                    Date pulse = g.fromJson(writer.toString(), new TypeToken<List<Date>>(){}.getType());
                    System.out.println("OK");
                }
                else {
                    System.out.println("ERROR " + result.getStatusLine().getReasonPhrase() + result.getStatusLine().getStatusCode());
                }
            }catch(Exception e){
                Log.e("HistoryActivity", e.getMessage(), e);
            }
        }

        @Override
        protected void onCancelled() {

        }
    }
}
