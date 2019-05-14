package com.ext1se.weather.ui.weather;

import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.SkipStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;
import com.ext1se.weather.common.BaseView;
import com.ext1se.weather.data.model.CurrentAndForecast;
import com.ext1se.weather.data.model.Weather;

import java.util.List;

@StateStrategyType(AddToEndSingleStrategy.class)
public interface WeatherView extends BaseView {

    void updateWeather(Weather weather);

    void updateChart(List<Weather> weathers);

    @StateStrategyType(SkipStrategy.class)
    void updateNotification(CurrentAndForecast forecast);
}
