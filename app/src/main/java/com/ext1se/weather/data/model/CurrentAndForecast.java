package com.ext1se.weather.data.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class CurrentAndForecast implements Serializable {

    @SerializedName("current")
    private Weather mCurrentWeather;
    @SerializedName("forecast")
    private Forecast mForecast;
    @SerializedName("city")
    private String mCityName;

    public CurrentAndForecast(Weather currentWeather, Forecast forecast) {
        mCurrentWeather = currentWeather;
        mForecast = forecast;
    }

    public Weather getCurrentWeather() {
        return mCurrentWeather;
    }

    public Forecast getForecast() {
        return mForecast;
    }

    public List<Weather> getWeathers() {
        List<Weather> weathers = new ArrayList<>();
        weathers.add(mCurrentWeather);
        weathers.addAll(mForecast.getWeathers());
        return weathers;
    }

    public String getCityName() {
        return mCityName;
    }

    public void setCityName(String cityName) {
        mCityName = cityName;
    }
}
