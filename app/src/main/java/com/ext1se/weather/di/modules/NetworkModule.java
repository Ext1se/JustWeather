package com.ext1se.weather.di.modules;

import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.ext1se.weather.AppDelegate;
import com.ext1se.weather.BuildConfig;
import com.ext1se.weather.data.api.ApiKeyInterceptor;
import com.ext1se.weather.data.api.WeatherApi;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class NetworkModule {

    @Provides
    @Singleton
    Gson provideGson() {
        return new Gson();
    }

    @Provides
    @Singleton
    OkHttpClient provideClient(AppDelegate appDelegate, @Named("Settings")SharedPreferences preferences) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.addInterceptor(new ApiKeyInterceptor(appDelegate, preferences));
        if (!BuildConfig.BUILD_TYPE.contains("release")) {
            builder.addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY));
        }
        return builder.build();
    }

    @Provides
    @Singleton
    Retrofit provideRetrofit(OkHttpClient client, Gson gson) {
        return new Retrofit.Builder()
                .baseUrl(BuildConfig.API_URL_OPENWEATHER)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }

    @Provides
    @Singleton
    WeatherApi provideApi(Retrofit retrofit) {
        return retrofit.create(WeatherApi.class);
    }
}
