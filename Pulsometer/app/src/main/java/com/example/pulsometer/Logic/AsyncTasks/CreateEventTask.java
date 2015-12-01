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

            //System.out.println("Name des " + eventViewModel.Name);
            nameValuePairs.add(new BasicNameValuePair("Name", eventViewModel.Name));
            nameValuePairs.add(new BasicNameValuePair("Description", eventViewModel.Description));
            //System.out.println("Min = " + eventViewModel.Min.toString());
            nameValuePairs.add(new BasicNameValuePair("Min", Integer.toString(eventViewModel.Min)));
            nameValuePairs.add(new BasicNameValuePair("Max", Integer.toString(eventViewModel.Max)));
            System.out.println("event date " + eventViewModel.StartDateTimeEvent);
            nameValuePairs.add(new BasicNameValuePair("StartDateTimeEvent", eventViewModel.StartDateTimeEvent));
            nameValuePairs.add(new BasicNameValuePair("EventDuration", Integer.toString(eventViewModel.EventDuration)));
            nameValuePairs.add(new BasicNameValuePair("Duration", Integer.toString(eventViewModel.Duration)));
            //if (eventViewModel.Min != null && eventViewModel.Max != null)
                nameValuePairs.add(new BasicNameValuePair("Target", "2"));
            /*else if (eventViewModel.Max != null)
                nameValuePairs.add(new BasicNameValuePair("Target", "1"));
            else
                nameValuePairs.add(new BasicNameValuePair("Target", "0"));*/

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
