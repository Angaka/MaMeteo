package com.projects.crow.mameteo;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.projects.crow.mameteo.database.DatabaseHelper;
import com.projects.crow.mameteo.database.models.Weather;
import com.projects.crow.mameteo.utils.EnhancedSharedPreferences;
import com.projects.crow.mameteo.utils.MaMeteoUtils;
import com.projects.crow.mameteo.utils.PermissionsUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity
        implements View.OnClickListener {

    private static final String TAG = "MainActivity";
    private static final String PREFS = "myPrefs";

    private static final String[] mPermissions = {"android.permission.ACCESS_FINE_LOCATION", "android.permission.ACCESS_COARSE_LOCATION"};
    private static final int mRequestCode = 200;
    private PermissionsUtils mPermissionsUtils;
    private FusedLocationProviderClient mFusedLocationClient;

    private ConstraintLayout mConstraintLayout;
    private Snackbar mSnackBarPermissions;

    private BroadcastReceiver mUpdateDataReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d(TAG, "onReceive: updateData");
            updateData();
        }
    };
    private boolean mCheckIfReceiverRegistered = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mConstraintLayout = findViewById(R.id.constraint_layout);

        mSnackBarPermissions = Snackbar
                .make(mConstraintLayout, R.string.permissions_request, Snackbar.LENGTH_INDEFINITE)
                .setAction(R.string.see_permissions,
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                ArrayList<String> unacceptedPermissions = mPermissionsUtils.findUnacceptedPermissions(Arrays.asList(mPermissions));
                                String[] permissionsArray = new String[unacceptedPermissions.size()];

                                for (int i = 0; i < unacceptedPermissions.size(); i++)
                                    permissionsArray[i] = unacceptedPermissions.get(i);
                                requestPermissions(permissionsArray, mRequestCode);
                            }
                        });
        startAlarm();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!mCheckIfReceiverRegistered) {
            mCheckIfReceiverRegistered = true;
            LocalBroadcastManager.getInstance(this).registerReceiver(mUpdateDataReceiver, new IntentFilter("UPDATE"));
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mCheckIfReceiverRegistered)
            LocalBroadcastManager.getInstance(this).unregisterReceiver(mUpdateDataReceiver);
    }

    private void startAlarm() {
        if (isAllPermissionsAccepted()) {
            Intent alarm = new Intent(MainActivity.this, WeatherBootReceiver.class);
            boolean alarmRunning = (PendingIntent.getBroadcast(this, 0, alarm, PendingIntent.FLAG_NO_CREATE) != null);
            Log.d(TAG, "startAlarm: " + alarmRunning);
            if (!alarmRunning) {
                PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, alarm, PendingIntent.FLAG_UPDATE_CURRENT);
                AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
                alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, SystemClock.elapsedRealtime(), 1000 * 5, pendingIntent);
            }
        }
    }

    private boolean isAllPermissionsAccepted() {
        SharedPreferences preferences = getBaseContext().getSharedPreferences(PREFS, Context.MODE_PRIVATE);
        EnhancedSharedPreferences enhancedSharedPreferences = new EnhancedSharedPreferences(preferences);

        mPermissionsUtils = new PermissionsUtils(this, enhancedSharedPreferences);

        ArrayList<String> unaskedPermissions = mPermissionsUtils.findUnAskedPermissions(Arrays.asList(mPermissions));

        if (!unaskedPermissions.isEmpty()) {
            String[] permissionsArray = new String[unaskedPermissions.size()];

            for (int i = 0; i < unaskedPermissions.size(); i++)
                permissionsArray[i] = unaskedPermissions.get(i);
            requestPermissions(permissionsArray, mRequestCode);
            return false;
        } else {
            ArrayList<String> unacceptedPermission = mPermissionsUtils.findUnacceptedPermissions(Arrays.asList(mPermissions));

            if (!unacceptedPermission.isEmpty()) {
                if (!mSnackBarPermissions.isShown())
                    mSnackBarPermissions.show();
                return false;
            }
        }

        return true;
    }

    private void updateData() {
        SharedPreferences preferences = getBaseContext().getSharedPreferences(PREFS, Context.MODE_PRIVATE);
        EnhancedSharedPreferences enhancedSharedPreferences = new EnhancedSharedPreferences(preferences);
        final EnhancedSharedPreferences.Editor editor = enhancedSharedPreferences.edit();

        final double latitude = enhancedSharedPreferences.getDouble("latitude", 0.0);
        final double longitude = enhancedSharedPreferences.getDouble("longitude", 0.0);

        Log.d(TAG, "onReceive: sharedPreferences " + latitude + " " + longitude);

        if (!MaMeteoUtils.isProviderEnabled(this, LocationManager.GPS_PROVIDER) && !MaMeteoUtils.isInternetConnectionAvailable(this)) {
            Toast.makeText(this, "No provider enabled", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Enabled", Toast.LENGTH_SHORT).show();
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

                        DatabaseHelper db = DatabaseHelper.getInstance();

                        db.getWeather(latitude, longitude).enqueue(new Callback<Weather>() {
                            @Override
                            public void onResponse(Call<Weather> call, Response<Weather> response) {
                                Log.d(TAG, "onResponse: " + response.code());
                                if (response.code() == 200) {
                                    Gson gson = new GsonBuilder().create();
                                    Weather weather = new Weather();

                                    try {
                                        weather = gson.fromJson(response.body().toString(), Weather.class);

                                        Log.d(TAG, "onResponse: " + weather.toString());
                                    } catch (JsonSyntaxException e) {
                                        Log.d(TAG, "onResponse: " + e.getMessage() + "  ");
                                    }
                                }
                            }

                            @Override
                            public void onFailure(Call<Weather> call, Throwable t) {
                                Log.d(TAG, "onFailure: " + t.getMessage());
                            }
                        });
                    }
                });
            } catch (SecurityException e) {
                Log.d(TAG, "onReceive: " + e.getMessage());
            }
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
                    startAlarm();
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
