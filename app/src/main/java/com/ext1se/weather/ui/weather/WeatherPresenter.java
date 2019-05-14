package com.ext1se.weather.ui.weather;

import com.arellomobile.mvp.InjectViewState;
import com.ext1se.weather.AppDelegate;
import com.ext1se.weather.common.BasePresenter;
import com.ext1se.weather.data.api.WeatherApi;
import com.ext1se.weather.data.model.CurrentAndForecast;
import com.ext1se.weather.data.model.Forecast;
import com.ext1se.weather.data.model.Weather;
import com.ext1se.weather.preferences.CurrentLocation;
import com.ext1se.weather.preferences.SharedPreferencesHelper;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

@InjectViewState
public class WeatherPresenter extends BasePresenter<WeatherView> {

    @Inject
    WeatherApi mApi;
    @Inject
    SharedPreferencesHelper mPreferencesHelper;

    private CurrentLocation mLocation;
    private List<Weather> mWeathers = new ArrayList<>();

    public WeatherPresenter() {
        AppDelegate.getAppComponent().inject(this);
        mLocation = mPreferencesHelper.getLocation();
        if (mLocation != null) {
            /*
            Вызов этого метода из конструктора Тормозит загрузку ...
            Можно создать отдельный метод и вызываеть его во фрагменте.
            Торможение, вероятно, связано с Moxy, так создание Презентера происходит до создания вью.
            -----
            Данная проблема наблюдается в дебаг версии, в релизе - все хорошо.
             */
            loadForecastByLocation(mLocation);
        }
    }

    public void loadForecastByLocation() {
        if (mLocation != null) {
            Single<Weather> weather = mApi.getWeatherByCoordinates(mLocation.getLat(), mLocation.getLon());
            Single<Forecast> forecast = mApi.getForecastByCoordinates(mLocation.getLat(), mLocation.getLon());
            loadForecast(weather, forecast);
        } else {
            getViewState().hideRefresh();
            getViewState().showError();
        }
    }

    public void loadForecastByLocation(CurrentLocation location) {
        mLocation = location;
        loadForecastByLocation();
    }

    private void loadForecast(Single<Weather> weather, Single<Forecast> forecast) {
        mCompositeDisposable.add(Single.zip(weather, forecast, CurrentAndForecast::new)
                .doOnSuccess(response -> {
                    response.setCityName(mLocation.getAddress());
                    mPreferencesHelper.setForecast(response);
                })
                .onErrorReturn(throwable -> mPreferencesHelper.getForecast())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable -> getViewState().showRefresh())
                .doFinally(() -> getViewState().hideRefresh())
                .subscribe(
                        response -> {
                            if (response != null) {
                                mWeathers = response.getWeathers();

                                getViewState().updateWeather(response.getCurrentWeather());
                                getViewState().updateChart(response.getWeathers());

                                getViewState().updateNotification(response);
                            }
                        },
                        throwable -> {
                            //TODO: handle error
                        }
                ));
    }

    public void showSelectedWeather(int position) {
        if (mWeathers == null) {
            return;
        }
        getViewState().updateWeather(mWeathers.get(position));
    }

    /*
    Метод для загрузки текущей погоды ... Понадобится для карты. Потом нужно перенести в отдельный презентер.
     */
    private void getCurrentWeather() {
        mCompositeDisposable.add(mApi.getWeatherByCity("s")
                .doOnSuccess(response -> {
                })
                .onErrorReturn(throwable -> {
                    return null;
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable -> getViewState().showRefresh())
                .subscribe(
                        response -> {

                        },
                        throwable -> {
                        }
                ));
    }
}
