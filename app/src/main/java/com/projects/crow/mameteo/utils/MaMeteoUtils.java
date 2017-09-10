package com.projects.crow.mameteo.utils;

import android.content.Context;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

/**
 * Created by Venom on 10/09/2017.
 */

public class MaMeteoUtils {

    public static final String PREFS = "myPrefs";

    public static final String[] PERMISSIONS = {"android.permission.ACCESS_FINE_LOCATION", "android.permission.ACCESS_COARSE_LOCATION"};
    public static final int REQUEST_CODE = 200;

    public static final String CITYNAME = "cityName";
    public static final String STATENAME = "stateName";
    public static final String COUNTRYNAME = "countryName";
    public static final String LATITUDE = "latitude";
    public static final String LONGITUDE = "longitude";
    public static final String UPDATE_FORECAST = "updateForecast";

    public static boolean isInternetConnectionAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnectedOrConnecting();
    }

    public static boolean isProviderEnabled(Context context, String providerType) {
        LocationManager lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        return lm.isProviderEnabled(providerType);
    }

    public static void writeToFile(Context context, String data) {
        try {
            OutputStreamWriter osw = new OutputStreamWriter(context.openFileOutput("forecast.json", Context.MODE_PRIVATE));
            osw.write(data);
            osw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String readFromFile(Context context) {
        String ret = "";
        try {
            InputStream is = context.openFileInput("forecast.json");

            if (is != null) {
                InputStreamReader isr = new InputStreamReader(is);
                BufferedReader br = new BufferedReader(isr);
                String receiveString = "";
                StringBuilder sb = new StringBuilder();

                while ((receiveString = br.readLine()) != null)
                    sb.append(receiveString);
                is.close();
                ret = sb.toString();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return ret;
    }
}
