package com.projects.crow.mameteo.database.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Venom on 09/09/2017.
 */

public class Datum_ {
    @SerializedName("time")
    @Expose
    private int time = 0;
    @SerializedName("summary")
    @Expose
    private String summary = "";
    @SerializedName("icon")
    @Expose
    private String icon = "";
    @SerializedName("temperature")
    @Expose
    private float temperature = 0;
    @SerializedName("apparentTemperature")
    @Expose
    private float apparentTemperature = 0;
    @SerializedName("dewPoint")
    @Expose
    private float dewPoint = 0;
    @SerializedName("humidity")
    @Expose
    private float humidity = 0;
    @SerializedName("pressure")
    @Expose
    private float pressure = 0;
    @SerializedName("windSpeed")
    @Expose
    private float windSpeed = 0;
    @SerializedName("windGust")
    @Expose
    private float windGust = 0;
    @SerializedName("windBearing")
    @Expose
    private int windBearing = 0;
    @SerializedName("cloudCover")
    @Expose
    private float cloudCover = 0;
    @SerializedName("uvIndex")
    @Expose
    private int uvIndex = 0;
    @SerializedName("ozone")
    @Expose
    private float ozone = 0;
    @SerializedName("precipType")
    @Expose
    private String precipType = "";

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public float getTemperature() {
        return temperature;
    }

    public void setTemperature(float temperature) {
        this.temperature = temperature;
    }

    public float getApparentTemperature() {
        return apparentTemperature;
    }

    public void setApparentTemperature(float apparentTemperature) {
        this.apparentTemperature = apparentTemperature;
    }

    public float getDewPoint() {
        return dewPoint;
    }

    public void setDewPoint(float dewPoint) {
        this.dewPoint = dewPoint;
    }

    public float getHumidity() {
        return humidity;
    }

    public void setHumidity(float humidity) {
        this.humidity = humidity;
    }

    public float getPressure() {
        return pressure;
    }

    public void setPressure(float pressure) {
        this.pressure = pressure;
    }

    public float getWindSpeed() {
        return windSpeed;
    }

    public void setWindSpeed(float windSpeed) {
        this.windSpeed = windSpeed;
    }

    public float getWindGust() {
        return windGust;
    }

    public void setWindGust(float windGust) {
        this.windGust = windGust;
    }

    public int getWindBearing() {
        return windBearing;
    }

    public void setWindBearing(int windBearing) {
        this.windBearing = windBearing;
    }

    public float getCloudCover() {
        return cloudCover;
    }

    public void setCloudCover(float cloudCover) {
        this.cloudCover = cloudCover;
    }

    public int getUvIndex() {
        return uvIndex;
    }

    public void setUvIndex(int uvIndex) {
        this.uvIndex = uvIndex;
    }

    public float getOzone() {
        return ozone;
    }

    public void setOzone(float ozone) {
        this.ozone = ozone;
    }

    public String getPrecipType() {
        return precipType;
    }

    public void setPrecipType(String precipType) {
        this.precipType = precipType;
    }

    @Override
    public String toString() {
        return "Datum_{" +
                "time=" + time +
                ", summary='" + summary + '\'' +
                ", icon='" + icon + '\'' +
                ", temperature=" + temperature +
                ", apparentTemperature=" + apparentTemperature +
                ", dewPoint=" + dewPoint +
                ", humidity=" + humidity +
                ", pressure=" + pressure +
                ", windSpeed=" + windSpeed +
                ", windGust=" + windGust +
                ", windBearing=" + windBearing +
                ", cloudCover=" + cloudCover +
                ", uvIndex=" + uvIndex +
                ", ozone=" + ozone +
                ", precipType='" + precipType + '\'' +
                '}';
    }
}
