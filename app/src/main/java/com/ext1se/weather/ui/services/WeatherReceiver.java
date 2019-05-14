package com.ext1se.weather.ui.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;

public class WeatherReceiver extends BroadcastReceiver {

    public static final String RECEIVER_UPDATE = "com.ponomarev.weather.UPDATE";
    public static final int REQUEST_CODE = 1000;

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (intent.getAction() == null) {
            return;
        }
        Log.d("WeatherService", "onReceive: " + action);
        if (action.equalsIgnoreCase(RECEIVER_UPDATE) || action.equalsIgnoreCase(Intent.ACTION_BOOT_COMPLETED)) {
            IBinder iBinder = peekService(context, new Intent(context, WeatherService.class));
            /*
            Если отлично от нуля, значит есть пара Сервис-Клиент.
            Извлекаем сервис и отправляем сообщение с действием.
            ---------------------------------------
            Если равно нулю, то приложение либо свернуто, либо не запущено,
            поэтому запускаем не привязанную службу и следим за ее циклом.
             */
            if (iBinder != null) {
                Messenger service = new Messenger(iBinder);
                Message message = Message.obtain(null, WeatherService.MSG_UPDATE);
                try {
                    service.send(message);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            } else {
                Intent i = new Intent(context, WeatherService.class);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    context.startForegroundService(i);
                } else {
                    context.startService(i);
                }
            }
        }
    }
}
