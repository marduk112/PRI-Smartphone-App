package com.example.pulsometer;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.activeandroid.query.Select;
import com.example.pulsometer.Logic.AsyncTasks.GetMeasurementTask;
import com.example.pulsometer.Logic.AsyncTasks.SendPulseTask;
import com.example.pulsometer.Logic.Extensions.GlobalVariables;
import com.example.pulsometer.Logic.Interfaces.ListListener;
import com.example.pulsometer.Logic.PulseAnalyse.AnalysePulse;
import com.example.pulsometer.Model.AuthenticationDataViewModel;
import com.example.pulsometer.Model.PulseSqlite;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.Date;
import java.util.List;

public class AnalisysActivity extends Activity {

    private Date MeasurementDate;
    public static boolean isGettingFromWatch = true;
    public static LineGraphSeries<DataPoint> series;
    public static GraphView graph;
    private AuthenticationDataViewModel auth;
    private Context context;
    private static double x = 0;
    private Date date;

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        /*if (series != null) {
            series.resetData(new DataPoint[]{});
        }
        if (graph != null) {
            graph.removeAllSeries();
        }*/
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analisys);

        graph = (GraphView) findViewById(R.id.graph);
        graph.removeAllSeries();
        graph.getViewport().setMinY(50);
        graph.getViewport().setMaxY(100);
        //graph.setBackground();
        graph.setBackgroundColor(getResources().getColor(android.R.color.white));
        /*graph.getViewport().setMinX(0);
        graph.getViewport().setMaxX(100);*/
        graph.getViewport().setYAxisBoundsManual(false);
        graph.getViewport().setXAxisBoundsManual(true);
        graph.getViewport().setScrollable(true);
        graph.getViewport().setScalable(true);
        series = new LineGraphSeries<>(new DataPoint[]{});
        series.resetData(new DataPoint[]{});
        graph.addSeries(series);
        GlobalVariables.Pulses.getList().clear();

        if (!GlobalVariables.Pulses.isSetListener()) {
            GlobalVariables.Pulses.setListener(new ListListener<Integer>() {
                @Override
                public void afterAdd(final Integer item) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            series.appendData(new DataPoint(x, item), true, 10000);
                            x += 0.25;
                            graph.getViewport().computeScroll();
                            if (isGettingFromWatch) {
                                PulseSqlite p = new PulseSqlite(item, date);
                                p.save();
                                new SendPulseTask(item, date, auth.access_token).execute();
                            }
                        }
                    });
                }
            });
        }
        date = new Date();
        Intent intent = getIntent();
        auth = (AuthenticationDataViewModel)intent.getSerializableExtra("authData");
        //series.resetData(new DataPoint[] {});
        if (intent.getSerializableExtra("Measurement") != null) {
            x=0;
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



    public void showAnalysisOnClick(View view) {
        if (!GlobalVariables.Pulses.getList().isEmpty()) {
            AnalysePulse analysePulse = new AnalysePulse(GlobalVariables.Pulses.getList(), 23, getResources());
            String result = analysePulse.analysePulse();
            new AlertDialog.Builder(this)
                    .setTitle("Analysis")
                    .setMessage(result)
                    .setPositiveButton("OK", null)
                    .show();
        }
        else {
            new AlertDialog.Builder(this)
                    .setTitle("Analysis")
                    .setMessage("No data")
                    .setPositiveButton("OK", null)
                    .show();
        }
    }

    public void refreshGraphOnClick(View view) {
        x=0;
        graph.removeAllSeries();
        series = new LineGraphSeries<>(new DataPoint[]{});
        graph.addSeries(series);
        x=0;
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
