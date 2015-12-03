package com.example.pulsometer.Logic.AsyncTasks;

import android.os.AsyncTask;
import android.util.Log;

import com.example.pulsometer.Logic.Extensions.GlobalVariables;
import com.example.pulsometer.Model.Pulse;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Szymon Wójcik on 30.10.2015.
 */
public class SendPulseTask extends AsyncTask<Void, Void, HttpResponse> {

    private final Integer pulse;
    private Date date;
    private String accessToken;

    public SendPulseTask(Integer pulse, Date date, String accessToken) {
        this.pulse = pulse;
        this.date = date;
        this.accessToken = accessToken;
    }

    @Override
    protected HttpResponse doInBackground(Void... params) {
        try {
            HttpClient client = new DefaultHttpClient();
            String url = GlobalVariables.BaseUrlForRest + "api/Pulses";
            HttpPost post = new HttpPost(url);
            post.addHeader("Authorization", "Bearer " + accessToken);
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("PulseValue", pulse.toString()));
            android.text.format.DateFormat df = new android.text.format.DateFormat();
            String newDate = df.format("yyyy-MM-dd'T'HH:mm:ss", date).toString();
            nameValuePairs.add(new BasicNameValuePair("DateCreated", newDate));
            post.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
            HttpResponse response = client.execute(post);
            return response;
        } catch (Exception e) {
            Log.e("MainActivity", e.getMessage(), e);
        }
        return null;
    }

    /*@Override
    protected void onPostExecute(HttpResponse result) {
        try {
            if (result.getStatusLine().getStatusCode() == 201){
                System.out.println("OK");
            }
            else {
                System.out.println("ERROR " + result.getStatusLine().getReasonPhrase() + result.getStatusLine().getStatusCode());
            }
        }catch(Exception e){
            Log.e("AnalysisActivity", e.getMessage(), e);
        }
    }*/
}
