package com.ext1se.weather.data.api;

import android.content.SharedPreferences;

import com.ext1se.weather.AppDelegate;
import com.ext1se.weather.BuildConfig;
import com.ext1se.weather.R;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class ApiKeyInterceptor implements Interceptor {

    private AppDelegate mAppDelegate;
    private SharedPreferences mSharedPreferences;

    public ApiKeyInterceptor(AppDelegate appDelegate, SharedPreferences sharedPreferences) {
        mAppDelegate = appDelegate;
        mSharedPreferences = sharedPreferences;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        String units = mSharedPreferences.getString(mAppDelegate.getResources().getString(R.string.key_units),
                mAppDelegate.getResources().getStringArray(R.array.units_format_value)[0]); //metric
        String lang = mAppDelegate.getResources().getConfiguration().locale.getLanguage();
        Request request = chain.request();
        HttpUrl httpUrl = request.url().newBuilder()
                .addQueryParameter("appid", BuildConfig.API_KEY_OPENWEATHER)
                .addQueryParameter("lang", lang)
                .addQueryParameter("units", units)
                .build();
        request = request.newBuilder().url(httpUrl).build();
        return chain.proceed(request);
    }
}
