package com.projects.crow.mameteo.utils;

import android.content.Context;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.projects.crow.mameteo.R;
import com.projects.crow.mameteo.database.models.Forecast;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.DecimalFormat;
import java.util.Locale;

/**
 * Created by Venom on 10/09/2017.
 */

public class MaMeteoUtils {

    private static final String TAG = "MaMeteoUtils";
    private static final String localFile = "forecast.json";

    public static final String PREFS = "myPrefs";

    public static final String[] PERMISSIONS = {"android.permission.ACCESS_FINE_LOCATION", "android.permission.ACCESS_COARSE_LOCATION"};
    public static final int REQUEST_CODE = 200;

    public static final String CITYNAME = "cityName";
    public static final String STATENAME = "stateName";
    public static final String COUNTRYNAME = "countryName";
    public static final String LATITUDE = "latitude";
    public static final String LONGITUDE = "longitude";

    public static final String CURRENTLY = "currently";
    public static final String DAILY = "daily";
    public static final String HOURLY = "hourly";

    public static final String UPDATE_FORECAST = "updateForecast";

    public static boolean isInternetConnectionAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = null;
        if (connectivityManager != null) {
            networkInfo = connectivityManager.getActiveNetworkInfo();
        }
        return networkInfo != null && networkInfo.isConnectedOrConnecting();
    }

    public static boolean isProviderEnabled(Context context, String providerType) {
        LocationManager lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        return lm!= null && lm.isProviderEnabled(providerType);
    }

    public static void writeToFile(Context context, Forecast forecast) {
        try {
            Gson gson = new GsonBuilder().create();
            String data = gson.toJson(forecast);

            FileOutputStream fos = context.openFileOutput(localFile, Context.MODE_PRIVATE);
            OutputStreamWriter osw = new OutputStreamWriter(fos);
            osw.write(data);
            osw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Forecast readFromFile(Context context) {
        try {
            FileInputStream fis = context.openFileInput(localFile);
            if (fis != null) {
                InputStreamReader isr = new InputStreamReader(fis);
                BufferedReader br = new BufferedReader(isr);
                StringBuilder sb = new StringBuilder();

                String receiveString;
                while ((receiveString = br.readLine()) != null) {
                    sb.append(receiveString);
                }
                fis.close();
                String ret = sb.toString();
                Gson gson = new GsonBuilder().create();
                return gson.fromJson(ret, Forecast.class);
            } else {
                writeToFile(context, new Forecast());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new Forecast();
    }

    public static String formatToCelsius(double temperature) {
        return String.valueOf(new DecimalFormat("#").format(temperature)) + " C°";
    }

    public static String windspeedFormat(float windspeed) {
        return String.format(Locale.getDefault(), "%.2f MPH", windspeed);
    }

    public static String percentageFormat(float value) {
        return String.format(Locale.getDefault(), "%.2f", value) + " %";
    }

    public static int getIconByName(String icon) {
        switch (icon) {
            case "clear-day":
                return R.drawable.ic_monocolor_clear_day;
            case "clear-night":
                return R.drawable.ic_monocolor_clear_night;
            case "rain":
                return R.drawable.ic_monocolor_rain;
            case "snow":
                return R.drawable.ic_monocolor_snow;
            case "fog":
                return R.drawable.ic_monocolor_fog;
            case "cloudy":
                return R.drawable.ic_monocolor_cloudy;
            case "partly-cloudy-day":
                return R.drawable.ic_monocolor_partly_cloudy_day;
            case "partly-cloudy-night":
                return R.drawable.ic_monocolor_partly_cloudy_night;
            default:
                return R.drawable.ic_monocolor_clear_day;
        }
    }
}
