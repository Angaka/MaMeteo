package com.projects.crow.mameteo.database;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.projects.crow.mameteo.database.models.Forecast;
import com.projects.crow.mameteo.utils.MaMeteoUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Venom on 07/09/2017.
 */

public class DatabaseHelper {

    private static final String TAG = "DatabaseHelper";

    private static final String API_KEY = "c991c7c73f94c21cc675bb9e13d9fadf";
    private static final String URL_ENDPOINT = "https://api.darksky.net/forecast/" + API_KEY + "/";

    private static Context mContext;
    private static DatabaseHelper mInstance;
    private SharedPreferences mPreferences;
    private IDarkSkyService mService;

    private Forecast mForecast;

    public DatabaseHelper() {
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

    public Forecast getLastForecast() {
        Gson gson = new GsonBuilder().create();
        return mForecast = gson.fromJson(MaMeteoUtils.readFromFile(mContext), Forecast.class);
    }

    public Forecast getForecast(double latitude, double longitude) {
        mService.getForecast(latitude, longitude).enqueue(new Callback<Forecast>() {
            @Override
            public void onResponse(Call<Forecast> call, Response<Forecast> response) {
                if (response.code() == 200) {
                    mForecast = response.body();
                    Gson gson = new GsonBuilder().create();
                    String data = gson.toJson(mForecast);
                    MaMeteoUtils.writeToFile(mContext, data);
                }
            }

            @Override
            public void onFailure(Call<Forecast> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());
            }
        });

        return mForecast;
    }
}
