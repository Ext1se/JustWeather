package com.ext1se.weather.data.model;

import com.google.gson.annotations.SerializedName;
import com.ext1se.weather.data.model.details.City;

import java.util.List;

public class Forecast {

    @SerializedName("cod")
    private String mCodeResponse;
    @SerializedName("message")
    private double mMessage;
    @SerializedName("cnt")
    private int mCount;
    @SerializedName("city")
    private City mCity;
    @SerializedName("list")
    private List<Weather> mWeathers;

    public String getCodeResponse() {
        return mCodeResponse;
    }

    public void setCodeResponse(String codeResponse) {
        mCodeResponse = codeResponse;
    }

    public double getMessage() {
        return mMessage;
    }

    public void setMessage(double message) {
        mMessage = message;
    }

    public int getCount() {
        return mCount;
    }

    public void setCount(int count) {
        mCount = count;
    }

    public City getCity() {
        return mCity;
    }

    public void setCity(City city) {
        mCity = city;
    }

    public List<Weather> getWeathers() {
        return mWeathers;
    }

    public void setWeathers(List<Weather> weathers) {
        mWeathers = weathers;
    }
}
