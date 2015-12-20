package com.example.pulsometer.Logic.AsyncTasks;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.pulsometer.Logic.Extensions.GlobalVariables;
import com.example.pulsometer.Logic.Interfaces.AdapterListenable;
import com.example.pulsometer.Logic.Interfaces.AdapterListener;
import com.example.pulsometer.Logic.Interfaces.ListListener;
import com.example.pulsometer.Logic.Interfaces.Listenable;
import com.example.pulsometer.Model.DateDTO;
import com.example.pulsometer.R;
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
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * Created by Szymon Wójcik on 30.10.2015.
 */
public class GetHistoryTask extends AsyncTask<Void, Void, HttpResponse> implements AdapterListenable {

    private List<DateDTO> temp;
    private List<Date> history;
    private static ArrayAdapter<Date> adapter;
    private ListView listView;
    private Context context;
    private String accessToken;
    private List<Date> historyClone;
    private AdapterListener adapterListener;

    public GetHistoryTask(String access_token, List<DateDTO> temp, List<Date> history, List<Date> historyClone, ArrayAdapter<Date> adapter, ListView listView, Context context) {
        this.temp=temp;
        this.history=history;
        this.adapter=adapter;
        this.listView=listView;
        this.context=context;
        this.accessToken = access_token;
        this.historyClone = historyClone;
    }

    @Override
    protected HttpResponse doInBackground(Void... params) {
        try {
            HttpClient client = new DefaultHttpClient();
            String url = GlobalVariables.BaseUrlForRest + "api/GetMeasurementsDates";
            HttpGet get = new HttpGet(url);
            get.addHeader("Authorization", "Bearer " + accessToken);
            System.out.println("History token " + accessToken);
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
                    historyClone.add(sdf.parse(dateStr));
                }
                Collections.sort(history);
                // Adapter: You need three parameters 'the context, id of the layout (it will be where the data is shown),
                // and the array that contains the data
                adapter = new ArrayAdapter<>(context, R.layout.date_row, history);
                listView.setAdapter(adapter);
                if (adapterListener != null)
                    adapterListener.setAdapter(adapter);

                System.out.println("OK");
                System.out.println("adapter is null " + (adapter == null));
            }
            else {
                System.out.println("ERROR " + result.getStatusLine().getReasonPhrase() + result.getStatusLine().getStatusCode());
            }
        }catch(Exception e){
            Log.e("HistoryActivity", e.getMessage(), e);
        }
    }

    @Override
    public void setAdapter(AdapterListener adapter) {
        this.adapterListener = adapter;
    }
}
