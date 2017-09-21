package com.projects.crow.mameteo.activities;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.projects.crow.mameteo.R;
import com.projects.crow.mameteo.adapters.PeriodAdapter;
import com.projects.crow.mameteo.networks.models.Datum;
import com.projects.crow.mameteo.networks.models.Forecast;
import com.projects.crow.mameteo.receivers.WeatherBootReceiver;
import com.projects.crow.mameteo.utils.DateUtils;
import com.projects.crow.mameteo.utils.MaMeteoUtils;
import com.projects.crow.mameteo.utils.services.EnhancedSharedPreferences;
import com.projects.crow.mameteo.utils.services.LocationService;
import com.projects.crow.mameteo.utils.services.PermissionsService;
import com.projects.crow.mameteo.utils.views.SpacesItemDecoration;

import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity
        implements LocationService.OnLocationServiceListener {

    private static final String TAG = "MainActivity";
    private PermissionsService mPermissionsService;

    private EnhancedSharedPreferences mPreferences;
    private LocationService mLocationService;

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
                .make(findViewById(android.R.id.content), R.string.permissions_request, Snackbar.LENGTH_INDEFINITE)
                .setAction(R.string.see_permissions,
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                ArrayList<String> unacceptedPermissions = mPermissionsService.findUnacceptedPermissions(Arrays.asList(MaMeteoUtils.PERMISSIONS));
                                String[] permissionsArray = new String[unacceptedPermissions.size()];

                                for (int i = 0; i < unacceptedPermissions.size(); i++)
                                    permissionsArray[i] = unacceptedPermissions.get(i);
                                requestPermissions(permissionsArray, MaMeteoUtils.REQUEST_CODE);
                            }
                        });

        SharedPreferences preferences = getBaseContext().getSharedPreferences(MaMeteoUtils.PREFS, Context.MODE_PRIVATE);
        mPreferences = new EnhancedSharedPreferences(preferences);
        mLocationService = new LocationService(this, mPreferences);
        mLocationService.setListener(this);
        startAlarm();
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
                mLocationService.updateLocation();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void startAlarm() {
        if (isAllPermissionsAccepted()) {
            mLocationService.updateLocation();
            Intent alarm = new Intent(MainActivity.this, WeatherBootReceiver.class);
            boolean alarmRunning = (PendingIntent.getBroadcast(this, 0, alarm, PendingIntent.FLAG_NO_CREATE) != null);
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
        mPermissionsService = new PermissionsService(this, mPreferences);

        ArrayList<String> unaskedPermissions = mPermissionsService.findUnAskedPermissions(Arrays.asList(MaMeteoUtils.PERMISSIONS));

        if (!unaskedPermissions.isEmpty()) {
            String[] permissionsArray = new String[unaskedPermissions.size()];

            for (int i = 0; i < unaskedPermissions.size(); i++)
                permissionsArray[i] = unaskedPermissions.get(i);
            requestPermissions(permissionsArray, MaMeteoUtils.REQUEST_CODE);
            return false;
        } else {
            ArrayList<String> unacceptedPermission = mPermissionsService.findUnacceptedPermissions(Arrays.asList(MaMeteoUtils.PERMISSIONS));
            if (!unacceptedPermission.isEmpty())
                return false;
        }

        return true;
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
                        mPermissionsService.markAsAsked(permissions[i]);

                    ArrayList<String> unacceptedPermissions = mPermissionsService.findUnacceptedPermissions(Arrays.asList(MaMeteoUtils.PERMISSIONS));
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
    public void onNewLocation(Forecast forecast) {
        MaMeteoUtils.initNotification(this, forecast);
        updateUI(forecast);
    }
}
