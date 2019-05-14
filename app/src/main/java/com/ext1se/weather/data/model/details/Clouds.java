package com.ext1se.weather.data.model.details;

import com.google.gson.annotations.SerializedName;

public class Clouds {

    /**
     * all : 90
     */

    @SerializedName("all")
    private double mAll;

    public double getAll() {
        return mAll;
    }

    public void setAll(double all) {
        mAll = all;
    }
}
