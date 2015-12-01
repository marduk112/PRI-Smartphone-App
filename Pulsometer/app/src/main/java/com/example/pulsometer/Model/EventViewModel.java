package com.example.pulsometer.Model;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Szymon Wójcik on 16.11.2015.
 */
public class EventViewModel implements Serializable {
    public int Id;
    public String Name;
    public String Description;
    public int Min;
    public int Max;
    public String StartDateTimeEvent;
    public int EventDuration;
    public int Duration;
    //0-Min, 1-Max, 2-Between
    public int Target;

    public EventViewModel(String name, String description, int min, int max, Date startDateTimeEvent,
                          int eventDuration, int duration) {
        Name = name;
        Description = description;
        Min = min;
        Max = max;
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        String newDate = dateFormat.format(startDateTimeEvent);
        StartDateTimeEvent = newDate;
        EventDuration = eventDuration;
        Duration = duration;
    }
}

