package com.ext1se.weather.di.modules;


import android.support.v4.app.Fragment;

import com.ext1se.weather.common.RefreshOwner;
import com.ext1se.weather.di.scopes.WeatherFragmentScope;

import dagger.Module;
import dagger.Provides;

@Module
public class WeatherFragmentModule {

    private Fragment mFragment;

    public WeatherFragmentModule(Fragment fragment) {
        mFragment = fragment;
    }

    @Provides
    @WeatherFragmentScope
    RefreshOwner provideOwner() {
        if (mFragment.getContext() instanceof RefreshOwner) {
            return (RefreshOwner) mFragment.getContext();
        }
        throw new ClassCastException("Context doesn't implement RefreshOwner");
    }
}
