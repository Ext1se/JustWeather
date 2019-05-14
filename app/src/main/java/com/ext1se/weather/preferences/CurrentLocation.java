package com.ext1se.weather.preferences;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class CurrentLocation implements Serializable {

    @SerializedName("id")
    private String mId;
    @SerializedName("name")
    private String mName;
    @SerializedName("address")
    private String mAddress;
    @SerializedName("lat")
    private double mLat;
    @SerializedName("lon")
    private double mLon;

    public CurrentLocation() {
    }

    public CurrentLocation(String id, String name, String address, double lat, double lon) {
        mId = id;
        mName = name;
        mAddress = address;
        mLat = lat;
        mLon = lon;
    }

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getAddress() {
        return mAddress;
    }

    public void setAddress(String address) {
        mAddress = address;
    }

    public double getLat() {
        return mLat;
    }

    public void setLat(double lat) {
        mLat = lat;
    }

    public double getLon() {
        return mLon;
    }

    public void setLon(double lon) {
        mLon = lon;
    }
}
