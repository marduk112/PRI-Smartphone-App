package com.example.pulsometer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;

import com.example.pulsometer.Logic.AsyncTasks.GetAllEventsTask;
import com.example.pulsometer.Model.AuthenticationDataViewModel;
import com.example.pulsometer.R;

public class EventsActivity extends Activity {

    private AuthenticationDataViewModel auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events);

        Intent intent = getIntent();
        auth = (AuthenticationDataViewModel)intent.getSerializableExtra("authData");
    }

    public void toAddEventActivityOnClick(View view) {
        Intent intent = new Intent(this, AddEventActivity.class);
        intent.putExtra("authData", auth);
        startActivity(intent);
    }

    public void toAllEventsActivityOnClick(View view) {
        Intent intent = new Intent(this, EventsListActivity.class);
        intent.putExtra("authData", auth);
        startActivity(intent);
    }
}
