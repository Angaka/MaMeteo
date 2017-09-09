package com.projects.crow.mameteo.database;

import android.content.Context;
import android.content.SharedPreferences;

import com.projects.crow.mameteo.database.models.Weather;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Venom on 07/09/2017.
 */

public class DatabaseHelper {

    private static final String API_KEY = "c991c7c73f94c21cc675bb9e13d9fadf";
    private static final String URL_ENDPOINT = "https://api.darksky.net/forecast/" + API_KEY + "/";

    private static Context mContext;
    private static DatabaseHelper mInstance;
    private SharedPreferences mPreferences;
    private IDarkSkyService mService;

    public DatabaseHelper() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(URL_ENDPOINT)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        mService = retrofit.create(IDarkSkyService.class);
    }

    public static DatabaseHelper getInstance() {
        if (mInstance == null) {
            mInstance = new DatabaseHelper();
        }
        return mInstance;
    }

    public Call<Weather> getWeather(double latitude, double longitude) {
        return mService.getForecast(latitude, longitude);
    }
}
