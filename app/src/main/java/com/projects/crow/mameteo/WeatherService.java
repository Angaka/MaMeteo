package com.projects.crow.mameteo;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

/**
 * Created by Venom on 08/09/2017.
 */

public class WeatherService extends Service {

    private static final String TAG = "WeatherService";

    private boolean mIsRunning;
    private Context mContext;
    private Thread mBackgroundThread;
    private Runnable mMyTask = new Runnable() {
        @Override
        public void run() {
            Log.d(TAG, "run: ");
            stopSelf();
        }
    };

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        mContext = this;
        mIsRunning = false;
        mBackgroundThread = new Thread(mMyTask);
    }

    @Override
    public void onDestroy() {
        mIsRunning = false;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand: isRunning " + mIsRunning);
        if (!mIsRunning) {
            mIsRunning = true;
            mBackgroundThread.start();
        }
        return START_NOT_STICKY;
    }
}
