package com.example.pulsometer.Logic.PulseAnalyse;

import com.example.pulsometer.Logic.Extensions.GlobalVariables;

/**
 * Created by Szymon Wójcik on 02.12.2015.
 */
public class AnalysePulse {
    public String analysePulse() {
        return "max pulse = " + GetMaxPulse();
    }

    private int GetMaxPulse() {
        int maxPulse = 0;
        for (Integer pulse : GlobalVariables.Pulses) {
            if (maxPulse < pulse)
                maxPulse = pulse;
        }

        return maxPulse;
    }
}
