package com.ext1se.weather.di.modules;

import android.support.annotation.IdRes;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.TypeFilter;
import com.google.android.libraries.places.api.net.FindCurrentPlaceRequest;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.ext1se.weather.BuildConfig;
import com.ext1se.weather.di.scopes.WeatherActivityScope;
import com.ext1se.weather.ui.weather.WeatherActivity;

import java.util.Arrays;
import java.util.List;

import dagger.Module;
import dagger.Provides;

@Module
public class PlaceModule {

    private WeatherActivity mActivity;
    private List<Place.Field> mPlaceFields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG, Place.Field.ADDRESS);
    private int mIdFragment;

    public PlaceModule(WeatherActivity weatherActivity, @IdRes int idFragment) {
        mIdFragment = idFragment;
        mActivity = weatherActivity;
        if (!Places.isInitialized()) {
            Places.initialize(mActivity.getApplicationContext(), BuildConfig.API_KEY_PLACES);
        }
    }

    @Provides
    @WeatherActivityScope
    AutocompleteSupportFragment provideAutocompleteSupportFragment() {
        AutocompleteSupportFragment autocompleteFragment = (AutocompleteSupportFragment)
                mActivity.getSupportFragmentManager().findFragmentById(mIdFragment);
        autocompleteFragment.setPlaceFields(mPlaceFields);
        autocompleteFragment.setTypeFilter(TypeFilter.CITIES);
        if (mActivity instanceof PlaceSelectionListener) {
            autocompleteFragment.setOnPlaceSelectedListener(mActivity);
        }
        return autocompleteFragment;
    }

    @Provides
    @WeatherActivityScope
    FindCurrentPlaceRequest provideFindCurrentPlaceRequest(){
        return FindCurrentPlaceRequest.newInstance(mPlaceFields);
    }

    @Provides
    @WeatherActivityScope
    PlacesClient providePlacesClient(){
        return Places.createClient(mActivity);
    }

    @Provides
    @WeatherActivityScope
    FusedLocationProviderClient provideFusedLocationProviderClient(){
        return LocationServices.getFusedLocationProviderClient(mActivity);
    }
}
