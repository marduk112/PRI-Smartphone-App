package com.example.pulsometer.Model;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Szymon Wójcik on 16.11.2015.
 */
public class EventViewModel implements Serializable {
    public Integer Id;
    public String Name;
    public String Description;
    public Integer Min;
    public Integer Max;
    public Date StartDateTimeEvent;
    public Integer EventDuration;
    public Integer Duration;
    //0-Min, 1-Max, 2-Between
    public int Target;

    public EventViewModel(String name, String description, Integer min, Integer max, String startDateTimeEvent,
                          Integer eventDuration, Integer duration) {
        Name = name;
        Description = description;
        Min = min;
        Max = max;
        StartDateTimeEvent = new Date(startDateTimeEvent);
        EventDuration = eventDuration;
        Duration = duration;
    }
}

