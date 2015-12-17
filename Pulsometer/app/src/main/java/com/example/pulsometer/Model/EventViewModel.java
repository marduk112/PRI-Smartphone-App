package com.example.pulsometer.Model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Szymon Wójcik on 16.11.2015.
 */
@Table(name = "Events")
public class EventViewModel extends Model implements Serializable {
    @Column(name = "id")
    public int Id;
    @Column(name = "name")
    public String Name;
    @Column(name = "description")
    public String Description;
    @Column(name = "stepsNumber")
    public int StepsNumber;
    @Column(name = "startDateTimeEvent")
    public String StartDateTimeEvent;
    @Column(name = "endDateTimeEvent")
    public String EndDateTimeEvent;

    public EventViewModel(String name, String description, int stepsNumber, Date startDateTimeEvent, Date endDateTimeEvent) {
        Name = name;
        Description = description;
        StepsNumber = stepsNumber;
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        StartDateTimeEvent = dateFormat.format(startDateTimeEvent);
        EndDateTimeEvent = dateFormat.format(endDateTimeEvent);
    }
}

