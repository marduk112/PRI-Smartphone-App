package com.example.pulsometer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.pulsometer.Logic.AsyncTasks.GetAllEventsTask;
import com.example.pulsometer.Model.AuthenticationDataViewModel;
import com.example.pulsometer.Model.EventViewModel;
import com.example.pulsometer.R;

import java.util.ArrayList;
import java.util.List;

public class EventsListActivity extends Activity implements AdapterView.OnItemClickListener {

    private ListView eventsListView;
    private List<EventViewModel> eventViewModelList = new ArrayList<>();
    private AuthenticationDataViewModel auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events_list);

        Intent intent = getIntent();
        auth = (AuthenticationDataViewModel)intent.getSerializableExtra("authData");

        eventsListView = (ListView) findViewById(R.id.eventsList);
        eventsListView.setOnItemClickListener(this);

        new GetAllEventsTask(auth.access_token, eventsListView, eventViewModelList, this).execute();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(this, AddEventActivity.class);
        intent.putExtra("event", eventViewModelList.get(position));
        intent.putExtra("readOnly", true);
        startActivity(intent);
    }
}
