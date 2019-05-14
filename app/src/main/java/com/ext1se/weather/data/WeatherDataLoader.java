package com.ext1se.weather.data;

import com.ext1se.weather.data.api.WeatherApi;
import com.ext1se.weather.data.model.CurrentAndForecast;
import com.ext1se.weather.data.model.Forecast;
import com.ext1se.weather.data.model.Weather;
import com.ext1se.weather.preferences.CurrentLocation;
import com.ext1se.weather.preferences.SharedPreferencesHelper;

import javax.inject.Inject;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class WeatherDataLoader {
    @Inject
    WeatherApi mApi;
    @Inject
    SharedPreferencesHelper mPreferencesHelper;

    private CompositeDisposable mCompositeDisposable;
    private Loader mLoader;

    public WeatherDataLoader(Loader loader) {
        //TODO: inject dependencies
        mLoader = loader;
        mCompositeDisposable = new CompositeDisposable();
    }

    private void loadForecast(CurrentLocation location) {
        Single<Weather> weather = mApi.getWeatherByCoordinates(location.getLat(), location.getLon());
        Single<Forecast> forecast = mApi.getForecastByCoordinates(location.getLat(), location.getLon());
        mCompositeDisposable.add(Single.zip(weather, forecast, CurrentAndForecast::new)
                .doOnSuccess(response -> mPreferencesHelper.setForecast(response))
                .onErrorReturn(throwable -> mPreferencesHelper.getForecast())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable -> mLoader.doOnSubscribe())
                .doFinally(() -> mLoader.doFinally())
                .subscribe(
                        response -> {
                            if (response != null) {
                                mLoader.doOnSuccessResponse(response);
                            }
                        },
                        throwable -> {
                            mLoader.doOnErrorResponse();
                        }
                ));
    }

    public void disposeAll() {
        mCompositeDisposable.clear();
    }

    public interface Loader{
        void doOnSubscribe();
        void doFinally();
        void doOnSuccessResponse(CurrentAndForecast response);
        void doOnErrorResponse();
    }
}
