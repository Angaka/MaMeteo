package com.projects.crow.mameteo.database.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Venom on 07/09/2017.
 */

public class Currently {

    @SerializedName("time")
    @Expose
    private int time;
    @SerializedName("summary")
    @Expose
    private String summary;
    @SerializedName("icon")
    @Expose
    private String icon;
    @SerializedName("nearestStormDistance")
    @Expose
    private int nearestStormDistance;
    @SerializedName("temperature")
    @Expose
    private float temperature;
    @SerializedName("apparentTemperature")
    @Expose
    private float apparentTemperature;
    @SerializedName("humidity")
    @Expose
    private float humidity;
    @SerializedName("pressure")
    @Expose
    private float pressure;
    @SerializedName("windSpeed")
    @Expose
    private float windSpeed;
    @SerializedName("windGust")
    @Expose
    private float windGust;
    @SerializedName("windBearing")
    @Expose
    private int windBearing;
    @SerializedName("cloudCover")
    @Expose
    private float cloudCover;
    @SerializedName("uvIndex")
    @Expose
    private int uvIndex;
    @SerializedName("visibility")
    @Expose
    private float visibility;
    @SerializedName("ozone")
    @Expose
    private float ozone;

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

    public int getNearestStormDistance() {
        return nearestStormDistance;
    }

    public void setNearestStormDistance(int nearestStormDistance) {
        this.nearestStormDistance = nearestStormDistance;
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

    public float getVisibility() {
        return visibility;
    }

    public void setVisibility(float visibility) {
        this.visibility = visibility;
    }

    public float getOzone() {
        return ozone;
    }

    public void setOzone(float ozone) {
        this.ozone = ozone;
    }

    @Override
    public String toString() {
        return "Currently{" +
                "time=" + time +
                ", summary='" + summary + '\'' +
                ", icon='" + icon + '\'' +
                ", nearestStormDistance=" + nearestStormDistance +
                ", temperature=" + temperature +
                ", apparentTemperature=" + apparentTemperature +
                ", humidity=" + humidity +
                ", pressure=" + pressure +
                ", windSpeed=" + windSpeed +
                ", windGust=" + windGust +
                ", windBearing=" + windBearing +
                ", cloudCover=" + cloudCover +
                ", uvIndex=" + uvIndex +
                ", visibility=" + visibility +
                ", ozone=" + ozone +
                '}';
    }
}
