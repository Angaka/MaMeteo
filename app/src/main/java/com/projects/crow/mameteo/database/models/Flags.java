package com.projects.crow.mameteo.database.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Venom on 09/09/2017.
 */

public class Flags {
    @SerializedName("sources")
    @Expose
    private List<String> sources = new ArrayList<>();
    @SerializedName("isd-stations")
    @Expose
    private List<String> isdStations = new ArrayList<>();
    @SerializedName("units")
    @Expose
    private String units = "";

    public List<String> getSources() {
        return sources;
    }

    public void setSources(List<String> sources) {
        this.sources = sources;
    }

    public List<String> getIsdStations() {
        return isdStations;
    }

    public void setIsdStations(List<String> isdStations) {
        this.isdStations = isdStations;
    }

    public String getUnits() {
        return units;
    }

    public void setUnits(String units) {
        this.units = units;
    }

    @Override
    public String toString() {
        return "Flags{" +
                "sources=" + sources +
                ", isdStations=" + isdStations +
                ", units='" + units + '\'' +
                '}';
    }
}
