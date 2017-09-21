package com.projects.crow.mameteo.networks.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Venom on 07/09/2017.
 */

public class Forecast {
    @SerializedName("latitude")
    @Expose
    private float latitude = 0;
    @SerializedName("longitude")
    @Expose
    private float longitude = 0;
    @SerializedName("timezone")
    @Expose
    private String timezone = "";
    @SerializedName("currently")
    @Expose
    private Currently currently = new Currently();
    @SerializedName("minutely")
    @Expose
    private Minutely minutely = new Minutely();
    @SerializedName("hourly")
    @Expose
    private Hourly hourly = new Hourly();
    @SerializedName("daily")
    @Expose
    private Daily daily = new Daily();
    @SerializedName("flags")
    @Expose
    private Flags flags = new Flags();
    @SerializedName("offset")
    @Expose
    private int offset = 0;

    public float getLatitude() {
        return latitude;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public float getLongitude() {
        return longitude;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    public Currently getCurrently() {
        return currently;
    }

    public void setCurrently(Currently currently) {
        this.currently = currently;
    }

    public Minutely getMinutely() {
        return minutely;
    }

    public void setMinutely(Minutely minutely) {
        this.minutely = minutely;
    }

    public Hourly getHourly() {
        return hourly;
    }

    public void setHourly(Hourly hourly) {
        this.hourly = hourly;
    }

    public Daily getDaily() {
        return daily;
    }

    public void setDaily(Daily daily) {
        this.daily = daily;
    }

    public Flags getFlags() {
        return flags;
    }

    public void setFlags(Flags flags) {
        this.flags = flags;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    @Override
    public String toString() {
        return "Forecast{" +
                "latitude=" + latitude +
                ", longitude=" + longitude +
                ", timezone='" + timezone + '\'' +
                ", currently=" + currently +
                ", minutely=" + minutely +
                ", hourly=" + hourly +
                ", daily=" + daily +
                ", flags=" + flags +
                ", offset=" + offset +
                '}';
    }
}
