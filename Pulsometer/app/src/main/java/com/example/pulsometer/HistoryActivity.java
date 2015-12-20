package com.example.pulsometer;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.pulsometer.Logic.AsyncTasks.GetHistoryTask;
import com.example.pulsometer.Logic.Extensions.DatePickerFragment;
import com.example.pulsometer.Logic.Extensions.GlobalVariables;
import com.example.pulsometer.Logic.Interfaces.AdapterListener;
import com.example.pulsometer.Model.AuthenticationDataViewModel;
import com.example.pulsometer.Model.DateDTO;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Szymon Wójcik on 2015-06-24.
 */
public class HistoryActivity extends FragmentActivity{
    private final HistoryActivity context = this;
    private static List<Date> history = new ArrayList<>();
    private static List<Date> historyClone = new ArrayList<>();
    private ListView listView;
    private static ArrayAdapter<java.util.Date> adapter;
    private List<DateDTO> temp;
    private AuthenticationDataViewModel auth;

    public static List<Date> getHistory() {
        return history;
    }

    public static List<Date> getHistoryClone() {
        return historyClone;
    }

    public static ArrayAdapter<Date> getAdapter() {
        return adapter;
    }

    public static void setHistory(Date date) {
        
    }

    /*@Override
    public void onBackPressed() {
        //super.onBackPressed();
        finish();
    }*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        history.clear();
        historyClone.clear();

        setContentView(R.layout.activity_history);
        listView = (ListView) findViewById(R.id.historyListView);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Log.i("HistoryListView", "You clicked Item: " + id + " at position:" + position);
                Intent intent = new Intent();
                intent.setClass(context, AnalisysActivity.class);
                intent.putExtra("authData", auth);
                intent.putExtra("Measurement", history.get(position));
                startActivity(intent);
            }
        });


        Bundle extras = getIntent().getExtras();
        auth = (AuthenticationDataViewModel)extras.get("authData");
        GetHistoryTask task = new GetHistoryTask(auth.access_token, temp, history, historyClone, adapter, listView, this);
        task.setAdapter(new AdapterListener() {
            @Override
            public void setAdapter(ArrayAdapter<Date> arrayAdapter) {
                adapter = arrayAdapter;
            }
        });
        task.execute();
    }

//    @Override
//    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//
//        //finish();
//    }

    public void showDatePickerDialog(View view) {
        System.out.println("adapter is null " + (adapter == null));
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }
}
