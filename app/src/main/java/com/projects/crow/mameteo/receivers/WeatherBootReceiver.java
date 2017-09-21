package com.projects.crow.mameteo.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.projects.crow.mameteo.utils.MaMeteoUtils;
import com.projects.crow.mameteo.utils.services.EnhancedSharedPreferences;
import com.projects.crow.mameteo.utils.services.LocationService;

/**
 * Created by Venom on 09/09/2017.
 */

public class WeatherBootReceiver extends BroadcastReceiver {
    private static final String TAG = "WeatherBootReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        SharedPreferences preferences = context.getSharedPreferences(MaMeteoUtils.PREFS, Context.MODE_PRIVATE);
        EnhancedSharedPreferences prefs = new EnhancedSharedPreferences(preferences);

        LocationService locationService = new LocationService(context, prefs);
        locationService.updateLocation();
    }
}
