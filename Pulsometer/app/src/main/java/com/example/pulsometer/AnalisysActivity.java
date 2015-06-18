package com.example.pulsometer;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.pulsometer.Logic.GlobalVariables;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.io.IOException;

import ca.odell.glazedlists.BasicEventList;
import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.event.ListEvent;
import ca.odell.glazedlists.event.ListEventListener;

public class AnalisysActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        setContentView(R.layout.activity_analisys);
        graph = (GraphView) findViewById(R.id.graph);
        series = new LineGraphSeries<DataPoint>(new DataPoint[] {

        });
        graph.getViewport().setMinY(50);
        graph.getViewport().setMaxY(100);
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
                        }
                    });
                    //
                    //
                //    listChanges.next();
                }
            }
        }
    }
    private LineGraphSeries<DataPoint> series;
    private GraphView graph;
    private Context context;
    private static double x = 0;
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
