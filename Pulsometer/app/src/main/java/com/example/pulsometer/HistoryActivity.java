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
import com.example.pulsometer.Model.AuthenticationDataViewModel;
import com.example.pulsometer.Model.DateDTO;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Szymon Wójcik on 2015-06-24.
 */
public class HistoryActivity extends FragmentActivity implements AdapterView.OnItemClickListener {
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

    public static void setHistory(List<Date> history) {
        HistoryActivity.history = history;
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
        listView.setOnItemClickListener(this);
        Bundle extras = getIntent().getExtras();
        auth = (AuthenticationDataViewModel)extras.get("authData");
        new GetHistoryTask(auth.access_token, temp, history, historyClone, adapter, listView, this).execute();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Log.i("HistoryListView", "You clicked Item: " + id + " at position:" + position);
        Intent intent = new Intent();
        intent.setClass(this, AnalisysActivity.class);
        intent.putExtra("authData", auth);
        intent.putExtra("Measurement", history.get(position));
        startActivity(intent);
        //finish();
    }

    public void showDatePickerDialog(View view) {
        System.out.println("adapter is null " + (adapter == null));
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }
}
