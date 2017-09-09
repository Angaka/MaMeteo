package com.projects.crow.mameteo.database.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Venom on 07/09/2017.
 */

public class Weather {

    @SerializedName("latitude")
    private double mLatitude;
    @SerializedName("longitude")
    private double mLongitude;
    @SerializedName("timezone")
    private String mTimezone;

    @SerializedName("currently")
    private Currently mCurrently;
}
