package com.ext1se.weather.di.modules;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.ext1se.weather.AppDelegate;
import com.ext1se.weather.preferences.SharedPreferencesHelper;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class AppModule {

    private final AppDelegate mApp;

    public AppModule(AppDelegate app) {
        mApp = app;
    }

    @Provides
    @Singleton
    AppDelegate provideApp() {
        return mApp;
    }

    @Provides
    @Singleton
    SharedPreferencesHelper provideSharedPreferencesHelper(){
        return new SharedPreferencesHelper(mApp);
    }

    @Provides
    @Named("Settings")
    SharedPreferences provideSharedPreferences(){
        return PreferenceManager.getDefaultSharedPreferences(mApp);
    }

}
