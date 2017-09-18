package com.projects.crow.mameteo.database.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Venom on 09/09/2017.
 */

public class Hourly {

    @SerializedName("summary")
    @Expose
    private String summary = "";
    @SerializedName("icon")
    @Expose
    private String icon = "";
    @SerializedName("data")
    @Expose
    private List<Datum> data = new ArrayList<>();

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

    public List<Datum> getData() {
        return data;
    }

    public void setData(List<Datum> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "Hourly{" +
                "summary='" + summary + '\'' +
                ", icon='" + icon + '\'' +
                ", data=" + data +
                '}';
    }
}
