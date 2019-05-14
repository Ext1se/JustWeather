package com.ext1se.weather.ui.weather;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresPermission;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
import com.ext1se.weather.BuildConfig;
import com.ext1se.weather.R;
import com.ext1se.weather.common.BaseActivity;
import com.ext1se.weather.common.Refreshable;
import com.ext1se.weather.data.model.CurrentAndForecast;
import com.ext1se.weather.preferences.CurrentLocation;
import com.ext1se.weather.preferences.SharedPreferencesHelper;
import com.ext1se.weather.ui.services.WeatherService;
import com.ext1se.weather.ui.settings.SettingsActivity;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.ACCESS_WIFI_STATE;

public class WeatherBaseActivity extends BaseActivity implements PlaceSelectionListener {

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

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.tv_city_name)
    TextView mCityName;

    private boolean mUseGeocoderForPlace = true;
    private boolean mUseLastLocation = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(), BuildConfig.API_KEY_PLACES);
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
            //
            getCurrentLocation();
        }
    }

    private void setCityName(String address) {
        mCityName.setText(address);
    }

    public void injectDependencies() {
        //AppDelegate.getAppComponent()
         //       .plusWeatherActivity(new PlaceModule(this, R.id.autocomplete_fragment))
         //       .inject(this);
    }

    @Override
    public Fragment getFragment() {
        return WeatherFragment.newInstance();
    }

    @Override
    public void onPlaceSelected(@NonNull Place place) {
        setCityName(place.getAddress());
        CurrentLocation location = new CurrentLocation(place.getId(), place.getName(), place.getAddress(), place.getLatLng().latitude, place.getLatLng().latitude);
        updateWeatherData(location);
    }

    @Override
    public void onError(@NonNull Status status) {
        showError();
    }

    private void updateWeatherData(CurrentLocation location) {
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
                    showError();
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

    public void showError() {
        Toast.makeText(WeatherBaseActivity.this, R.string.error_operation, Toast.LENGTH_SHORT).show();
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
}
