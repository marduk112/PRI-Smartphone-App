package com.example.pulsometer.Model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

/**
 * Created by Szymon Wójcik on 03.12.2015.
 */
@Table(name = "UserData")
public class AgeSqlite extends Model {
    @Column(name = "age")
    public int age;

    public AgeSqlite(int age) {
        this.age = age;
    }
}
