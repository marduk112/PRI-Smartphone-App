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
    private String activity;

    public AnalysePulse(ArrayList<Integer> pulses, int age, Resources resources, String activity) {
        this.pulses = pulses;
        this.age = age;
        this.resources = resources;
        this.activity = activity;
    }

    public String analysePulse() {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("max pulse = " + getMaxPulse() +
                "\nmin pulse = " + getMinPulse() + "\n");
        stringBuilder.append("pulse amplitude " + (getMaxPulse() - getMinPulse()) + "\n");
        if (activity.equals("rest")) {
            if (isChildren() && !(getMinPulse() >= 80 && getMaxPulse() <= 120))
                stringBuilder.append("not your average pulse\n");
            if (isYoung() && !(getMinPulse() >= 65 && getMaxPulse() <= 105))
                stringBuilder.append("not your average pulse\n");
            if (isAdult() && !(getMinPulse() >= 50 && getMaxPulse() <= 90))
                stringBuilder.append("not your average pulse\n");
            if (isOldMan() && !(getMinPulse() >= 40 && getMaxPulse() <= 80))
                stringBuilder.append("not your average pulse\n");
            if (isPulseBelow60())
                stringBuilder.append(resources.getString(R.string.there_is_risk_of_periodic_arrhythmia) + "\n");
            if (isPulseBelow60() || (isPulseAbove90() && !isYoung()))
                stringBuilder.append(resources.getString(R.string.possible_irregularities_in_heart_rate) + "\n");
        } else if (activity.equals("walk") || activity.equals("run")) {
            double tanaka = (208 - (0.7 * age));
            double miller = (217 - (0.85 * age));
            double londree = (206.3 - (0.711 * age));
            double oakland = (206.9 - (0.67 * age));
            double average = (tanaka + miller + londree + oakland) / 4;
            java.text.DecimalFormat df=new java.text.DecimalFormat();
            df.setMaximumFractionDigits(2);
            df.setMinimumFractionDigits(0);
            stringBuilder.append("maximum allowable pulse (Tanaka formula) " + df.format(tanaka) + "\n");
            stringBuilder.append("maximum allowable pulse (Miller AT al formula) " + df.format(miller) + "\n");
            stringBuilder.append("maximum allowable pulse (Londree and Moeschburger formula) " + df.format(londree) + "\n");
            stringBuilder.append("maximum allowable pulse (from Oakland University formula) " + df.format(oakland) + "\n");
            stringBuilder.append("maximum allowable pulse (average) " + df.format(average) + "\n");
        } else if (activity.equals("walk")) {

        } else if (activity.equals("run")) {

        }

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

    private boolean isPulsusRegularis() {
        boolean isPulsusRegularis = false;
        for (Integer pulse : pulses) {

        }

        return isPulsusRegularis;
    }

    private boolean isYoung() {
        return age <= 35;
    }

    private boolean isChildren(){
        return age < 18;
    }

    private boolean isYouth() {
        return age >= 18 && age < 35;
    }

    private boolean isAdult() {
        return age >= 35 && age < 65;
    }

    private boolean isOldMan() {
        return age >= 65;
    }
}
