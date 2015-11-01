package com.example.pulsometer.Logic.Extensions;

import com.example.pulsometer.Logic.Extensions.ListenableArrayList;

/**
 * Created by Acer on 2015-06-16.</>
 */
public final class GlobalVariables {
    public static ListenableArrayList<Integer> Pulses = new ListenableArrayList<>();
    public static final String BaseUrlForRest = "http://pulsometerrest.apphb.com/";
    public static String AccessToken;
}

