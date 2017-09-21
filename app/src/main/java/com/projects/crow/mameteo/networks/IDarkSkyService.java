package com.projects.crow.mameteo.networks;

import com.projects.crow.mameteo.networks.models.Forecast;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Venom on 07/09/2017.
 */

public interface IDarkSkyService {

    @GET("{latitude},{longitude}")
    Call<Forecast> getForecast(@Path("latitude") double latitude,
                               @Path("longitude") double longitude,
                               @Query("lang") String language,
                               @Query("units") String units);

}
