package Logic.SHealth;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.samsung.android.sdk.shealth.tracker.TrackerTileManager;

/**
 * Created by Szymon Wójcik on 2015-04-23.
 */
public class MyTrackerService extends IntentService {
    private static final String LOG_TAG = "PluginTracker";
    private static final String SHARED_PREFERENCE_NAME = "tile_content";
    private static final String SHARED_PREFERENCE_KEY = "content_value";
    /**
     * Constructs a TrackerTileService object.
     *
     */
    public MyTrackerService() {
        super(LOG_TAG);
    }
    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent == null) {
            return;
        }
        String trackerId = intent.getStringExtra(TrackerTileManager.EXTRA_TRACKER_ID);
        if (trackerId == null) {
            return;
        }
        Log.d(LOG_TAG, "tracker id : " + trackerId);
        String tileId = intent.getStringExtra(TrackerTileManager.EXTRA_TILE_ID);
        if (tileId == null) {
            return;
        }
        Log.d(LOG_TAG, "tile id : " + tileId);
        SharedPreferences sp = getSharedPreferences(SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE);
        int tileContent = sp.getInt(SHARED_PREFERENCE_KEY, 0) + 1;
        Log.d(LOG_TAG, "content value : " + String.valueOf(tileContent));
        sp.edit().putInt(SHARED_PREFERENCE_KEY, tileContent).commit();
        MyTracker tracker = new MyTracker(/*this*/);
        tracker.updateTile(this, trackerId, tileId);
    }
    @Override public void onDestroy() {
        super.onDestroy();
    }
}
