package com.example.pulsometer;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import com.example.pulsometer.Model.AuthenticationDataViewModel;
import com.example.pulsometer.Logic.GlobalVariables;
import com.example.pulsometer.Model.Pulse;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.InputStream;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.event.ListEvent;
import ca.odell.glazedlists.event.ListEventListener;

public class AnalisysActivity extends Activity {

    private Date MeasurementDate;
    private static boolean isGettingFromWatch = true;
    private int position;
    private LineGraphSeries<DataPoint> series;
    private GraphView graph;
    private AuthenticationDataViewModel auth;
    private Context context;
    private static double x = 0;
    private Date date;

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analisys);
        //x=0;
        date = new Date();
        GlobalVariables.Pulses.clear();
        Intent intent = getIntent();
        auth = (AuthenticationDataViewModel)intent.getSerializableExtra("authData");
        graph = (GraphView) findViewById(R.id.graph);
        graph.getViewport().setMinY(50);
        graph.getViewport().setMaxY(100);
        //graph.setBackground();
        graph.setBackgroundColor(getResources().getColor(android.R.color.white));
        /*graph.getViewport().setMinX(0);
        graph.getViewport().setMaxX(100);*/
        graph.getViewport().setYAxisBoundsManual(true);
        graph.getViewport().setXAxisBoundsManual(true);
        graph.getViewport().setScrollable(true);
        graph.getViewport().setScalable(true);
        series = new LineGraphSeries<DataPoint>(new DataPoint[] {

        });
        graph.removeAllSeries();
        graph.addSeries(series);
        if (intent.getSerializableExtra("Measurement") != null) {
            isGettingFromWatch = false;
            MeasurementDate = (Date) intent.getSerializableExtra("Measurement");
            position = intent.getIntExtra("DBPosition", 0);
            System.out.println("MeasurementDate: " + MeasurementDate);
            intent.removeExtra("Measurement");
            new GetMeasurementTask(MeasurementDate, position).execute();
        }

        context = this;
        GlobalVariables.Pulses.addListEventListener(new ChangeListEventListener());
        System.out.println("On create");
    }
    public class ChangeListEventListener implements ListEventListener<Integer> {
        @Override
        public void listChanged(ListEvent<Integer> listChanges) {
            EventList<Integer> tempList = listChanges.getSourceList();
            for (final Integer temp : tempList) {
                if (temp > 0) {
                    System.out.println("Event pulses " + temp);
                    ((Activity)context).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //TextView t = (TextView)findViewById(R.id.textView2);
                            //t.append("Pulse: " + temp + "\n");
                            series.appendData(new DataPoint(x, temp), true, 10000);
                            x += 0.25;
                            graph.getViewport().computeScroll();
                            if (isGettingFromWatch) {
                                new SendPulseTask(temp).execute();
                            }
                        }
                    });
                    //
                    //
                //    listChanges.next();
                }
            }
        }
    }
    private class SendPulseTask extends AsyncTask<Void, Void, HttpResponse> {

        private final Integer pulse;

        SendPulseTask(Integer pulse) {
            this.pulse = pulse;
        }

        @Override
        protected HttpResponse doInBackground(Void... params) {
            try {
                HttpClient client = new DefaultHttpClient();
                String url = GlobalVariables.BaseUrlForRest + "api/Pulses";
                HttpPost post = new HttpPost(url);
                post.addHeader("Authorization", "Bearer " + GlobalVariables.AccessToken);
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("PulseValue", pulse.toString()));
                android.text.format.DateFormat df = new android.text.format.DateFormat();
                String newDate = df.format("yyyy-MM-dd'T'HH:mm:ss", date).toString();
                System.out.println("Data " + newDate);
                nameValuePairs.add(new BasicNameValuePair("DateCreated", newDate));
                post.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
                HttpResponse response = client.execute(post);
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
                    System.out.println("OK");
                    this.onCancelled();
                }
                else {
                    System.out.println("ERROR " + result.getStatusLine().getReasonPhrase() + result.getStatusLine().getStatusCode());
                }
            }catch(Exception e){
                Log.e("AnalysisActivity", e.getMessage(), e);
            }
        }
    }

    private class GetMeasurementTask extends AsyncTask<Void, Void, HttpResponse> {

        private final Date date;
        private int position;
        private int timeoutConnection = 10000;

        GetMeasurementTask(Date date, int position) {
            this.date = date;
            this.position = position;
            isGettingFromWatch = false;
        }

        @Override
        protected HttpResponse doInBackground(Void... params) {
            try {
                //HttpParams httpParameters = new BasicHttpParams();
                //HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
                HttpClient client = new DefaultHttpClient(/*httpParameters*/);
                String url = GlobalVariables.BaseUrlForRest + "api/GetMeasurements/";
                HttpPost post = new HttpPost(url);
                post.addHeader("Authorization", "Bearer " + GlobalVariables.AccessToken);
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

                android.text.format.DateFormat df = new android.text.format.DateFormat();
                String newDate = df.format("yyyy-MM-dd'T'HH:mm:ss", this.date).toString();
                nameValuePairs.add(new BasicNameValuePair("MeasurementDate", newDate));

                post.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
                HttpResponse response = client.execute(post);
                return response;
            } catch (Exception e) {
                Log.e("MainActivity", e.getMessage(), e);
            }
            return null;
        }

        @Override
        protected void onPostExecute(HttpResponse result) {
            try {
                //if (result.getStatusLine().getStatusCode() == 200){
                    HttpEntity entity = result.getEntity();
                    StatusLine status = result.getStatusLine();
                    InputStream in = entity.getContent();
                    StringWriter writer = new StringWriter();
                    IOUtils.copy(in, writer, "UTF-8");
                    Gson g = new Gson();
                    System.out.println("History: \n" + writer.toString());
                    List<Pulse> temp = g.fromJson(writer.toString(), new TypeToken<Collection<Pulse>>(){}.getType());
                    for (Pulse pulse : temp){
                        GlobalVariables.Pulses.add(pulse.PulseValue);
                    }
                    System.out.println("OK");
                //}
                //else {

                //}
            }catch(Exception e){
                Log.e("GetMeasurementTask", e.getMessage(), e);
            }
        }

        @Override
        protected void onCancelled() {

        }
    }

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_analisys, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }*/
}
