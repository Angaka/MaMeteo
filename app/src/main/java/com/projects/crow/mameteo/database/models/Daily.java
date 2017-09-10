package com.projects.crow.mameteo.database.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Venom on 09/09/2017.
 */

public class Daily {
    @SerializedName("summary")
    @Expose
    private String summary = "";
    @SerializedName("icon")
    @Expose
    private String icon = "";
    @SerializedName("data")
    @Expose
    private List<Datum__> data = new ArrayList<>();

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

    public List<Datum__> getData() {
        return data;
    }

    public void setData(List<Datum__> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "Daily{" +
                "summary='" + summary + '\'' +
                ", icon='" + icon + '\'' +
                ", data=" + data +
                '}';
    }
}
