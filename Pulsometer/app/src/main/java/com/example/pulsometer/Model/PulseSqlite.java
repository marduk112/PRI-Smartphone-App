package com.example.pulsometer.Model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

import java.util.*;

/**
 * Created by Szymon Wójcik on 02.11.2015.
 */
@Table(name = "Pulses")
public class PulseSqlite extends Model {
    @Column(name = "pulse")
    public int PulseValue;
    @Column(name = "date")
    public java.util.Date DateCreated;

    public PulseSqlite(){
        super();
    }

    public PulseSqlite(int pulseValue, java.util.Date dateCreated) {
        super();
        PulseValue = pulseValue;
        DateCreated = dateCreated;
    }
}
