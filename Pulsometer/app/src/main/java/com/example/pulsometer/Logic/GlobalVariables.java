package com.example.pulsometer.Logic;

import java.util.ArrayList;
import java.util.List;

import ca.odell.glazedlists.BasicEventList;
import ca.odell.glazedlists.EventList;

/**
 * Created by Acer on 2015-06-16.</>
 */
public class GlobalVariables {
    public static EventList<Integer> Pulses = new BasicEventList<>();
    public static final String BaseUrlForRest = "http://pulsometerrest.apphb.com/";
    public static String AccessToken;
}
