package com.ext1se.weather.di.components;

import com.ext1se.weather.di.modules.PlaceModule;
import com.ext1se.weather.di.scopes.WeatherActivityScope;
import com.ext1se.weather.ui.weather.WeatherActivity;

import dagger.Subcomponent;

@WeatherActivityScope
@Subcomponent(modules = {PlaceModule.class})
public interface WeatherActivityComponent {
    void inject(WeatherActivity activity);
}
