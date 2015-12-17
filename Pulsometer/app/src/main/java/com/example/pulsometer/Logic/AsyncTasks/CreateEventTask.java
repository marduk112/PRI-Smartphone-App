package com.example.pulsometer.Logic.AsyncTasks;

import android.app.AlertDialog;
import android.os.AsyncTask;
import android.util.Log;

import com.example.pulsometer.AddEventActivity;
import com.example.pulsometer.Logic.Extensions.GlobalVariables;
import com.example.pulsometer.Model.EventViewModel;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Szymon Wójcik on 16.11.2015.
 */
public class CreateEventTask extends AsyncTask<Void, Void, HttpResponse> {
    private String accessToken;
    private EventViewModel eventViewModel;
    private AddEventActivity mActivity;

    public CreateEventTask(String accessToken, EventViewModel eventViewModel, AddEventActivity mActivity){
        this.accessToken = accessToken;
        this.eventViewModel = eventViewModel;
        this.mActivity = mActivity;
    }
    @Override
    protected HttpResponse doInBackground(Void... params) {
        try {
            HttpClient client = new DefaultHttpClient();
            String url = GlobalVariables.BaseUrlForRest + "api/Events";
            HttpPost post = new HttpPost(url);
            post.addHeader("Authorization", "Bearer " + accessToken);
            List<NameValuePair> nameValuePairs = new ArrayList<>();

            nameValuePairs.add(new BasicNameValuePair("Name", eventViewModel.Name));
            nameValuePairs.add(new BasicNameValuePair("Description", eventViewModel.Description));
            nameValuePairs.add(new BasicNameValuePair("StepsNumber", Integer.toString(eventViewModel.StepsNumber)));
            nameValuePairs.add(new BasicNameValuePair("StartDateTimeEvent", eventViewModel.StartDateTimeEvent));
            nameValuePairs.add(new BasicNameValuePair("EndDateTimeEvent", eventViewModel.EndDateTimeEvent));

            post.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
            HttpResponse response = client.execute(post);
            return response;
        } catch (Exception e) {
            Log.e("HistoryActivity", e.getMessage(), e);
        }
        return null;
    }

    @Override
    protected void onPostExecute(HttpResponse result) {
        try {
            StatusLine status = result.getStatusLine();
            if (result.getStatusLine().getStatusCode() == 201){
                new AlertDialog.Builder(mActivity)
                        .setTitle("")
                        .setMessage("Event was added")
                        .setPositiveButton("OK", null)
                        .show();
            }
            else {
                new AlertDialog.Builder(mActivity)
                        .setTitle("Error")
                        .setMessage(status.getReasonPhrase() + "\n" + status.getStatusCode())
                        .setPositiveButton("OK", null)
                        .show();
            }
        }catch(Exception e){
            Log.e("HistoryActivity", e.getMessage(), e);
        }
    }
}
