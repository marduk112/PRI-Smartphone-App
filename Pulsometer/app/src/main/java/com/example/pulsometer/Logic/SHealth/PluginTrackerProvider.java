package com.example.pulsometer.Logic.SHealth;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.samsung.android.sdk.SsdkUnsupportedException;
import com.samsung.android.sdk.shealth.Shealth;

public class PluginTrackerProvider extends Application {

    private Context mContext;
    private static final String LOG_TAG = "Pulsometer";

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
        Shealth shealth = new Shealth();
        try {
            shealth.initialize(mContext);
        } catch (SsdkUnsupportedException e) {
            int eType = e.getType();
            Log.d(LOG_TAG, "Samsung Digital Health Initialization failed. Error type : " + eType);
            if (eType == SsdkUnsupportedException.VENDOR_NOT_SUPPORTED) {
                // It is thrown if the device does not support the S Health SDK.
            } else if (eType == SsdkUnsupportedException.LIBRARY_NOT_INSTALLED) {
                // It is thrown if the library of the SDK is not found.
            }
        } catch (Exception e1) {
            Log.d(LOG_TAG, "Samsung Digital Health Initialization failed.");
        }
    }
}