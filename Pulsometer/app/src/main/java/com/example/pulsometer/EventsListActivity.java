package com.example.pulsometer;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.pulsometer.Logic.AsyncTasks.CreateEventTask;
import com.example.pulsometer.Logic.AsyncTasks.GetAllEventsTask;
import com.example.pulsometer.Logic.AsyncTasks.JoinToEventTask;
import com.example.pulsometer.Model.AuthenticationDataViewModel;
import com.example.pulsometer.Model.EventViewModel;
import com.example.pulsometer.R;

import java.util.ArrayList;
import java.util.List;

public class EventsListActivity extends Activity implements AdapterView.OnItemClickListener {

    private ListView eventsListView;
    private static List<EventViewModel> eventViewModelList;
    private AuthenticationDataViewModel auth;
    private EventsListActivity mActivity = this;

    public static void setEventViewModelList(List<EventViewModel> eventViewModelList) {
        EventsListActivity.eventViewModelList = eventViewModelList;
    }

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
        final EventViewModel eventViewModel = eventViewModelList.get(position);
        new AlertDialog.Builder(this)
                .setTitle("Event")
                //To strings.xml
                .setMessage("Name " + eventViewModel.Name
                        + "\nDescription " + eventViewModel.Description
                        + "\nNumber of steps " + eventViewModel.StepsNumber
                        + "\nStart date " + eventViewModel.StartDateTimeEvent
                        + "\nEnd date " + eventViewModel.EndDateTimeEvent)
                .setPositiveButton("Join to event", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        eventViewModel.save();
                        new JoinToEventTask(auth.access_token, eventViewModel.Id, mActivity).execute();
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }
}
