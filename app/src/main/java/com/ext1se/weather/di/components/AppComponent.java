package com.ext1se.weather.di.components;

import com.ext1se.weather.di.modules.AppModule;
import com.ext1se.weather.di.modules.NetworkModule;
import com.ext1se.weather.di.modules.PlaceModule;
import com.ext1se.weather.di.modules.ServiceModule;
import com.ext1se.weather.di.modules.WeatherFragmentModule;
import com.ext1se.weather.ui.settings.SettingsFragment;
import com.ext1se.weather.ui.weather.WeatherPresenter;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {AppModule.class, NetworkModule.class})
public interface AppComponent {
    void inject(WeatherPresenter presenter);
    void inject(SettingsFragment fragment);
    WeatherFragmentComponent plusWeatherFragment(WeatherFragmentModule module);
    WeatherActivityComponent plusWeatherActivity(PlaceModule module);
    WeatherServiceComponent plusWeatherService(ServiceModule module);
}
