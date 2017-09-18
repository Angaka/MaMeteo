package com.projects.crow.mameteo.database;

import android.content.Context;
import android.util.Log;

import com.projects.crow.mameteo.database.models.Forecast;
import com.projects.crow.mameteo.utils.MaMeteoUtils;

import java.io.IOException;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Venom on 07/09/2017.
 */

public class DatabaseHelper {

    private static final String TAG = "DatabaseHelper";

    private static final String API_KEY = "c991c7c73f94c21cc675bb9e13d9fadf";
    private static final String URL_ENDPOINT = "https://api.darksky.net/forecast/" + API_KEY + "/";

    private static DatabaseHelper mInstance;
    private IDarkSkyService mService;

    private Forecast mForecast;

    private DatabaseHelper() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(URL_ENDPOINT)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        mService = retrofit.create(IDarkSkyService.class);
        mForecast = new Forecast();
    }

    public static DatabaseHelper getInstance() {
        if (mInstance == null) {
            mInstance = new DatabaseHelper();
        }
        return mInstance;
    }

    public Forecast mgetLastForecast(Context context) {
        mForecast = MaMeteoUtils.readFromFile(context);
        Log.d(TAG, "getLastForecast: " + mForecast.getTimezone());
        return mForecast;
    }

    public Forecast getForecast(Context context, double latitude, double longitude) {
        try {
            mForecast = mService.getForecast(latitude, longitude).execute().body();
            Log.d(TAG, "getForecast: " + mForecast.toString());
            MaMeteoUtils.writeToFile(context, mForecast);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return mForecast;
    }
}
