package com.example.pulsometer;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;

import com.example.pulsometer.Logic.AsyncTasks.GetAllEventsTask;
import com.example.pulsometer.Model.AuthenticationDataViewModel;
import com.example.pulsometer.R;

public class EventsActivity extends Activity {

    public Button add_events;
    public Button all_events;
    private final Context context = this;

    private AuthenticationDataViewModel auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events);

        Intent intent = getIntent();
        auth = (AuthenticationDataViewModel)intent.getSerializableExtra("authData");
        add_events = (Button) findViewById(R.id.add_events);
        all_events = (Button) findViewById(R.id.all_events);

        add_events.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, AddEventActivity.class);
                intent.putExtra("authData", auth);
                startActivity(intent);
            }
        });

        all_events.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, EventsListActivity.class);
                intent.putExtra("authData", auth);
                startActivity(intent);
            }
        });

    }

    public void toAddEventActivityOnClick(View view) {

    }

    public void toAllEventsActivityOnClick(View view) {

    }
}
