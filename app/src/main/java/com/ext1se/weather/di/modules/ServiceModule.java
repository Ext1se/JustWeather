package com.ext1se.weather.di.modules;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;

import com.ext1se.weather.di.scopes.ServiceScope;
import com.ext1se.weather.ui.services.NotificationHelper;
import com.ext1se.weather.ui.services.WeatherReceiver;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;

@Module
public class ServiceModule {

    private Service mService;

    public ServiceModule(Service service) {
        mService = service;
    }

    @Provides
    @ServiceScope
    WeatherReceiver provideWeatherReceiver(){
        return new WeatherReceiver();
    }

    @Provides
    @ServiceScope
    NotificationHelper provideWeatherNotification(@Named("Settings")SharedPreferences preferences,
                                                  @Named("PendingIntentUpdate") PendingIntent pendingIntent){
        return new NotificationHelper(mService, preferences, pendingIntent);
    }

    @Provides
    @Named("PendingIntentUpdate")
    PendingIntent providePendingIntentUpdate(){
        Intent intent = new Intent(mService.getApplicationContext(), WeatherReceiver.class);
        intent.setAction(WeatherReceiver.RECEIVER_UPDATE);
        return PendingIntent.getBroadcast(mService.getApplicationContext(), WeatherReceiver.REQUEST_CODE, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }
}
