package com.projects.crow.mameteo.networks.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Venom on 09/09/2017.
 */

public class Datum {

    @SerializedName("time")
    @Expose
    private int time = 0;
    @SerializedName("summary")
    @Expose
    private String summary = "";
    @SerializedName("icon")
    @Expose
    private String icon = "";
    @SerializedName("sunriseTime")
    @Expose
    private int sunriseTime = 0;
    @SerializedName("sunsetTime")
    @Expose
    private int sunsetTime = 0;
    @SerializedName("moonPhase")
    @Expose
    private float moonPhase = 0;
    @SerializedName("precipIntensity")
    @Expose
    private float precipIntensity = 0;
    @SerializedName("precipIntensityMax")
    @Expose
    private float precipIntensityMax = 0;
    @SerializedName("precipIntensityMaxTime")
    @Expose
    private int precipIntensityMaxTime = 0;
    @SerializedName("precipProbability")
    @Expose
    private float precipProbability = 0;
    @SerializedName("precipType")
    @Expose
    private String precipType = "";
    @SerializedName("temperature")
    @Expose
    private float temperature = 0;
    @SerializedName("temperatureHigh")
    @Expose
    private float temperatureHigh = 0;
    @SerializedName("temperatureHighTime")
    @Expose
    private int temperatureHighTime = 0;
    @SerializedName("temperatureLow")
    @Expose
    private float temperatureLow = 0;
    @SerializedName("temperatureLowTime")
    @Expose
    private int temperatureLowTime = 0;
    @SerializedName("apparentTemperatureHigh")
    @Expose
    private float apparentTemperatureHigh = 0;
    @SerializedName("apparentTemperatureHighTime")
    @Expose
    private int apparentTemperatureHighTime = 0;
    @SerializedName("apparentTemperatureLow")
    @Expose
    private float apparentTemperatureLow = 0;
    @SerializedName("apparentTemperatureLowTime")
    @Expose
    private int apparentTemperatureLowTime = 0;
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
    @SerializedName("windGustTime")
    @Expose
    private int windGustTime = 0;
    @SerializedName("windBearing")
    @Expose
    private int windBearing = 0;
    @SerializedName("cloudCover")
    @Expose
    private float cloudCover = 0;
    @SerializedName("uvIndex")
    @Expose
    private int uvIndex = 0;
    @SerializedName("uvIndexTime")
    @Expose
    private int uvIndexTime = 0;
    @SerializedName("ozone")
    @Expose
    private float ozone = 0;
    @SerializedName("temperatureMin")
    @Expose
    private float temperatureMin = 0;
    @SerializedName("temperatureMinTime")
    @Expose
    private int temperatureMinTime = 0;
    @SerializedName("temperatureMax")
    @Expose
    private float temperatureMax = 0;
    @SerializedName("temperatureMaxTime")
    @Expose
    private int temperatureMaxTime = 0;
    @SerializedName("apparentTemperatureMin")
    @Expose
    private float apparentTemperatureMin = 0;
    @SerializedName("apparentTemperatureMinTime")
    @Expose
    private int apparentTemperatureMinTime = 0;
    @SerializedName("apparentTemperatureMax")
    @Expose
    private float apparentTemperatureMax = 0;
    @SerializedName("apparentTemperatureMaxTime")
    @Expose
    private int apparentTemperatureMaxTime = 0;

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

    public int getSunriseTime() {
        return sunriseTime;
    }

    public void setSunriseTime(int sunriseTime) {
        this.sunriseTime = sunriseTime;
    }

    public int getSunsetTime() {
        return sunsetTime;
    }

    public void setSunsetTime(int sunsetTime) {
        this.sunsetTime = sunsetTime;
    }

    public float getMoonPhase() {
        return moonPhase;
    }

    public void setMoonPhase(float moonPhase) {
        this.moonPhase = moonPhase;
    }

    public float getPrecipIntensity() {
        return precipIntensity;
    }

    public void setPrecipIntensity(float precipIntensity) {
        this.precipIntensity = precipIntensity;
    }

    public float getPrecipIntensityMax() {
        return precipIntensityMax;
    }

    public void setPrecipIntensityMax(float precipIntensityMax) {
        this.precipIntensityMax = precipIntensityMax;
    }

    public int getPrecipIntensityMaxTime() {
        return precipIntensityMaxTime;
    }

    public void setPrecipIntensityMaxTime(int precipIntensityMaxTime) {
        this.precipIntensityMaxTime = precipIntensityMaxTime;
    }

    public float getPrecipProbability() {
        return precipProbability;
    }

    public void setPrecipProbability(float precipProbability) {
        this.precipProbability = precipProbability;
    }

    public String getPrecipType() {
        return precipType;
    }

    public void setPrecipType(String precipType) {
        this.precipType = precipType;
    }

    public float getTemperatureHigh() {
        return temperatureHigh;
    }

    public void setTemperatureHigh(float temperatureHigh) {
        this.temperatureHigh = temperatureHigh;
    }

    public float getTemperature() {
        return temperature;
    }

    public void setTemperature(float temperature) {
        this.temperature = temperature;
    }

    public int getTemperatureHighTime() {
        return temperatureHighTime;
    }

    public void setTemperatureHighTime(int temperatureHighTime) {
        this.temperatureHighTime = temperatureHighTime;
    }

    public float getTemperatureLow() {
        return temperatureLow;
    }

    public void setTemperatureLow(float temperatureLow) {
        this.temperatureLow = temperatureLow;
    }

    public int getTemperatureLowTime() {
        return temperatureLowTime;
    }

    public void setTemperatureLowTime(int temperatureLowTime) {
        this.temperatureLowTime = temperatureLowTime;
    }

    public float getApparentTemperatureHigh() {
        return apparentTemperatureHigh;
    }

    public void setApparentTemperatureHigh(float apparentTemperatureHigh) {
        this.apparentTemperatureHigh = apparentTemperatureHigh;
    }

    public int getApparentTemperatureHighTime() {
        return apparentTemperatureHighTime;
    }

    public void setApparentTemperatureHighTime(int apparentTemperatureHighTime) {
        this.apparentTemperatureHighTime = apparentTemperatureHighTime;
    }

    public float getApparentTemperatureLow() {
        return apparentTemperatureLow;
    }

    public void setApparentTemperatureLow(float apparentTemperatureLow) {
        this.apparentTemperatureLow = apparentTemperatureLow;
    }

    public int getApparentTemperatureLowTime() {
        return apparentTemperatureLowTime;
    }

    public void setApparentTemperatureLowTime(int apparentTemperatureLowTime) {
        this.apparentTemperatureLowTime = apparentTemperatureLowTime;
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

    public int getWindGustTime() {
        return windGustTime;
    }

    public void setWindGustTime(int windGustTime) {
        this.windGustTime = windGustTime;
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

    public int getUvIndexTime() {
        return uvIndexTime;
    }

    public void setUvIndexTime(int uvIndexTime) {
        this.uvIndexTime = uvIndexTime;
    }

    public float getOzone() {
        return ozone;
    }

    public void setOzone(float ozone) {
        this.ozone = ozone;
    }

    public float getTemperatureMin() {
        return temperatureMin;
    }

    public void setTemperatureMin(float temperatureMin) {
        this.temperatureMin = temperatureMin;
    }

    public int getTemperatureMinTime() {
        return temperatureMinTime;
    }

    public void setTemperatureMinTime(int temperatureMinTime) {
        this.temperatureMinTime = temperatureMinTime;
    }

    public float getTemperatureMax() {
        return temperatureMax;
    }

    public void setTemperatureMax(float temperatureMax) {
        this.temperatureMax = temperatureMax;
    }

    public int getTemperatureMaxTime() {
        return temperatureMaxTime;
    }

    public void setTemperatureMaxTime(int temperatureMaxTime) {
        this.temperatureMaxTime = temperatureMaxTime;
    }

    public float getApparentTemperatureMin() {
        return apparentTemperatureMin;
    }

    public void setApparentTemperatureMin(float apparentTemperatureMin) {
        this.apparentTemperatureMin = apparentTemperatureMin;
    }

    public int getApparentTemperatureMinTime() {
        return apparentTemperatureMinTime;
    }

    public void setApparentTemperatureMinTime(int apparentTemperatureMinTime) {
        this.apparentTemperatureMinTime = apparentTemperatureMinTime;
    }

    public float getApparentTemperatureMax() {
        return apparentTemperatureMax;
    }

    public void setApparentTemperatureMax(float apparentTemperatureMax) {
        this.apparentTemperatureMax = apparentTemperatureMax;
    }

    public int getApparentTemperatureMaxTime() {
        return apparentTemperatureMaxTime;
    }

    public void setApparentTemperatureMaxTime(int apparentTemperatureMaxTime) {
        this.apparentTemperatureMaxTime = apparentTemperatureMaxTime;
    }

    @Override
    public String toString() {
        return "Datum{" +
                "time=" + time +
                ", summary='" + summary + '\'' +
                ", icon='" + icon + '\'' +
                ", sunriseTime=" + sunriseTime +
                ", sunsetTime=" + sunsetTime +
                ", moonPhase=" + moonPhase +
                ", precipIntensity=" + precipIntensity +
                ", precipIntensityMax=" + precipIntensityMax +
                ", precipIntensityMaxTime=" + precipIntensityMaxTime +
                ", precipProbability=" + precipProbability +
                ", precipType='" + precipType + '\'' +
                ", temperatureHigh=" + temperatureHigh +
                ", temperatureHighTime=" + temperatureHighTime +
                ", temperatureLow=" + temperatureLow +
                ", temperatureLowTime=" + temperatureLowTime +
                ", apparentTemperatureHigh=" + apparentTemperatureHigh +
                ", apparentTemperatureHighTime=" + apparentTemperatureHighTime +
                ", apparentTemperatureLow=" + apparentTemperatureLow +
                ", apparentTemperatureLowTime=" + apparentTemperatureLowTime +
                ", dewPoint=" + dewPoint +
                ", humidity=" + humidity +
                ", pressure=" + pressure +
                ", windSpeed=" + windSpeed +
                ", windGust=" + windGust +
                ", windGustTime=" + windGustTime +
                ", windBearing=" + windBearing +
                ", cloudCover=" + cloudCover +
                ", uvIndex=" + uvIndex +
                ", uvIndexTime=" + uvIndexTime +
                ", ozone=" + ozone +
                ", temperatureMin=" + temperatureMin +
                ", temperatureMinTime=" + temperatureMinTime +
                ", temperatureMax=" + temperatureMax +
                ", temperatureMaxTime=" + temperatureMaxTime +
                ", apparentTemperatureMin=" + apparentTemperatureMin +
                ", apparentTemperatureMinTime=" + apparentTemperatureMinTime +
                ", apparentTemperatureMax=" + apparentTemperatureMax +
                ", apparentTemperatureMaxTime=" + apparentTemperatureMaxTime +
                '}';
    }
}
