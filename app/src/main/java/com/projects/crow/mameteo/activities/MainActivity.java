package com.projects.crow.mameteo.activities;

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
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.projects.crow.mameteo.R;
import com.projects.crow.mameteo.WeatherBootReceiver;
import com.projects.crow.mameteo.adapters.PeriodAdapter;
import com.projects.crow.mameteo.database.DatabaseHelper;
import com.projects.crow.mameteo.database.models.Datum;
import com.projects.crow.mameteo.database.models.Forecast;
import com.projects.crow.mameteo.utils.DateUtils;
import com.projects.crow.mameteo.utils.EnhancedSharedPreferences;
import com.projects.crow.mameteo.utils.MaMeteoUtils;
import com.projects.crow.mameteo.utils.PermissionsUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity
        implements View.OnClickListener {

    private static final String TAG = "MainActivity";
    private PermissionsUtils mPermissionsUtils;

    private EnhancedSharedPreferences mPreferences;

    private TextView mTvLocation;
    private ImageView mIvIcon;
    private TextView mTvSummary;
    private TextView mTvWindspeed;
    private TextView mTvTemperature;

    private TextView mTvPeriod;
    private RecyclerView mRvDaily;

    private PeriodAdapter mPeriodAdapter;

    private Snackbar mSnackBarPermissions;

    private BroadcastReceiver mUpdateDataReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d(TAG, "onReceive: updateData");
            updateLocation();
        }
    };
    private boolean mCheckIfReceiverRegistered = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTvLocation = findViewById(R.id.text_view_location);
        mIvIcon = findViewById(R.id.image_view_icon);
        mTvSummary = findViewById(R.id.text_view_summary);
        mTvWindspeed = findViewById(R.id.text_view_windspeed);
        mTvTemperature = findViewById(R.id.text_view_temperature);

        mTvPeriod = findViewById(R.id.text_view_period);
        mTvPeriod.setText(MaMeteoUtils.HOURLY);
        mRvDaily = findViewById(R.id.recycler_view_daily);
        mPeriodAdapter = new PeriodAdapter(this, new ArrayList<Datum>(), MaMeteoUtils.HOURLY);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mRvDaily.setLayoutManager(layoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, layoutManager.getOrientation());
        mRvDaily.addItemDecoration(dividerItemDecoration);
        mRvDaily.setAdapter(mPeriodAdapter);

        mSnackBarPermissions = Snackbar
                .make(getWindow().getDecorView().getRootView(), R.string.permissions_request, Snackbar.LENGTH_INDEFINITE)
                .setAction(R.string.see_permissions,
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                ArrayList<String> unacceptedPermissions = mPermissionsUtils.findUnacceptedPermissions(Arrays.asList(MaMeteoUtils.PERMISSIONS));
                                String[] permissionsArray = new String[unacceptedPermissions.size()];

                                for (int i = 0; i < unacceptedPermissions.size(); i++)
                                    permissionsArray[i] = unacceptedPermissions.get(i);
                                requestPermissions(permissionsArray, MaMeteoUtils.REQUEST_CODE);
                            }
                        });

        SharedPreferences preferences = getBaseContext().getSharedPreferences(MaMeteoUtils.PREFS, Context.MODE_PRIVATE);
        mPreferences = new EnhancedSharedPreferences(preferences);

        startAlarm();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!mCheckIfReceiverRegistered) {
            mCheckIfReceiverRegistered = true;
            LocalBroadcastManager.getInstance(this).registerReceiver(mUpdateDataReceiver, new IntentFilter(MaMeteoUtils.UPDATE_FORECAST));
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
            updateLocation();
            Intent alarm = new Intent(MainActivity.this, WeatherBootReceiver.class);
            boolean alarmRunning = (PendingIntent.getBroadcast(this, 0, alarm, PendingIntent.FLAG_NO_CREATE) != null);
            if (!alarmRunning) {
                PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, alarm, PendingIntent.FLAG_UPDATE_CURRENT);
                AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
                if (alarmManager != null) {
                    alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, DateUtils.getNextHour().getTime(), AlarmManager.INTERVAL_HOUR, pendingIntent);
                }
            }
        } else {
            if (!mSnackBarPermissions.isShown())
                mSnackBarPermissions.show();
        }
    }

    private boolean isAllPermissionsAccepted() {
        mPermissionsUtils = new PermissionsUtils(this, mPreferences);

        ArrayList<String> unaskedPermissions = mPermissionsUtils.findUnAskedPermissions(Arrays.asList(MaMeteoUtils.PERMISSIONS));

        if (!unaskedPermissions.isEmpty()) {
            String[] permissionsArray = new String[unaskedPermissions.size()];

            for (int i = 0; i < unaskedPermissions.size(); i++)
                permissionsArray[i] = unaskedPermissions.get(i);
            requestPermissions(permissionsArray, MaMeteoUtils.REQUEST_CODE);
            return false;
        } else {
            ArrayList<String> unacceptedPermission = mPermissionsUtils.findUnacceptedPermissions(Arrays.asList(MaMeteoUtils.PERMISSIONS));
            if (!unacceptedPermission.isEmpty())
                return false;
        }

        return true;
    }

    private void updateLocation() {
        if (!isLocationEnabled()) {
            launchTask();
            Toast.makeText(this, R.string.gps_or_internet_disabled, Toast.LENGTH_SHORT).show();
        } else {
            try {
                FusedLocationProviderClient fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
                fusedLocationClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        Geocoder geocoder = new Geocoder(getBaseContext(), Locale.getDefault());
                        try {
                            List<Address> addressList = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);

                            if (!addressList.isEmpty()) {
                                Address nearestCity = addressList.get(0);
                                if (nearestCity.hasLatitude() && nearestCity.hasLongitude()) {
                                    EnhancedSharedPreferences.Editor editor = mPreferences.edit();
                                    editor.putString(MaMeteoUtils.CITYNAME, nearestCity.getAddressLine(0));
                                    editor.putString(MaMeteoUtils.STATENAME, nearestCity.getAddressLine(1));
                                    editor.putString(MaMeteoUtils.COUNTRYNAME, nearestCity.getAddressLine(2));
                                    editor.putDouble(MaMeteoUtils.LATITUDE, nearestCity.getLatitude());
                                    editor.putDouble(MaMeteoUtils.LONGITUDE, nearestCity.getLongitude());
                                    editor.apply();

                                    launchTask();
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
    }

    private void launchTask() {
        double latitude = mPreferences.getDouble(MaMeteoUtils.LATITUDE, 0.0);
        double longitude = mPreferences.getDouble(MaMeteoUtils.LONGITUDE, 0.0);

        Log.d(TAG, "updateLocation: " + latitude + " " + longitude);
        UpdateForecastTask updateForecastTask = new UpdateForecastTask(MainActivity.this, latitude, longitude);
        updateForecastTask.execute();
    }

    private void updateUI(Forecast forecast) {
        mTvLocation.setText(forecast.getTimezone());
        mIvIcon.setImageResource(MaMeteoUtils.getIconByName(this, forecast.getCurrently().getIcon()));
        mTvSummary.setText(forecast.getCurrently().getSummary());
//        mTvWindspeed = findViewById(R.id.text_view_windspeed);
        mTvTemperature.setText(MaMeteoUtils.fahrenheitToCelsius(forecast.getCurrently().getTemperature()));

        mPeriodAdapter.updateDatas(forecast.getHourly().getData());
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case MaMeteoUtils.REQUEST_CODE:
                if (grantResults.length > 0) {
                    for (int i = 0; i < grantResults.length; i++)
                        mPermissionsUtils.markAsAsked(permissions[i]);

                    ArrayList<String> unacceptedPermissions = mPermissionsUtils.findUnacceptedPermissions(Arrays.asList(MaMeteoUtils.PERMISSIONS));
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

    private boolean isLocationEnabled() {
        boolean isProviderEnabled = MaMeteoUtils.isProviderEnabled(this, LocationManager.GPS_PROVIDER);
        boolean isInternetConnected = MaMeteoUtils.isInternetConnectionAvailable(this);
        return isProviderEnabled && isInternetConnected;
    }

    private class UpdateForecastTask extends AsyncTask<Void, Void, Forecast> {

        private Context mContext;
        private double mLatitude;
        private double mLongitude;

        private UpdateForecastTask(Context context, double latitude, double longitude) {
            mContext = context;
            mLatitude = latitude;
            mLongitude = longitude;
        }

        @Override
        protected Forecast doInBackground(Void... voids) {
            DatabaseHelper db = DatabaseHelper.getInstance();

            if (!isLocationEnabled()) {
                Log.d(TAG, "doInBackground: ");
                return db.getLastForecast(mContext);
            }
            Log.d(TAG, "doInBackground: lat long " + mLatitude +  " " + mLongitude);
            return db.getForecast(mContext, mLatitude, mLongitude);
        }

        @Override
        protected void onPostExecute(Forecast forecast) {
            updateUI(forecast);
        }
    }
}