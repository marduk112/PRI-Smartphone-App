package com.example.pulsometer.Logic.Extensions;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;

import com.example.pulsometer.AddEventActivity;
import com.example.pulsometer.HistoryActivity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Szymon Wójcik on 30.11.2015.
 */
public class DatePicker2Fragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    private boolean setStartDate = false;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        // Create a new instance of DatePickerDialog and return it
        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        // Do something with the date chosen by the user
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, monthOfYear, dayOfMonth);
        long time = calendar.getTimeInMillis();
        Bundle args = getArguments();
        if (args.getString("date").equals("start")) {
            setStartDate = true;
        }
        if (setStartDate)
            AddEventActivity.setStartDateEvent(new Date(time));
        else
            AddEventActivity.setEndDateEvent(new Date(time));
    }
}
