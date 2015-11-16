package com.example.pulsometer.Logic.AsyncTasks;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.pulsometer.Logic.Extensions.GlobalVariables;
import com.example.pulsometer.Model.DateDTO;
import com.example.pulsometer.Model.EventViewModel;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.InputStream;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * Created by Szymon Wójcik on 16.11.2015.
 */
public class GetAllEventsTask extends AsyncTask<Void, Void, HttpResponse> {
    private ListView listView;
    private String accessToken;
    private List<EventViewModel> eventViewModelList;
    private Context context;

    public GetAllEventsTask(String access_token, ListView listView, List<EventViewModel> eventViewModelList, Context context) {
        this.listView = listView;
        this.accessToken = access_token;
        this.eventViewModelList = eventViewModelList;
        this.context = context;
    }

    @Override
    protected HttpResponse doInBackground(Void... params) {
        try {
            HttpClient client = new DefaultHttpClient();
            String url = GlobalVariables.BaseUrlForRest + "api/Events";
            HttpGet get = new HttpGet(url);
            get.addHeader("Authorization", "Bearer " + accessToken);
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
                List<String> temp = new ArrayList<>();

                HttpEntity entity = result.getEntity();
                InputStream in = entity.getContent();
                StringWriter writer = new StringWriter();
                IOUtils.copy(in, writer, "UTF-8");
                Gson g = new Gson();
                eventViewModelList = g.fromJson(writer.toString(),
                        new TypeToken<Collection<EventViewModel>>(){}.getType());

                for (EventViewModel eventViewModel : eventViewModelList) {
                    temp.add(eventViewModel.Name);
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, temp);
                listView.setAdapter(adapter);
                System.out.println("OK");
            }
            else {
                System.out.println("ERROR " + result.getStatusLine().getReasonPhrase() + result.getStatusLine().getStatusCode());
            }
        }catch(Exception e){
            Log.e("HistoryActivity", e.getMessage(), e);
        }
    }
}
