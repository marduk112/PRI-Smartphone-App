package com.example.pulsometer;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import com.example.pulsometer.Logic.AsyncTasks.CreateEventTask;
import com.example.pulsometer.Logic.AsyncTasks.JoinToEventTask;
import com.example.pulsometer.Logic.Extensions.DatePicker2Fragment;
import com.example.pulsometer.Logic.Extensions.DatePickerFragment;
import com.example.pulsometer.Model.AuthenticationDataViewModel;
import com.example.pulsometer.Model.EventViewModel;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class AddEventActivity extends FragmentActivity {

    private EditText nameEditText;
    private EditText descriptionEditText;
    private EditText minEditText;
    private EditText maxEditText;
    private EditText startDateEventEditText;
    private EditText eventDurationEditText;
    private EditText durationEditText;
    private AddEventActivity mActivity = this;

    private AuthenticationDataViewModel auth;

    private static Date startDateEvent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);

        nameEditText = (EditText) findViewById(R.id.name_of_event);
        descriptionEditText = (EditText) findViewById(R.id.event_description);
        minEditText = (EditText) findViewById(R.id.min_value_of_pulse);
        maxEditText = (EditText) findViewById(R.id.max_value_of_pulse);
        //startDateEventEditText = (EditText) findViewById(R.id.start_date_event);
        eventDurationEditText = (EditText) findViewById(R.id.event_duration);
        durationEditText = (EditText) findViewById(R.id.duration);

        Intent intent = getIntent();


            auth = (AuthenticationDataViewModel)intent.getSerializableExtra("authData");
            Button button = (Button) findViewById(R.id.create_event);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    addNewEventOnClick();
                }
            });

    }

    public static Date getStartDateEvent() {
        return startDateEvent;
    }

    public static void setStartDateEvent(Date startDateEvent) {
        AddEventActivity.startDateEvent = startDateEvent;
    }

    public void addNewEventOnClick() {
        try {
            final EventViewModel eventViewModel = new EventViewModel(nameEditText.getText().toString(),
                    descriptionEditText.getText().toString(),
                    Integer.parseInt(minEditText.getText().toString()),
                    Integer.parseInt(maxEditText.getText().toString()),
                    startDateEvent,
                    Integer.parseInt(eventDurationEditText.getText().toString()),
                    Integer.parseInt(durationEditText.getText().toString()));
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

    public void showDatePickerDialog(View view) {
        DialogFragment newFragment = new DatePicker2Fragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }
}
