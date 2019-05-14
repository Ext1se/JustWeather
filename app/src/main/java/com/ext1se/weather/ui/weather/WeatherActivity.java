package com.ext1se.weather.ui.weather;

import android.Manifest;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresPermission;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.FindCurrentPlaceRequest;
import com.google.android.libraries.places.api.net.FindCurrentPlaceResponse;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.ext1se.weather.AppDelegate;
import com.ext1se.weather.BuildConfig;
import com.ext1se.weather.R;
import com.ext1se.weather.common.RefreshOwner;
import com.ext1se.weather.common.Refreshable;
import com.ext1se.weather.data.model.CurrentAndForecast;
import com.ext1se.weather.di.modules.PlaceModule;
import com.ext1se.weather.preferences.CurrentLocation;
import com.ext1se.weather.preferences.SharedPreferencesHelper;
import com.ext1se.weather.ui.services.WeatherService;
import com.ext1se.weather.ui.settings.SettingsActivity;
import com.ext1se.weather.views.CustomSwipeRefreshLayout;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.ACCESS_WIFI_STATE;

public class WeatherActivity extends AppCompatActivity implements PlaceSelectionListener,
        SwipeRefreshLayout.OnRefreshListener,
        RefreshOwner {

    public static final int MSG_UPDATE = 0;
    private static final int CODE_PERMISSION = 100;

    @Inject
    AutocompleteSupportFragment mAutocompleteSupportFragment;
    @Inject
    SharedPreferencesHelper mPreferencesHelper;
    @Inject
    PlacesClient mPlacesClient;
    @Inject
    FusedLocationProviderClient mFusedLocationProviderClient;
    @Inject
    FindCurrentPlaceRequest mCurrentPlaceRequest;

    @BindView(R.id.refresher)
    CustomSwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(R.id.toolbar_background)
    FrameLayout mToolbarBackground;
    @BindView(R.id.ll_info)
    LinearLayout mWelcomeLayout;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.tv_city_name)
    TextView mCityName;

    private boolean mUseGeocoderForPlace = true;
    private boolean mUseLastLocation = false;

    /*
    Метод setNotification может вызваться до привязки службы. Поэтому используем дополнительную переменную,
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
        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(), BuildConfig.API_KEY_PLACES);
        }
        mClient = new Messenger(new IncomingHandler());
        mSwipeRefreshLayout.setOnRefreshListener(this);
        if (savedInstanceState == null) {
            setFragment(getFragment());
        }
        setToolbar();
        setCityName();
    }

    private void setToolbar() {
        setSupportActionBar(mToolbar);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_map);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //mCityName.setSelected(true);
    }

    private void setCityName() {
        CurrentLocation location = mPreferencesHelper.getLocation();
        if (location != null) {
            setCityName(location.getAddress());
        } else {
            setCityName(getString(R.string.emerald_city));
            mWelcomeLayout.setVisibility(View.VISIBLE);
            getCurrentLocation();
        }
    }

    private void setCityName(String address) {
        mCityName.setText(address);
    }

    public void injectDependencies() {
        AppDelegate.getAppComponent()
                .plusWeatherActivity(new PlaceModule(this, R.id.autocomplete_fragment))
                .inject(this);
    }

    private void setFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fr_container, fragment);
        transaction.commit();
    }

    public Fragment getFragment() {
        return WeatherFragment.newInstance();
    }

    @Override
    public void onPlaceSelected(@NonNull Place place) {
        setCityName(place.getAddress());
        CurrentLocation location = new CurrentLocation(place.getId(), place.getName(), place.getAddress(), place.getLatLng().latitude, place.getLatLng().longitude);
        updateWeatherData(location);
    }

    @Override
    public void onError(@NonNull Status status) {
        showError();
    }

    private void updateWeatherData(CurrentLocation location) {
        mWelcomeLayout.setVisibility(View.GONE);
        mPreferencesHelper.setLocation(location);
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fr_container);
        if (fragment instanceof Refreshable) {
            ((Refreshable) fragment).onRefreshData(location);
        } else {
            setRefreshState(false);
        }
    }

    private void getCurrentLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_WIFI_STATE) != PackageManager.PERMISSION_DENIED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_WIFI_STATE, Manifest.permission.ACCESS_FINE_LOCATION}, CODE_PERMISSION);
        } else {
            if (mUseLastLocation) {
                getLastLocation();
            } else {
                findCurrentLocationByPlace();
            }
        }
    }

    @RequiresPermission(allOf = {ACCESS_FINE_LOCATION, ACCESS_WIFI_STATE})
    private void getLastLocation() {
        mFusedLocationProviderClient.getLastLocation()
                .addOnSuccessListener(this, location -> {
                    if (location != null) {
                        double lat = location.getLatitude();
                        double lon = location.getLongitude();
                        setAddressByGeocoder(null, lat, lon);
                    } else {
                        findCurrentLocationByPlace();
                    }
                })
                .addOnFailureListener(e -> {
                    findCurrentLocationByPlace();
                });
    }

    @RequiresPermission(allOf = {ACCESS_FINE_LOCATION, ACCESS_WIFI_STATE})
    private void findCurrentLocationByPlace() {
        setRefreshState(true);
        Task<FindCurrentPlaceResponse> currentPlaceTask = mPlacesClient.findCurrentPlace(mCurrentPlaceRequest);
        currentPlaceTask.addOnSuccessListener(response -> {
            Place place = response.getPlaceLikelihoods().get(0).getPlace();
            if (!mUseGeocoderForPlace) {
                CurrentLocation location = new CurrentLocation(place.getId(), place.getName(), place.getAddress(), place.getLatLng().latitude, place.getLatLng().longitude);
                setCityName(location.getAddress());
                updateWeatherData(location);
            } else {
                setAddressByGeocoder(place, place.getLatLng().latitude, place.getLatLng().longitude);
            }
        });
        currentPlaceTask.addOnFailureListener(
                exception -> {
                    exception.printStackTrace();
                    showDialogError();
                });
        currentPlaceTask.addOnCompleteListener(task -> setRefreshState(false));
    }

    private void setAddressByGeocoder(Place place, double lat, double lon) {
        setRefreshState(true);
        Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
        try {
            List<Address> listAddresses = geocoder.getFromLocation(lat, lon, 1);
            if (listAddresses != null && listAddresses.size() > 0) {
                Address address = listAddresses.get(0);
                String city = address.getLocality();
                String shortAddress = address.getLocality() + ", " + address.getAdminArea() + ", " + address.getCountryName();
                setCityName(shortAddress);
                CurrentLocation currentLocation;
                if (place != null) {
                    currentLocation = new CurrentLocation(place.getId(), place.getName(), shortAddress, lat, lon);
                } else {
                    currentLocation = new CurrentLocation(null, city, shortAddress, lat, lon);
                }
                updateWeatherData(currentLocation);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            setRefreshState(false);
        }
    }

    @Override
    @RequiresPermission(allOf = {ACCESS_FINE_LOCATION, ACCESS_WIFI_STATE})
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CODE_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                if (mUseLastLocation) {
                    getLastLocation();
                } else {
                    findCurrentLocationByPlace();
                }
            }
        }
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

    private void showError() {
        Toast.makeText(WeatherActivity.this, R.string.error_operation, Toast.LENGTH_SHORT).show();
    }

    private void showDialogError() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.dialog_error)
                .setMessage(R.string.dialog_advice)
                .setPositiveButton(android.R.string.ok, null);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case (R.id.action_location):
                getCurrentLocation();
                break;
            case (R.id.action_settings):
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (intent.getBooleanExtra(SettingsActivity.KEY_UPDATE_WEATHER, false)) {
            setFragment(getFragment());
            return;
        }
        if (intent.getBooleanExtra(SettingsActivity.KEY_UPDATE_SETTINGS, false)) {
            CurrentAndForecast forecast = mPreferencesHelper.getForecast();
            if (forecast == null){
                return;
            }
            sendMessage(WeatherService.MSG_SHOW, forecast);
            return;
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
