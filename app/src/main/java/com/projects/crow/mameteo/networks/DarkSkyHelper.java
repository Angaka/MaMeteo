package com.projects.crow.mameteo.networks;

import android.content.Context;

import com.projects.crow.mameteo.BuildConfig;
import com.projects.crow.mameteo.networks.models.Forecast;
import com.projects.crow.mameteo.utils.MaMeteoUtils;

import java.io.IOException;
import java.util.Locale;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Venom on 07/09/2017.
 */

public class DarkSkyHelper {

    private static final String TAG = "DarkSkyHelper";

    private static final String API_KEY = BuildConfig.API_KEY;
    private static final String URL_ENDPOINT = "https://api.darksky.net/forecast/" + API_KEY + "/";

    private static DarkSkyHelper mInstance;
    private IDarkSkyService mService;

    private Forecast mForecast;

    private DarkSkyHelper() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(URL_ENDPOINT)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        mService = retrofit.create(IDarkSkyService.class);
        mForecast = new Forecast();
    }

    public static DarkSkyHelper getInstance() {
        if (mInstance == null) {
            mInstance = new DarkSkyHelper();
        }
        return mInstance;
    }

    public Forecast getLastForecast(Context context) {
        mForecast = MaMeteoUtils.readFromFile(context);
        return mForecast;
    }

    public Forecast getForecast(Context context, double latitude, double longitude) {
        try {
            mForecast = mService.getForecast(latitude, longitude, Locale.getDefault().getLanguage(), "si").execute().body();
            MaMeteoUtils.writeToFile(context, mForecast);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return mForecast;
    }
}
