package com.ext1se.weather.data.model.details;

import com.google.gson.annotations.SerializedName;

public class Location {

    @SerializedName("lon")
    private double mLongitude;
    @SerializedName("lat")
    private double mLatitude;

    public double getLongitude() {
        return mLongitude;
    }

    public void setLongitude(double longitude) {
        mLongitude = longitude;
    }

    public double getLatitude() {
        return mLatitude;
    }

    public void setLatitude(double latitude) {
        mLatitude = latitude;
    }
}
