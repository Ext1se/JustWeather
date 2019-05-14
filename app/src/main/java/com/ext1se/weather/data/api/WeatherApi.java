package com.ext1se.weather.data.api;

import com.ext1se.weather.data.model.Forecast;
import com.ext1se.weather.data.model.Weather;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface WeatherApi {

    @GET("weather")
    Single<Weather> getWeatherByCity(@Query("q") String city);

    @GET("weather")
    Single<Weather> getWeatherByCoordinates(@Query("lat") double lat, @Query("lon") double lon);

    @GET("forecast")
    Single<Forecast> getForecastByCity(@Query("q") String city);

    @GET("forecast")
    Single<Forecast> getForecastByCoordinates(@Query("lat") double lat, @Query("lon") double lon);

}
