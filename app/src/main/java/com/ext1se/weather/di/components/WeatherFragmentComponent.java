package com.ext1se.weather.di.components;

import com.ext1se.weather.di.modules.WeatherFragmentModule;
import com.ext1se.weather.di.scopes.WeatherFragmentScope;
import com.ext1se.weather.ui.weather.WeatherFragment;

import dagger.Subcomponent;

@WeatherFragmentScope
@Subcomponent(modules = {WeatherFragmentModule.class})
public interface WeatherFragmentComponent {
    void inject (WeatherFragment fragment);
}
