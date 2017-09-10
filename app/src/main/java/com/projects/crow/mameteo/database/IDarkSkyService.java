package com.projects.crow.mameteo.database;

import com.projects.crow.mameteo.database.models.Forecast;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by Venom on 07/09/2017.
 */

public interface IDarkSkyService {

    @GET("{latitude},{longitude}")
    Call<Forecast> getForecast(@Path("latitude") double latitude, @Path("longitude") double longitude);

}
