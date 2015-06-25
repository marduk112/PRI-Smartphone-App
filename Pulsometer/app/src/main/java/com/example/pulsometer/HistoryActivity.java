package com.example.pulsometer;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.pulsometer.Logic.AuthenticationData;
import com.example.pulsometer.Logic.GlobalVariables;
import com.example.pulsometer.Model.Date;
import com.example.pulsometer.Model.DateDTO;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.InputStream;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Created by Szymon Wójcik on 2015-06-24.
 */
public class HistoryActivity extends Activity implements AdapterView.OnItemClickListener {
    private AuthenticationData auth;
    private final Context context = this;
    private List<java.util.Date> history = new ArrayList<>();
    private ListView listView;
    private ArrayAdapter<java.util.Date> adapter;
    private List<DateDTO> temp;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        Intent intent = getIntent();
        auth = (AuthenticationData)intent.getSerializableExtra("authData");
        System.out.println("History Token" + auth.access_token);
        listView = (ListView) findViewById(R.id.historyListView);
        listView.setOnItemClickListener(this);
        new GetHistoryTask().execute();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Log.i("HistoryListView", "You clicked Item: " + id + " at position:" + position);
        Intent intent = new Intent();
        intent.setClass(this, AnalisysActivity.class);
        intent.putExtra("authData", auth);
        intent.putExtra("DBPosition", temp.get(position).Id);
        intent.putExtra("Measurement", history.get(position));
        startActivity(intent);
        finish();
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
                    System.out.println("History: \n" + writer.toString());
                    temp = g.fromJson(writer.toString(), new TypeToken<Collection<DateDTO>>(){}.getType());

                    //"yyyy-MM-dd'T'HH:mm:ss"
                    for (DateDTO date : temp)
                    {
                        String dateStr = date.MeasurementDate;
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                        history.add(sdf.parse(dateStr));
                    }
                    Collections.sort(history);
                    // Adapter: You need three parameters 'the context, id of the layout (it will be where the data is shown),
                    // and the array that contains the data
                    adapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_item, history);
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

        @Override
        protected void onCancelled() {

        }
    }
}
