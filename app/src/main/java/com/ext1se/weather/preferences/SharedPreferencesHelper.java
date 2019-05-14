package com.ext1se.weather.preferences;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

import java.lang.reflect.Type;

import com.google.gson.reflect.TypeToken;
import com.ext1se.weather.data.model.CurrentAndForecast;


public class SharedPreferencesHelper {
    private static final String SHARED_PREF_NAME = "SHARED_PREF_LOCATION";
    private static final String KEY_LOCATION = "KEY_LOCATION";
    private static final String KEY_FORECAST = "KEY_FORECAST";
    private static final Type TYPE_LOCATION = new TypeToken<CurrentLocation>() {
    }.getType();
    private static final Type TYPE_FORECAST = new TypeToken<CurrentAndForecast>() {
    }.getType();

    private SharedPreferences mSharedPreferences;
    private Gson mGson;

    public SharedPreferencesHelper(Context context) {
        mGson = new Gson();
        mSharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
    }

    public CurrentLocation getLocation() {
        return mGson.fromJson(mSharedPreferences.getString(KEY_LOCATION, ""), TYPE_LOCATION);
    }

    public void setLocation(CurrentLocation location) {
        mSharedPreferences.edit().putString(KEY_LOCATION, mGson.toJson(location, TYPE_LOCATION)).apply();
    }

    public CurrentAndForecast getForecast() {
        return mGson.fromJson(mSharedPreferences.getString(KEY_FORECAST, ""), TYPE_FORECAST);
    }

    public void setForecast(CurrentAndForecast forecast) {
        String s = mGson.toJson(forecast, TYPE_FORECAST);
        mSharedPreferences.edit().putString(KEY_FORECAST, s).apply();
    }
}
