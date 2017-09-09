package com.projects.crow.mameteo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by Venom on 08/09/2017.
 */

public class WeatherBootReceiver extends BroadcastReceiver {

    private static final String TAG = "WeatherBootReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "onReceive:");
        Intent weatherIntent = new Intent(context, WeatherService.class);
        context.startService(weatherIntent);
    }
}
