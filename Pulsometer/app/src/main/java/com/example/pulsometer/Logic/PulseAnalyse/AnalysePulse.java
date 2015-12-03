package com.example.pulsometer.Logic.PulseAnalyse;

import android.content.res.Resources;

import com.example.pulsometer.R;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by Szymon Wójcik on 02.12.2015.
 */
public class AnalysePulse {
    private ArrayList<Integer> pulses;
    private int age;
    private Resources resources;

    public AnalysePulse(ArrayList<Integer> pulses, int age, Resources resources) {
        this.pulses = pulses;
        this.age = age;
        this.resources = resources;
    }

    public String analysePulse() {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("max pulse = " + getMaxPulse() +
                "\nmin pulse = " + getMinPulse() + "\n");
        if (isPulseBelow60())
            stringBuilder.append(resources.getString(R.string.there_is_risk_of_periodic_arrhythmia) + "\n");
        if (isPulseBelow60() || (isPulseAbove90() && !isYoung()))
            stringBuilder.append(resources.getString(R.string.possible_irregularities_in_heart_rate) + "\n");

        return stringBuilder.toString();
    }

    private int getMaxPulse() {
        return Collections.max(pulses);
    }

    private int getMinPulse() {
        return Collections.min(pulses);
    }

    private boolean isPulseBelow60() {
        return getMinPulse() < 60;
    }

    private boolean isPulseAbove90() {
        return getMaxPulse() > 90;
    }

    private boolean isYoung() {
        return age <= 35;
    }
}
