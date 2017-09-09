package com.projects.crow.mameteo.database.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Venom on 07/09/2017.
 */

public class Currently {

    @SerializedName("time")
    private long mTime;
    @SerializedName("summary")
    private String mSummary;
    @SerializedName("icon")
    private String mIcon;
    @SerializedName("temperature")
    private double mTemperature;

    public long getTime() {
        return mTime;
    }

    public String getSummary() {
        return mSummary;
    }

    public String getIcon() {
        return mIcon;
    }

    public double getTemperature() {
        return mTemperature;
    }
}
