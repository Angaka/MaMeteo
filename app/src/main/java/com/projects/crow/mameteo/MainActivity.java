package com.projects.crow.mameteo;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.projects.crow.mameteo.utils.EnhancedSharedPreferences;
import com.projects.crow.mameteo.utils.PermissionsUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity
    implements View.OnClickListener {

    private static final String TAG = "MainActivity";
    private static final String PREFS = "myPrefs";

    private static final String[] mPermissions = { "android.permission.ACCESS_FINE_LOCATION", "android.permission.ACCESS_COARSE_LOCATION" };
    private static final int mRequestCode = 200;
    private PermissionsUtils mPermissionsUtils;

    private FusedLocationProviderClient mFusedLocationClient;

    private ConstraintLayout mConstraintLayout;

    private Snackbar mSnackBarPermissions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mConstraintLayout = findViewById(R.id.constraint_layout);

        mSnackBarPermissions = Snackbar.make(mConstraintLayout, R.string.permissions_request, Snackbar.LENGTH_INDEFINITE)
                                    .setAction(R.string.see_permissions, new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            ArrayList<String> unacceptedPermissions = mPermissionsUtils.findUnacceptedPermissions(Arrays.asList(mPermissions));
                                            String[] permissionsArray = new String[unacceptedPermissions.size()];

                                            for (int i = 0; i < unacceptedPermissions.size(); i++)
                                                permissionsArray[i] = unacceptedPermissions.get(i);
                                            requestPermissions(permissionsArray, mRequestCode);
                                        }
                                    });

        checkPermissions();

        Intent alarm = new Intent(this, WeatherBootReceiver.class);
        boolean alarmRunning = (PendingIntent.getBroadcast(this, 0, alarm, PendingIntent.FLAG_NO_CREATE) != null);
        Log.d(TAG, "onCreate: " + alarmRunning);
        if (!alarmRunning) {
            PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, alarm, PendingIntent.FLAG_UPDATE_CURRENT);
            AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, SystemClock.elapsedRealtime(), 1000, pendingIntent);
        }
    }

    private void checkPermissions() {
        SharedPreferences preferences = getBaseContext().getSharedPreferences(PREFS, Context.MODE_PRIVATE);
        EnhancedSharedPreferences enhancedSharedPreferences = new EnhancedSharedPreferences(preferences);

        mPermissionsUtils = new PermissionsUtils(this, enhancedSharedPreferences);

        ArrayList<String> unaskedPermissions = mPermissionsUtils.findUnAskedPermissions(Arrays.asList(mPermissions));

        if (!unaskedPermissions.isEmpty()) {
            String[] permissionsArray = new String[unaskedPermissions.size()];

            for (int i = 0; i < unaskedPermissions.size(); i++)
                permissionsArray[i] = unaskedPermissions.get(i);
            requestPermissions(permissionsArray, mRequestCode);
        } else {
            ArrayList<String> unacceptedPermission = mPermissionsUtils.findUnacceptedPermissions(Arrays.asList(mPermissions));

            if (!unacceptedPermission.isEmpty()) {
                if (!mSnackBarPermissions.isShown())
                    mSnackBarPermissions.show();
            }
        }
    }

    private void init() {
        Log.d(TAG, "onCreate: launchAlarmManager");
        Intent intentAlarm = new Intent(MainActivity.this, WeatherBootReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getService(getApplicationContext(), 0, intentAlarm, PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.setInexactRepeating(AlarmManager.RTC,
                SystemClock.elapsedRealtime() + AlarmManager.INTERVAL_HOUR ,
                AlarmManager.INTERVAL_HOUR,
                pendingIntent);

        Log.d(TAG, "onReceive:");
        SharedPreferences preferences = getBaseContext().getSharedPreferences(PREFS, Context.MODE_PRIVATE);
        EnhancedSharedPreferences enhancedSharedPreferences = new EnhancedSharedPreferences(preferences);
        final EnhancedSharedPreferences.Editor editor = enhancedSharedPreferences.edit();

        double latitude = enhancedSharedPreferences.getDouble("latitude", 0.0);
        double longitude = enhancedSharedPreferences.getDouble("longitude", 0.0);

        Log.d(TAG, "onReceive: sharedPreferences " + latitude  + " " + longitude);

        try {
            mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
            mFusedLocationClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    Log.d(TAG, "onSuccess: " + location.getLatitude() + " " + location.getLongitude());
                    Geocoder geocoder = new Geocoder(getBaseContext(), Locale.getDefault());
                    try {
                        List<Address> addressList = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);

                        if (!addressList.isEmpty()) {
                            Address nearestCity = addressList.get(0);
                            if (nearestCity.hasLatitude() && nearestCity.hasLongitude()) {
                                Log.d(TAG, "onReceive: nearest " + nearestCity.getLatitude() + " " + nearestCity.getLongitude());
                                editor.putDouble("latitude", nearestCity.getLatitude());
                                editor.putDouble("longitude", nearestCity.getLongitude());
                                editor.commit();
                            }
                        }
                    } catch (Exception e) {
                        Log.d(TAG, "onReceive: " + e.getMessage());
                    }
                }
            });
        } catch (SecurityException e) {
            Log.d(TAG, "onReceive: " + e.getMessage());
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case mRequestCode:
                if (grantResults.length > 0) {
                    for (int i = 0; i < grantResults.length; i++)
                        mPermissionsUtils.markAsAsked(permissions[i]);

                    ArrayList<String> unacceptedPermissions = mPermissionsUtils.findUnacceptedPermissions(Arrays.asList(mPermissions));
                    if (!unacceptedPermissions.isEmpty()) {
                        if (!mSnackBarPermissions.isShown())
                            mSnackBarPermissions.show();
                    } else {
                        if (mSnackBarPermissions.isShown())
                            mSnackBarPermissions.dismiss();
                    }
                }
                break;
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
        }
    }
}
