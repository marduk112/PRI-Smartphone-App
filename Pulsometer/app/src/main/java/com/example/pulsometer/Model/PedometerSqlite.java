package com.example.pulsometer.Model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

import java.util.Date;

/**
 * Created by Szymon Wójcik on 18.12.2015.
 */
@Table(name = "PedometerData")
public class PedometerSqlite extends Model {
    @Column(name = "Date", index = true)
    public String Date;
    @Column(name = "StepsNumber")
    public long StepsNumber;
    public PedometerSqlite() {

    }
}
