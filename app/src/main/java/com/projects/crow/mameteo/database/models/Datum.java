package com.projects.crow.mameteo.database.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Venom on 09/09/2017.
 */

public class Datum {

    @SerializedName("time")
    @Expose
    private int time = 0;

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "Datum{" +
                "time=" + time +
                '}';
    }
}
