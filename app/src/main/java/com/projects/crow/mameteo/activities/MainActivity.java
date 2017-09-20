package com.projects.crow.mameteo.activities;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
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
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.format.Time;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RemoteViews;
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
import com.projects.crow.mameteo.utils.SpacesItemDecoration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity
        implements View.OnClickListener {

    private static final String TAG = "MainActivity";
    private PermissionsUtils mPermissionsUtils;

    private EnhancedSharedPreferences mPreferences;

    private Toolbar mToolbar;
    private TextView mTvSummary;
    private TextView mTvWindspeed;
    private TextView mTvHumidity;
    private TextView mTvTemperature;

    private RecyclerView mRvDaily;
    private RecyclerView mRvHourly;

    private PeriodAdapter mDailyAdapter;
    private PeriodAdapter mHourlyAdapter;

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

        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        mTvSummary = findViewById(R.id.text_view_summary);
        mTvWindspeed = findViewById(R.id.text_view_windspeed);
        mTvHumidity = findViewById(R.id.text_view_humidity);
        mTvTemperature = findViewById(R.id.text_view_temperature);

        mRvDaily = findViewById(R.id.recycler_view_daily);
        mDailyAdapter = new PeriodAdapter(this, new ArrayList<Datum>(), MaMeteoUtils.DAILY);
        LinearLayoutManager dailyLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mRvDaily.setLayoutManager(dailyLayoutManager);
        SpacesItemDecoration itemDecoration = new SpacesItemDecoration(30);
        mRvDaily.addItemDecoration(itemDecoration);
        mRvDaily.setAdapter(mDailyAdapter);

        mRvHourly = findViewById(R.id.recycler_view_hourly);
        mHourlyAdapter = new PeriodAdapter(this, new ArrayList<Datum>(), MaMeteoUtils.HOURLY);
        LinearLayoutManager hourlyLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRvHourly.setLayoutManager(hourlyLayoutManager);
        mRvHourly.setAdapter(mHourlyAdapter);

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_refresh:
                updateLocation();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initNotification(Forecast forecast) {
        RemoteViews contentView = new RemoteViews(getPackageName(), R.layout.view_notification);
        contentView.setImageViewResource(R.id.image_view_icon, MaMeteoUtils.getIconByName(forecast.getCurrently().getIcon()));
        contentView.setTextViewText(R.id.text_view_summary, forecast.getCurrently().getSummary());
        contentView.setTextViewText(R.id.text_view_location, forecast.getTimezone());
        contentView.setTextViewText(R.id.text_view_temperature, MaMeteoUtils.formatToCelsius(forecast.getCurrently().getTemperature()));

        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        Notification notification = new Notification.Builder(this)
                .setSmallIcon(MaMeteoUtils.getIconByName(forecast.getCurrently().getIcon()))
                .setContent(contentView)
                .setContentIntent(pendingIntent)
                .setAutoCancel(false)
                .setOngoing(true)
                .build();

        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if (notificationManager != null) {
            notificationManager.notify(0, notification);
        }
    }

    private void startAlarm() {
        if (isAllPermissionsAccepted()) {
            updateLocation();
            Intent alarm = new Intent(MainActivity.this, WeatherBootReceiver.class);
            boolean alarmRunning = (PendingIntent.getBroadcast(this, 0, alarm, PendingIntent.FLAG_NO_CREATE) != null);
            Log.d(TAG, "startAlarm: " + alarmRunning);
            if (!alarmRunning) {
                PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, alarm, PendingIntent.FLAG_UPDATE_CURRENT);
                AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
                if (alarmManager != null) {
                    alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, DateUtils.getNextHour().getTime(), AlarmManager.INTERVAL_HOUR, pendingIntent);
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
        mToolbar.setTitle(forecast.getTimezone());
        mTvTemperature.setText(MaMeteoUtils.formatToCelsius(forecast.getCurrently().getTemperature()));
        mTvSummary.setCompoundDrawablesWithIntrinsicBounds(MaMeteoUtils.getIconByName(forecast.getCurrently().getIcon()), 0, 0, 0);
        mTvSummary.setText(forecast.getCurrently().getSummary());
        mTvWindspeed.setText(MaMeteoUtils.windspeedFormat(forecast.getCurrently().getWindSpeed()));
        mTvHumidity.setText(MaMeteoUtils.percentageFormat(forecast.getCurrently().getHumidity()));

        mDailyAdapter.updateDatas(forecast.getDaily().getData().subList(1, 6));
        mHourlyAdapter.updateDatas(forecast.getHourly().getData().subList(0, 23));
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

            if (!isLocationEnabled())
                return db.getLastForecast(mContext);
            return db.getForecast(mContext, mLatitude, mLongitude);
        }

        @Override
        protected void onPostExecute(Forecast forecast) {
            initNotification(forecast);
            updateUI(forecast);
        }
    }
}
