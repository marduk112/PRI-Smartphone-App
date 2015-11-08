package com.example.pulsometer;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.activeandroid.query.Select;
import com.example.pulsometer.Logic.AsyncTasks.GetMeasurementTask;
import com.example.pulsometer.Logic.AsyncTasks.SendPulseTask;
import com.example.pulsometer.Logic.Extensions.GlobalVariables;
import com.example.pulsometer.Logic.Interfaces.ListListener;
import com.example.pulsometer.Model.AuthenticationDataViewModel;
import com.example.pulsometer.Model.Pulse;
import com.example.pulsometer.Model.PulseSqlite;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.Date;
import java.util.List;

public class AnalisysActivity extends Activity {

    private Date MeasurementDate;
    public static boolean isGettingFromWatch = true;
    public static LineGraphSeries<DataPoint> series = new LineGraphSeries<DataPoint>(new DataPoint[] {});
    public static GraphView graph;
    private AuthenticationDataViewModel auth;
    private Context context;
    public static double x = 0;
    private Date date;

    /*@Override
    public void onBackPressed() {
        //super.onBackPressed();
        finish();
    }*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        GlobalVariables.Pulses.setListener(new ListListener<Integer>() {
            @Override
            public void afterAdd(Integer item) {
                series.appendData(new DataPoint(x, item), true, 10000);
                x += 0.25;
                graph.getViewport().computeScroll();
                if (isGettingFromWatch) {
                    PulseSqlite p = new PulseSqlite(item, date);
                    p.save();
                    new SendPulseTask(item, date).execute();
                }
            }
        });

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
        graph.getViewport().setYAxisBoundsManual(false);
        graph.getViewport().setXAxisBoundsManual(true);
        graph.getViewport().setScrollable(true);
        graph.getViewport().setScalable(false);
        graph.removeAllSeries();

        graph.addSeries(series);
        if (intent.getSerializableExtra("Measurement") != null) {
            isGettingFromWatch = false;
            MeasurementDate = (Date) intent.getSerializableExtra("Measurement");
            boolean isOffline = new Select().from(PulseSqlite.class).where("date = ?", MeasurementDate).exists();
            if (!isOffline)
                new GetMeasurementTask(auth.access_token, MeasurementDate, this).execute();
            else {
                System.out.println("offline");
                List<PulseSqlite> pulses = new Select().from(PulseSqlite.class).where("date = ?", MeasurementDate).execute();
                for (PulseSqlite p : pulses){
                    GlobalVariables.Pulses.add(p.PulseValue);
                }
            }
        }

        context = this;
        System.out.println("On create");
        graph.getViewport().computeScroll();
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
