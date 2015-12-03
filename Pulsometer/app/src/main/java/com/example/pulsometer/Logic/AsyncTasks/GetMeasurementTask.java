package com.example.pulsometer.Logic.AsyncTasks;

import android.app.AlertDialog;
import android.os.AsyncTask;
import android.util.Log;

import com.example.pulsometer.AnalisysActivity;
import com.example.pulsometer.Logic.Extensions.GlobalVariables;
import com.example.pulsometer.Model.Pulse;
import com.example.pulsometer.Model.PulseSqlite;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * Created by Szymon Wójcik on 30.10.2015.
 */
public class GetMeasurementTask extends AsyncTask<Void, Void, HttpResponse> {

    private final Date date;
    private String accessToken;
    private AnalisysActivity activity;

    public GetMeasurementTask(String access_token, Date date, AnalisysActivity activity) {
        this.date = date;
        this.accessToken = access_token;
        this.activity = activity;
    }

    @Override
    protected HttpResponse doInBackground(Void... params) {
        try {
            //HttpParams httpParameters = new BasicHttpParams();
            //HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
            HttpClient client = new DefaultHttpClient(/*httpParameters*/);

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            JSONObject object = new JSONObject();
            object.put("date", dateFormat.format(this.date));

            String url = GlobalVariables.BaseUrlForRest + "api/GetMeasurementsWithDate?measurementDate=" + object.getString("date");
            System.out.println("Measurement " + url);
            HttpGet get = new HttpGet(url);
            get.addHeader("Authorization", "Bearer " + accessToken);
            HttpResponse response = client.execute(get);
            return response;
        } catch (Exception e) {
            Log.e("MainActivity", e.getMessage(), e);
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
                List<Pulse> temp = g.fromJson(writer.toString(), new TypeToken<Collection<Pulse>>(){}.getType());
                for (Pulse pulse : temp){
                    GlobalVariables.Pulses.add(pulse.PulseValue);
                    PulseSqlite p = new PulseSqlite(pulse.PulseValue, date);
                    p.save();
                }
                System.out.println("OK");
            }
            else {
                new AlertDialog.Builder(activity)
                        .setTitle("Error")
                        .setMessage("Measurement error\n" + result.getStatusLine().getStatusCode()
                                + "\n" + result.getStatusLine().getReasonPhrase())
                        .setPositiveButton("OK", null)
                        .show();
            }
        }catch(Exception e){
            //Log.e("GetMeasurementTask", e.getMessage(), e);
            new GetMeasurementTask(accessToken, date, activity).execute();
        }
    }
}
