package com.example.pulsometer.Logic.AsyncTasks;

import android.app.AlertDialog;
import android.os.AsyncTask;
import android.util.Log;

import com.example.pulsometer.AddEventActivity;
import com.example.pulsometer.EventsListActivity;
import com.example.pulsometer.Logic.Extensions.GlobalVariables;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

/**
 * Created by Szymon Wójcik on 16.11.2015.
 */
public class JoinToEventTask extends AsyncTask<Void, Void, HttpResponse> {
    private String accessToken;
    private int eventId;
    private EventsListActivity mActivity;

    public JoinToEventTask(String accessToken, int eventId, EventsListActivity mActivity) {
        this.accessToken = accessToken;
        this.eventId = eventId;
        this.mActivity = mActivity;
    }

    @Override
    protected HttpResponse doInBackground(Void... params) {
        try {
            HttpClient client = new DefaultHttpClient();
            String url = GlobalVariables.BaseUrlForRest + "api/JoinToEvent?id=" + eventId;
            HttpPost post = new HttpPost(url);
            post.addHeader("Authorization", "Bearer " + accessToken);
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
            if (result.getStatusLine().getStatusCode() == 200){
                System.out.println("OK");
                new AlertDialog.Builder(mActivity)
                        .setTitle("")
                        .setMessage("You joined to event with id " + eventId)
                        .setPositiveButton("OK", null)
                        .show();
            }
            else {
                System.out.println("ERROR " + result.getStatusLine().getReasonPhrase() + result.getStatusLine().getStatusCode());
                new AlertDialog.Builder(mActivity)
                        .setTitle("")
                        .setMessage("Error " + result.getStatusLine().getReasonPhrase() + result.getStatusLine().getStatusCode())
                        .setPositiveButton("OK", null)
                        .show();
            }
        } catch(Exception e){
            Log.e("HistoryActivity", e.getMessage(), e);
        }
    }
}
