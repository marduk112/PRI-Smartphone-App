package com.example.pulsometer;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.pulsometer.Logic.AsyncTasks.GetHistoryTask;
import com.example.pulsometer.Model.AuthenticationDataViewModel;
import com.example.pulsometer.Model.DateDTO;
import com.example.pulsometer.Model.Pulse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Szymon Wójcik on 2015-06-24.
 */
public class HistoryActivity extends Activity implements AdapterView.OnItemClickListener {
    private final Context context = this;
    private List<java.util.Date> history = new ArrayList<>();
    private ListView listView;
    private ArrayAdapter<java.util.Date> adapter;
    private List<DateDTO> temp;
    private AuthenticationDataViewModel auth;

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        listView = (ListView) findViewById(R.id.historyListView);
        listView.setOnItemClickListener(this);
        Bundle extras = getIntent().getExtras();
        auth = (AuthenticationDataViewModel)extras.get("authData");
        new GetHistoryTask(auth.access_token, temp, history, adapter, listView, this).execute();
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
}
