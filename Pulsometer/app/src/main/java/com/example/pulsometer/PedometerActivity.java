package com.example.pulsometer;

import android.app.Activity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.activeandroid.query.Select;
import com.example.pulsometer.Model.EventViewModel;
import com.example.pulsometer.Model.PedometerSqlite;
import com.example.pulsometer.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.EventListener;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PedometerActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pedometer);
    }

    private void checkEvents() throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        List<EventViewModel> eventViewModelList = new Select()
                .from(EventViewModel.class)
                .execute();
        for (EventViewModel eventViewModel : eventViewModelList) {
            Set<Date> dates = new HashSet<>();
            Date startDate = dateFormat.parse(eventViewModel.StartDateTimeEvent);
            Date endDate = dateFormat.parse(eventViewModel.EndDateTimeEvent);
            dates.add(startDate);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(startDate);
            do {
                calendar.add(Calendar.DAY_OF_MONTH, 1);
                dates.add(calendar.getTime());
            } while(!calendar.getTime().equals(endDate));
            long stepsNumber = 0;
            for (Date date : dates) {
                PedometerSqlite pedometerSqlite = new Select()
                        .from(PedometerSqlite.class)
                        .where("Date = ?", date)
                        .executeSingle();
                stepsNumber += pedometerSqlite.StepsNumber;
            }
            if (stepsNumber >= eventViewModel.StepsNumber) {
                System.out.println("Event passed!!!");
            } else {
                System.out.println("Event not passed!!!");
            }
        }
    }
}
