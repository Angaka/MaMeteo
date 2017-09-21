package com.projects.crow.mameteo.utils.services;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

import com.projects.crow.mameteo.R;
import com.projects.crow.mameteo.threads.UpdateForecastTask;
import com.projects.crow.mameteo.networks.models.Forecast;
import com.projects.crow.mameteo.utils.MaMeteoUtils;

/**
 * Created by Venom on 20/09/2017.
 */

public class LocationService {
    private static final String TAG = "LocationService";

    private Context mContext;
    private EnhancedSharedPreferences mPreferences;

    private OnLocationServiceListener mListener;

    public LocationService(Context context, EnhancedSharedPreferences preferences) {
        mContext = context;
        mPreferences = preferences;
    }

    public void setListener(Context context) {
        mListener = (OnLocationServiceListener) context;
    }

    public void updateLocation() {
        if (!isLocationEnabled(mContext)) {
            launchTask();
            Toast.makeText(mContext, R.string.gps_or_internet_disabled, Toast.LENGTH_SHORT).show();
        } else {
            try {
                LocationManager locationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
                Location location = null;
                if (locationManager != null && locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))
                    location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                if (locationManager != null && locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER))
                    location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                if (location != null) {
                    EnhancedSharedPreferences.Editor editor = mPreferences.edit();
                    editor.putDouble(MaMeteoUtils.LATITUDE, location.getLatitude());
                    editor.putDouble(MaMeteoUtils.LONGITUDE, location.getLongitude());
                    editor.apply();
                    launchTask();
                }
            } catch (SecurityException e) {
                e.printStackTrace();
            }
        }
    }

    private void launchTask() {
        double latitude = mPreferences.getDouble(MaMeteoUtils.LATITUDE, 0.0);
        double longitude = mPreferences.getDouble(MaMeteoUtils.LONGITUDE, 0.0);

        UpdateForecastTask updateForecastTask = new UpdateForecastTask(mContext, latitude, longitude);
        updateForecastTask.setListener(new UpdateForecastTask.OnUpdateForecastTaskListener() {
            @Override
            public void onPostExecute(Context context, Forecast forecast) {
                MaMeteoUtils.initNotification(context, forecast);
                if (mListener != null)
                    mListener.onNewLocation(forecast);
            }
        });
        updateForecastTask.execute();
    }

    private static boolean isInternetConnectionAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = null;
        if (connectivityManager != null) {
            networkInfo = connectivityManager.getActiveNetworkInfo();
        }
        return networkInfo != null && networkInfo.isConnectedOrConnecting();
    }

    private static boolean isProviderEnabled(Context context, String providerType) {
        LocationManager lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        return lm!= null && lm.isProviderEnabled(providerType);
    }

    public static boolean isLocationEnabled(Context context) {
        boolean isProviderEnabled = isProviderEnabled(context, LocationManager.GPS_PROVIDER);
        boolean isInternetConnected = isInternetConnectionAvailable(context);
        return isProviderEnabled && isInternetConnected;
    }

    public interface OnLocationServiceListener {
        void onNewLocation(Forecast forecast);
    }
}
