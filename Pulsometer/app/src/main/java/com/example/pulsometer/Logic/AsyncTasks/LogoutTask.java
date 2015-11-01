package com.example.pulsometer.Logic.AsyncTasks;

import android.os.AsyncTask;
import android.util.Log;

import com.example.pulsometer.Logic.Extensions.GlobalVariables;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

/**
 * Created by Szymon Wójcik on 30.10.2015.
 */
public class LogoutTask extends AsyncTask<Void, Void, HttpResponse> {

    @Override
    protected HttpResponse doInBackground(Void... params) {
        try {
            HttpClient client = new DefaultHttpClient();
            String url = GlobalVariables.BaseUrlForRest + "api/Account/Logout";
            HttpPost post = new HttpPost(url);
            post.addHeader("Authorization", "Bearer " + GlobalVariables.AccessToken);
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
                GlobalVariables.AccessToken = "";
            }
            else {
                System.out.println("ERROR " + result.getStatusLine().getReasonPhrase() + result.getStatusLine().getStatusCode());
            }
        }catch(Exception e){
            Log.e("HistoryActivity", e.getMessage(), e);
        }
    }
}
