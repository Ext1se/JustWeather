package com.ext1se.weather.ui.services;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.text.format.DateFormat;
import android.widget.RemoteViews;

import com.ext1se.weather.R;
import com.ext1se.weather.data.model.CurrentAndForecast;
import com.ext1se.weather.data.model.Weather;
import com.ext1se.weather.ui.WeatherDesign;
import com.ext1se.weather.ui.weather.WeatherActivity;
import com.ext1se.weather.utils.StringUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class NotificationHelper {

    private static final String ID_CHANNEL = "channel_weather";
    private static final String NAME_CHANNEL = "notification_weather";

    private String mUnitTemp, mUnitWind;
    private Context mContext;
    private SharedPreferences mPreferences;
    private PendingIntent mPendingIntentUpdate;

    public NotificationHelper(Context context, SharedPreferences preferences, PendingIntent pendingIntentUpdate) {
        mContext = context;
        mPreferences = preferences;
        mPendingIntentUpdate = pendingIntentUpdate;
    }

    private NotificationCompat.Builder createNotificationBuilder(NotificationManager manager) {
        NotificationCompat.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (manager.getNotificationChannel(ID_CHANNEL) == null) {
                NotificationChannel channel = new NotificationChannel(ID_CHANNEL, NAME_CHANNEL, NotificationManager.IMPORTANCE_DEFAULT);
                manager.createNotificationChannel(channel);
            }
            builder = new NotificationCompat.Builder(mContext, ID_CHANNEL);
        } else {
            builder = new NotificationCompat.Builder(mContext);
        }

        Intent intent = new Intent(mContext, WeatherActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(mContext, 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        builder
                .setSmallIcon(R.drawable.ic_wi_clear_d)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setOnlyAlertOnce(true);
        return builder;
    }

    public Notification createNotificationSimple(Weather weather){
        NotificationManager manager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder = createNotificationBuilder(manager);

        createUnits();
        WeatherDesign design = new WeatherDesign();
        design.createWeatherState(weather);
        String temp = String.format(Locale.getDefault(), "%s: %.1f %s", mContext.getResources().getString(R.string.temperature),
                weather.getCondition().getTemp(), mUnitTemp);
        String wind = String.format(Locale.getDefault(), "%s: %.1f %s", mContext.getResources().getString(R.string.wind),
                weather.getWind().getSpeed(), mUnitWind);
        String pressure = String.format(Locale.getDefault(), "%s: %.1f %s", mContext.getResources().getString(R.string.pressure),
                weather.getCondition().getPressure(), mContext.getResources().getString(R.string.pressure_pascal));
        String humidity = String.format(Locale.getDefault(), "%s: %.1f %s", mContext.getResources().getString(R.string.humidity),
                weather.getCondition().getHumidity(), mContext.getResources().getString(R.string.percent));
        String[] params = new String[]{temp, wind, humidity, pressure};
        NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();
        for(String param : params){
            inboxStyle.addLine(param);
        }
        builder
                .setSmallIcon(design.getWeatherIcon())
                .setColor(mContext.getResources().getColor(R.color.colorBlackLight))
                .setLargeIcon(BitmapFactory.decodeResource(mContext.getResources(), design.getWeatherImage()))
                .setContentTitle(StringUtils.capitalize(weather.getDescription().getDescription()))
                .setContentText(temp)
                .setStyle(inboxStyle);
        return builder.build();
    }

    public Notification createNotificationCustom(CurrentAndForecast forecast) {
        NotificationManager manager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder = createNotificationBuilder(manager);

        createUnits();
        WeatherDesign design = new WeatherDesign();
        design.createWeatherState(forecast.getCurrentWeather());
        builder
                .setSmallIcon(design.getWeatherIcon())
                .setCustomBigContentView(createCustomBigContentView(forecast, design))
                .setCustomContentView(createCustomSmallContentView(forecast.getCurrentWeather(), design));

        return builder.build();
    }

    private RemoteViews createCustomBigContentView(CurrentAndForecast forecast, WeatherDesign design) {
        RemoteViews remoteViewsBig = new RemoteViews(mContext.getApplicationContext().getPackageName(), R.layout.notification_big);
        remoteViewsBig.setTextViewText(R.id.tv_city_name, forecast.getCityName());
        String currentTime = new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date(System.currentTimeMillis()));
        remoteViewsBig.setTextViewText(R.id.tv_time, currentTime);

        remoteViewsBig.removeAllViews(R.id.ll_content);
        for (int i = 0; i < 4; i++) {
            Weather weather = forecast.getWeathers().get(i);
            RemoteViews remote = new RemoteViews(mContext.getApplicationContext().getPackageName(), R.layout.notification_item);
            String time = (i == 0) ? mContext.getResources().getString(R.string.now) :
                    DateFormat.format("HH:mm", new Date(weather.getDateMillis())).toString();
            remote.setTextViewText(R.id.tv_time, time);
            String temp = String.format(Locale.getDefault(), "%.1f %s", weather.getCondition().getTemp(), mUnitTemp);
            remote.setTextViewText(R.id.tv_temp, temp);
            String wind = String.format(Locale.getDefault(), "%.1f %s", weather.getWind().getSpeed(), mUnitWind);
            remote.setTextViewText(R.id.tv_wind, wind);
            design.createWeatherState(weather);
            remote.setImageViewResource(R.id.iv_icon, design.getWeatherIcon());
            remoteViewsBig.addView(R.id.ll_content, remote);
        }

        RemoteViews remoteButton = new RemoteViews(mContext.getApplicationContext().getPackageName(), R.layout.notification_item_refresh);
        remoteButton.setOnClickPendingIntent(R.id.b_update, mPendingIntentUpdate);
        remoteViewsBig.addView(R.id.ll_content, remoteButton);

        return remoteViewsBig;
    }

    private RemoteViews createCustomSmallContentView(Weather weather, WeatherDesign design){
        RemoteViews remoteViewsSmall = new RemoteViews(mContext.getApplicationContext().getPackageName(), R.layout.notification_small);
        String currentTime = new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date(System.currentTimeMillis()));
        remoteViewsSmall.setTextViewText(R.id.tv_time, currentTime);
        design.createWeatherState(weather);
        remoteViewsSmall.setImageViewResource(R.id.iv_icon, design.getWeatherIcon());
        String temp = String.format(Locale.getDefault(), "%.1f %s", weather.getCondition().getTemp(), mUnitTemp);
        remoteViewsSmall.setTextViewText(R.id.tv_temp, temp);
        String wind = String.format(Locale.getDefault(), "%s: %.1f %s", mContext.getResources().getString(R.string.wind),
                weather.getWind().getSpeed(), mUnitWind);
        remoteViewsSmall.setTextViewText(R.id.tv_wind, wind);
        remoteViewsSmall.setTextViewText(R.id.tv_description, StringUtils.capitalize(weather.getDescription().getDescription()));
        remoteViewsSmall.setOnClickPendingIntent(R.id.b_update, mPendingIntentUpdate);
        return remoteViewsSmall;
    }

    private void createUnits(){
        String units = mPreferences.getString(mContext.getResources().getString(R.string.key_units),
                mContext.getResources().getStringArray(R.array.units_format_value)[0]);
        if (units.equals(mContext.getResources().getStringArray(R.array.units_format_value)[0])) {
            mUnitTemp = mContext.getResources().getString(R.string.celsius);
            mUnitWind = mContext.getResources().getString(R.string.meter_per_second);
        } else {
            mUnitTemp = mContext.getResources().getString(R.string.fahrenheit);
            mUnitWind = mContext.getResources().getString(R.string.mile_per_hour);
        }
    }

    public Notification createNotificationProgress(){
        NotificationManager manager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder = createNotificationBuilder(manager);
        builder
                .setContentTitle(mContext.getResources().getString(R.string.notification_update_title))
                .setProgress(100, 0, true);

        return builder.build();
    }

}
