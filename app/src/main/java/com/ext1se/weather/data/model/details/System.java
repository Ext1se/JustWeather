package com.ext1se.weather.data.model.details;

import com.google.gson.annotations.SerializedName;

public class System {

    /**
     * type : 1
     * id : 5091
     * message : 0.0103
     * country : GB
     * sunrise : 1485762037
     * sunset : 1485794875
     */

    @SerializedName("type")
    private int mType;
    @SerializedName("id")
    private int mId;
    @SerializedName("message")
    private double mMessage;
    @SerializedName("country")
    private String mCountry;
    @SerializedName("sunrise")
    private int mSunrise;
    @SerializedName("sunset")
    private int mSunset;

    public int getType() {
        return mType;
    }

    public void setType(int type) {
        mType = type;
    }

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    public double getMessage() {
        return mMessage;
    }

    public void setMessage(double message) {
        mMessage = message;
    }

    public String getCountry() {
        return mCountry;
    }

    public void setCountry(String country) {
        mCountry = country;
    }

    public int getSunrise() {
        return mSunrise;
    }

    public void setSunrise(int sunrise) {
        mSunrise = sunrise;
    }

    public int getSunset() {
        return mSunset;
    }

    public void setSunset(int sunset) {
        mSunset = sunset;
    }
}
