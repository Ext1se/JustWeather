package com.ext1se.weather.ui.services;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.os.SystemClock;
import android.util.Log;

import com.ext1se.weather.AppDelegate;
import com.ext1se.weather.R;
import com.ext1se.weather.common.BaseActivity;
import com.ext1se.weather.data.api.WeatherApi;
import com.ext1se.weather.data.model.CurrentAndForecast;
import com.ext1se.weather.data.model.Forecast;
import com.ext1se.weather.data.model.Weather;
import com.ext1se.weather.di.modules.ServiceModule;
import com.ext1se.weather.preferences.CurrentLocation;
import com.ext1se.weather.preferences.SharedPreferencesHelper;

import javax.inject.Inject;
import javax.inject.Named;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class WeatherService extends Service {
    private static final String TAG = WeatherService.class.getSimpleName();

    public static final int MSG_CONNECT = 0;
    public static final int MSG_SHOW = 1;
    public static final int MSG_UPDATE = 2;
    public static final int MSG_ERROR = 3;

    private static final int ID_NOTIFICATION = 100;
    private static final int ID_NOTIFICATION_UPDATE = 101;
    private Messenger mService, mClient;
    private CompositeDisposable mCompositeDisposable;

    @Inject
    WeatherApi mApi;
    @Inject
    @Named("PendingIntentUpdate")
    PendingIntent mPendingIntentUpdate;
    @Inject
    NotificationHelper mNotificationHelper;
    @Inject
    WeatherReceiver mWeatherReceiver;
    @Inject
    SharedPreferencesHelper mSharedPreferencesHelper;
    @Inject
    @Named("Settings")
    SharedPreferences mPreferences;

    public class IncomingHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case (MSG_CONNECT):
                    mClient = msg.replyTo;
                    break;
                case (MSG_SHOW):
                    mClient = msg.replyTo;
                    updateAlarm();
                    CurrentAndForecast forecast = (CurrentAndForecast) msg.obj;
                    showNotification(forecast);
                    break;
                case (MSG_UPDATE):
                    updateAlarm();
                    loadForecast(0);
                    break;
                case (MSG_ERROR):
                    break;

                default:
                    super.handleMessage(msg);
            }
        }
    }

    private void showNotification(CurrentAndForecast forecast) {
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if (!mPreferences.getBoolean(getResources().getString(R.string.key_notification_enable), true)) {
            manager.cancel(ID_NOTIFICATION);
            return;
        }
        Notification notification;
        if (mPreferences.getBoolean(getResources().getString(R.string.key_notification_custom_enable), true)) {
            notification = mNotificationHelper.createNotificationCustom(forecast);
        } else {
            notification = mNotificationHelper.createNotificationSimple(forecast.getCurrentWeather());
        }
        manager.notify(ID_NOTIFICATION, notification);
    }

    private void updateNotificationAndClient(CurrentAndForecast forecast) {
        showNotification(forecast);
        if (mClient != null) {
            Message message = Message.obtain(null, BaseActivity.MSG_UPDATE);
            message.obj = forecast;
            try {
                mClient.send(message);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate: ");
        AppDelegate
                .getAppComponent()
                .plusWeatherService(new ServiceModule(this))
                .inject(this);
        mService = new Messenger(new IncomingHandler());
        mCompositeDisposable = new CompositeDisposable();
        IntentFilter filter = new IntentFilter(WeatherReceiver.RECEIVER_UPDATE);
        registerReceiver(mWeatherReceiver, filter);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mService.getBinder();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mWeatherReceiver);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        updateAlarm();
        loadForecast(startId);
        return START_NOT_STICKY;
    }

    private void loadForecast(int startId) {
        CurrentLocation location = mSharedPreferencesHelper.getLocation();
        if (location == null) {
            return;
        }
        Single<Weather> weather = mApi.getWeatherByCoordinates(location.getLat(), location.getLon());
        Single<Forecast> forecast = mApi.getForecastByCoordinates(location.getLat(), location.getLon());
        mCompositeDisposable.add(Single.zip(weather, forecast, CurrentAndForecast::new)
                .doOnSuccess(response -> {
                    response.setCityName(location.getAddress());
                    mSharedPreferencesHelper.setForecast(response);
                })
                //.onErrorReturn(throwable -> mSharedPreferencesHelper.getForecast())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable -> startForeground(ID_NOTIFICATION_UPDATE, mNotificationHelper.createNotificationProgress()))
                .doFinally(() -> {
                    stopForeground(true);
                    stopSelf(startId);
                })
                .subscribe(
                        response -> {
                            if (response != null) {
                                updateNotificationAndClient(response);
                            }
                        },
                        throwable -> {
                            //TODO: handle error
                        }
                ));
    }

    private void updateAlarm() {
        AlarmManager alarmManager = (AlarmManager) getApplicationContext().getSystemService(ALARM_SERVICE);
        if (alarmManager != null) {
            if (!mPreferences.getBoolean(getResources().getString(R.string.key_update_enable), true)) {
                alarmManager.cancel(mPendingIntentUpdate);
            } else {
                String h = mPreferences.getString(getResources().getString(R.string.key_update_time), "3");
                long interval = Integer.parseInt(h) * 60 * 60 * 1000;
                alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() + interval, mPendingIntentUpdate);
            }
        }
    }
}
