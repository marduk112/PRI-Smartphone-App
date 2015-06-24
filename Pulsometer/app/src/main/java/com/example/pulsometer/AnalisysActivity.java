package com.example.pulsometer;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.pulsometer.Logic.AuthenticationData;
import com.example.pulsometer.Logic.GlobalVariables;
import com.google.gson.Gson;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import org.apache.commons.io.IOUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ca.odell.glazedlists.BasicEventList;
import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.event.ListEvent;
import ca.odell.glazedlists.event.ListEventListener;

public class AnalisysActivity extends Activity {

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        auth = (AuthenticationData)intent.getSerializableExtra("authData");
        context = this;
        setContentView(R.layout.activity_analisys);
        graph = (GraphView) findViewById(R.id.graph);
        series = new LineGraphSeries<DataPoint>(new DataPoint[] {

        });
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

        graph.addSeries(series);
        GlobalVariables.Pulses.addListEventListener(new ChangeListEventListener());
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
                            new SendPulseTask(temp).execute();
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
                String url = "http://pulsometerrest.apphb.com/api/Pulses";
                HttpPost post = new HttpPost(url);
                post.addHeader("Authorization", "Bearer " + auth.access_token);
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
                if (result.getStatusLine().getStatusCode() == 201){
                    System.out.println("OK");
                }
                else {
                    System.out.println("ERROR " + result.getStatusLine().getReasonPhrase() + result.getStatusLine().getStatusCode());
                }
            }catch(Exception e){
                Log.e("AnalysisActivity", e.getMessage(), e);
            }
        }

        @Override
        protected void onCancelled() {

        }
    }
    private LineGraphSeries<DataPoint> series;
    private GraphView graph;
    private AuthenticationData auth;
    private Context context;
    private static double x = 0;
    private Date date = new Date();
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
