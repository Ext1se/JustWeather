package com.ext1se.weather.data.model;

import com.google.gson.annotations.SerializedName;
import com.ext1se.weather.data.model.details.Clouds;
import com.ext1se.weather.data.model.details.Condition;
import com.ext1se.weather.data.model.details.WeatherDescription;
import com.ext1se.weather.data.model.details.System;
import com.ext1se.weather.data.model.details.Wind;

import java.util.List;

public class Weather {

    @SerializedName("main")
    private Condition mCondition;
    @SerializedName("wind")
    private Wind mWind;
    @SerializedName("clouds")
    private Clouds mClouds;
    @SerializedName("dt")
    private int mDate;
    @SerializedName("dt_txt")
    private String mDateString; //"2019-01-28 12:00:00"
    @SerializedName("sys")
    private System mSystem;
    @SerializedName("weather")
    private List<WeatherDescription> mWeatherDescriptions;

    public Condition getCondition() {
        return mCondition;
    }

    public void setCondition(Condition condition) {
        mCondition = condition;
    }

    public Wind getWind() {
        return mWind;
    }

    public void setWind(Wind wind) {
        mWind = wind;
    }

    public Clouds getClouds() {
        return mClouds;
    }

    public void setClouds(Clouds clouds) {
        mClouds = clouds;
    }

    public int getDate() {
        return mDate;
    }

    public long getDateMillis() {
        return (long) mDate * 1000;
    }

    public void setDate(int date) {
        mDate = date;
    }

    public System getSystem() {
        return mSystem;
    }

    public void setSystem(System system) {
        mSystem = system;
    }

    public List<WeatherDescription> getWeatherDescriptions() {
        return mWeatherDescriptions;
    }

    public void setWeatherDescriptions(List<WeatherDescription> weatherDescriptions) {
        mWeatherDescriptions = weatherDescriptions;
    }

    public String getDateString() {
        return mDateString;
    }

    public void setDateString(String dateString) {
        mDateString = dateString;
    }

    public WeatherDescription getDescription() {
        return mWeatherDescriptions.get(0);
    }
}
