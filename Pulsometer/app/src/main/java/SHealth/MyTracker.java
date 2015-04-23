package SHealth;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.util.Log;

import com.google.android.gms.analytics.Tracker;
import com.samsung.android.sdk.shealth.tracker.TrackerEventListener;
import com.samsung.android.sdk.shealth.tracker.TrackerTile;
import com.samsung.android.sdk.shealth.tracker.TrackerTileManager;

/**
 * Created by Szymon Wójcik on 2015-04-23.
 */
public class MyTracker implements TrackerEventListener {
    private static final String LOG_TAG = "PluginTracker";
    private TrackerTileManager mTrackerTileManager;
    private static final String MY_TILE_ID = "hello_tile";
    public MyTracker()
    {

    }
    @Override
    public void onCreate(Context context, String trackerId) {
        Log.d(LOG_TAG, "onCreate(" + trackerId + ")");
        if (mTrackerTileManager == null) {
            try {
                mTrackerTileManager = new TrackerTileManager(context);
            } catch (IllegalArgumentException e) {
                Log.d(LOG_TAG, "MyTracker onCreate() - IllegalArgumentException");
            }
        }
    }

    @Override
    public void onSubscribed(Context context, String trackerId) {
        Log.d(LOG_TAG, "onSubscribed(" + trackerId + ")");
        // User changed the subscription state of your tracker to subscribe.
        // Post your tracker tile to be shown on the S Health's main screen.
        // Use TRACKER_TILE_TYPE_1 if there is no data yet.
        //postDefaultTile(context, trackerId, MY_TILE_ID);
    }

    @Override
    public void onUnsubscribed(Context context, String trackerId) {
        Log.d(LOG_TAG, "onUnsubscribed(" + trackerId + ")");
        // User changed the subscription state of your tracker to unsubscription.
    }

    @Override
    public void onTileRequested(Context context, String trackerId, String tileId) {
        Log.d(LOG_TAG, "onTileRequested(" + trackerId + ", " + tileId + ")");
        // The main screen is resumed on the foreground.
        // If tileId is null, it means there is no posted tracker tile successfully on the main screen
        // Post or update your tracker tile.
        // Use TRACKER_TILE_TYPE_1 if there is no data yet.
        // If there is posted tracker tile already, you can update it with updated data here.
        if(tileId == null){

        }
        // Update your tracker tile.
        //updateTile(context, trackerId, MY_TILE_ID);
    }

    @Override
    public void onTileRemoved(Context context, String trackerId, String tileId) {
        Log.d(LOG_TAG, "onTileRemoved(" + trackerId + ", " + tileId + ")");
        // User removed your posted tracker tile from the main screen.
    }

    @Override
    public void onPaused(Context context, String trackerId) {
        Log.d(LOG_TAG, "onPaused(" + trackerId + ")");
        // The main screen of S Health went to the background.
        // Stop posting tracker tiles.
    }

    private void postDefaultTile(Context context, String trackerId, String tileId) {
        /*TrackerTile myTrackerTile = null;
        if (tileId == null) {
            tileId = MY_TILE_ID;
        }
        try {
            // Create Intent to do an action
            // when the tracker tile is clicked
            Intent launchIntent = new Intent(context, MainActivity.class);
            // Create Intent to do an action
            // when the button on this tile is clicked
            Intent serviceIntent = new Intent(context, MyTrackerService.class);
            // Set template
            //mTemplate = TrackerTile.TRACKER_TILE_TYPE_1;
            // Create TrackerTile and set each values and intents
            myTrackerTile = new TrackerTile(context, trackerId, tileId, mTemplate);
            // Set a tracker tile
            myTrackerTile
                    // Set Title
                    .setTitle(R.string.tracker_test_display_name)
                            // Set Icon resource
                    .setIcon(R.drawable.tracker_test_icon_big_1)
                            // Set content color
                    .setContentColor(Color.parseColor("#7CB342"))
                            // Set content intent
                    .setContentIntent(TrackerTile.INTENT_TYPE_ACTIVITY, launchIntent)
                            // Set button intent
                    .setButtonIntent("START", TrackerTile.INTENT_TYPE_SERVICE, serviceIntent);
            if (mTrackerTileManager != null) {
                mTrackerTileManager.post(myTrackerTile);
            }
        } catch (IllegalArgumentException e) {
            Log.d(LOG_TAG, "MyTracker postDefaultTile(" + trackerId + ", " + tileId + ") IllegalArgumentException");
        } catch (Resources.NotFoundException e) {
            Log.d(LOG_TAG, "MyTracker postDefaultTile(" + trackerId + ", " + tileId + ") NotFoundException");
        }*/
    }

    public void updateTile(Context context, String trackerId, String tileId) {
        Log.d(LOG_TAG, "updateTile(" + trackerId + ", " + tileId + ")");
        /*TrackerTile myTrackerTile = null;
        if (tileId == null) {
            tileId = MY_TILE_ID;
        }
        try {
            // Create Intent to do an action
            // when the tracker tile is clicked
            Intent launchIntent = new Intent(context, MainActivity.class);
            // Create Intent to do an action
            // when the button on this tile is clicked
            Intent serviceIntent = new Intent(context, MyTrackerService.class);
            // Set template
            //mTemplate = TrackerTile.TRACKER_TILE_TYPE_3;
            // Create TrackerTile and set each values and intents
            myTrackerTile = new TrackerTile(context, trackerId, tileId, mTemplate);
            // Set a tracker tile
            myTrackerTile
                    // Set Title
                    .setTitle(R.string.tracker_test_display_name)
                            // Set Icon resource
                    .setIcon(R.drawable.tracker_test_icon_big_1)
                            // Set content value
                    .setContentValue(String.valueOf(mTileContent))
                            // Set content unit
                    .setContentUnit("LBS")
                            // Set Date text
                    .setDate(new Date())
                            // Set content color
                    .setContentColor(Color.parseColor("#7CB342"))
                            // Set content intent
                    .setContentIntent(TrackerTile.INTENT_TYPE_ACTIVITY, launchIntent)
                            // Set button intent
                    .setButtonIntent("UPDATE", TrackerTile.INTENT_TYPE_SERVICE, serviceIntent);
            if (mTrackerTileManager != null) {
                mTrackerTileManager.post(myTrackerTile);
            }
        } catch (IllegalArgumentException e) {
            Log.d(LOG_TAG, "MyTracker postDefaultTile(" + trackerId + ", " + tileId + ") IllegalArgumentException");
        } catch (Resources.NotFoundException e) {
            Log.d(LOG_TAG, "MyTracker postDefaultTile(" + trackerId + ", " + tileId + ") NotFoundException");
        }*/
    }
}
