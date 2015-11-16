package com.example.pulsometer;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.pulsometer.Logic.AsyncTasks.CreateEventTask;
import com.example.pulsometer.Logic.AsyncTasks.JoinToEventTask;
import com.example.pulsometer.Model.AuthenticationDataViewModel;
import com.example.pulsometer.Model.EventViewModel;

import java.util.Date;

public class AddEventActivity extends Activity {

    private EditText nameEditText;
    private EditText descriptionEditText;
    private EditText minEditText;
    private EditText maxEditText;
    private EditText startDateEventEditText;
    private EditText eventDurationEditText;
    private EditText durationEditText;
    private AddEventActivity mActivity = this;

    private AuthenticationDataViewModel auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);

        nameEditText = (EditText) findViewById(R.id.name_of_event);
        descriptionEditText = (EditText) findViewById(R.id.event_description);
        minEditText = (EditText) findViewById(R.id.min_value_of_pulse);
        maxEditText = (EditText) findViewById(R.id.max_value_of_pulse);
        startDateEventEditText = (EditText) findViewById(R.id.start_date_event);
        eventDurationEditText = (EditText) findViewById(R.id.event_duration);
        durationEditText = (EditText) findViewById(R.id.duration);

        Intent intent = getIntent();
        boolean readOnly = intent.getBooleanExtra("readOnly", false);
        
        if (readOnly) {
            EventViewModel eventViewModel = (EventViewModel) intent.getSerializableExtra("event");
            ViewEventDetails(eventViewModel);
        }
        else {
            auth = (AuthenticationDataViewModel)intent.getSerializableExtra("authData");
            Button button = (Button) findViewById(R.id.create_event);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    addNewEventOnClick();
                }
            });
        }
    }

    public void ViewEventDetails(final EventViewModel eventViewModel) {
        nameEditText.setEnabled(false);
        descriptionEditText.setEnabled(false);
        minEditText.setEnabled(false);
        maxEditText.setEnabled(false);
        startDateEventEditText.setEnabled(false);
        eventDurationEditText.setEnabled(false);
        durationEditText.setEnabled(false);
        Button button = (Button) findViewById(R.id.create_event);
        button.setText(R.string.join_to_event);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new JoinToEventTask(auth.access_token, eventViewModel.Id, mActivity).execute();
            }
        });

        nameEditText.setText(eventViewModel.Name);
        descriptionEditText.setText(eventViewModel.Description);
        minEditText.setText(eventViewModel.Min);
        maxEditText.setText(eventViewModel.Max);
        startDateEventEditText.setText(eventViewModel.StartDateTimeEvent.toString());
        eventDurationEditText.setText(eventViewModel.EventDuration);
        durationEditText.setText(eventViewModel.Duration);
    }

    public void addNewEventOnClick() {
        try {
            final EventViewModel eventViewModel = new EventViewModel(nameEditText.getText().toString(),
                    descriptionEditText.getText().toString(),
                    Integer.getInteger(minEditText.getText().toString()),
                    Integer.getInteger(maxEditText.getText().toString()),
                    startDateEventEditText.getText().toString(),
                    Integer.getInteger(eventDurationEditText.getText().toString()),
                    Integer.getInteger(durationEditText.getText().toString()));
                    new AlertDialog.Builder(this)
                            .setTitle("")
                            .setMessage("Add new Event?")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    new CreateEventTask(auth.access_token, eventViewModel, mActivity).execute();
                                }
                            })
                            .setNegativeButton("Cancel", null)
                            .show();
        } catch (Exception e) {
            new AlertDialog.Builder(this)
                    .setTitle("Error")
                    .setMessage("Error with adding new event\n" + e.getMessage() + "\n" + e.getLocalizedMessage())
                    .setPositiveButton("OK", null)
                    .show();
        }
    }
}
