package com.ext1se.weather.common;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;
import android.widget.FrameLayout;

import com.ext1se.weather.R;
import com.ext1se.weather.data.model.CurrentAndForecast;
import com.ext1se.weather.ui.services.WeatherService;
import com.ext1se.weather.ui.weather.WeatherFragment;
import com.ext1se.weather.views.CustomSwipeRefreshLayout;

import butterknife.BindView;
import butterknife.ButterKnife;

public abstract class BaseActivity extends AppCompatActivity implements
        SwipeRefreshLayout.OnRefreshListener,
        RefreshOwner {

    public static final int MSG_UPDATE = 0;

    @BindView(R.id.refresher)
    CustomSwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(R.id.toolbar_background)
    FrameLayout mToolbarBackground;

    /*
    Метод setNotification может вызваться до привязки службы. Поэтому используем доп переменную,
    которая поможет отправить сообщение при привязке.
    TODO: костыльное решение -> первая привязка службы
     */
    private Message mFirstMessage = null;
    private Messenger mService, mClient;
    private boolean mBound;
    private final ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mService = new Messenger(service);
            mBound = true;
            Message message;
            if (mFirstMessage != null) {
                message = mFirstMessage;
                mFirstMessage = null;
            } else {
                message = Message.obtain(null, WeatherService.MSG_CONNECT);
                message.replyTo = mClient;
            }

            try {
                mService.send(message);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mService = null;
            mBound = false;
        }
    };

    private class IncomingHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == MSG_UPDATE) {
                Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fr_container);
                if (fragment instanceof WeatherFragment) {
                    ((WeatherFragment) fragment).updateForecast((CurrentAndForecast)msg.obj);
                }
                return;
            }
            super.handleMessage(msg);
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_weather);
        ButterKnife.bind(this);
        injectDependencies();
        mClient = new Messenger(new IncomingHandler());
        mSwipeRefreshLayout.setOnRefreshListener(this);
        if (savedInstanceState == null) {
            setFragment(getFragment());
        }
    }

    public abstract Fragment getFragment();

    public abstract void injectDependencies();

    protected void setFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fr_container, fragment);
        transaction.commit();
    }

    @Override
    public void onRefresh() {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fr_container);
        if (fragment instanceof Refreshable) {
            ((Refreshable) fragment).onRefreshData();
        } else {
            setRefreshState(false);
        }
    }

    @Override
    public void setRefreshState(boolean refreshing) {
        mSwipeRefreshLayout.post(() -> mSwipeRefreshLayout.setRefreshing(refreshing));
    }

    @Override
    public void setRefreshToolbar(int colorPrimary, int colorPrimaryDark) {
        mToolbarBackground.setBackgroundColor(colorPrimary);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(colorPrimaryDark);
        }
    }

    @Override
    public void setNotification(CurrentAndForecast forecast) {
        sendMessage(WeatherService.MSG_SHOW, forecast);
    }

    public void sendMessage(int what, CurrentAndForecast forecast){
        Message message = Message.obtain(null, what);
        message.obj = forecast;
        message.replyTo = mClient;
        if (mService == null) {
            mFirstMessage = message;
            return;
        }
        try {
            mService.send(message);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = new Intent(this, WeatherService.class);
        bindService(intent, mConnection, BIND_AUTO_CREATE);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mBound) {
            unbindService(mConnection);
            mBound = false;
        }
    }
}
