package com.ext1se.weather;

import android.preference.PreferenceManager;
import android.support.multidex.MultiDexApplication;

import com.ext1se.weather.di.components.AppComponent;
import com.ext1se.weather.di.components.DaggerAppComponent;
import com.ext1se.weather.di.modules.AppModule;
import com.ext1se.weather.di.modules.NetworkModule;

public class AppDelegate extends MultiDexApplication {

    private static AppComponent sAppComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
        sAppComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .networkModule(new NetworkModule())
                .build();

    }

    public static AppComponent getAppComponent() {
        return sAppComponent;
    }
}
