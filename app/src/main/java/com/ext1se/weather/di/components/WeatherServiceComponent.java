package com.ext1se.weather.di.components;

import com.ext1se.weather.di.modules.ServiceModule;
import com.ext1se.weather.di.scopes.ServiceScope;
import com.ext1se.weather.ui.services.WeatherService;

import dagger.Subcomponent;

@ServiceScope
@Subcomponent(modules = {ServiceModule.class})
public interface WeatherServiceComponent {
    void inject(WeatherService service);
}
