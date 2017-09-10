package com.projects.crow.mameteo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import com.projects.crow.mameteo.utils.MaMeteoUtils;

/**
 * Created by Venom on 09/09/2017.
 */

public class WeatherBootReceiver extends BroadcastReceiver {
    private static final String TAG = "WeatherBootReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent in = new Intent();
        in.setAction(MaMeteoUtils.UPDATE_FORECAST);
        LocalBroadcastManager.getInstance(context).sendBroadcast(in);
    }

}
