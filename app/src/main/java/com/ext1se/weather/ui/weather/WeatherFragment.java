package com.ext1se.weather.ui.weather;

import android.graphics.Color;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;
import com.ext1se.opengl.weather.WeatherGL;
import com.ext1se.weather.AppDelegate;
import com.ext1se.weather.R;
import com.ext1se.weather.common.PresenterFragment;
import com.ext1se.weather.common.RefreshOwner;
import com.ext1se.weather.common.Refreshable;
import com.ext1se.weather.data.model.CurrentAndForecast;
import com.ext1se.weather.data.model.Weather;
import com.ext1se.weather.data.model.details.Condition;
import com.ext1se.weather.di.modules.WeatherFragmentModule;
import com.ext1se.weather.preferences.CurrentLocation;
import com.ext1se.weather.ui.WeatherDesign;
import com.ext1se.weather.utils.StringUtils;
import com.ext1se.weather.views.ChartWeatherView;
import com.ext1se.weather.views.MeasureTextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import lecho.lib.hellocharts.listener.LineChartOnValueSelectListener;
import lecho.lib.hellocharts.model.PointValue;

public class WeatherFragment extends PresenterFragment implements WeatherView, Refreshable, LineChartOnValueSelectListener {

    @BindView(R.id.gl_weather)
    GLSurfaceView mGLSurfaceView;
    @BindView(R.id.fl_weather_info)
    FrameLayout mWeatherInfoView;
    @BindView(R.id.tv_day)
    TextView mDay;
    @BindView(R.id.tv_date)
    TextView mDate;
    @BindView(R.id.tv_icon)
    TextView mIcon;
    @BindView(R.id.iv_icon)
    ImageView mImage;
    @BindView(R.id.tv_description)
    TextView mDescription;
    @BindView(R.id.tv_temp)
    MeasureTextView mTemp;
    @BindView(R.id.tv_humidity)
    MeasureTextView mHumidity;
    @BindView(R.id.tv_pressure)
    MeasureTextView mPressure;
    @BindView(R.id.tv_wind)
    MeasureTextView mWind;
    @BindView(R.id.chart)
    ChartWeatherView mChartLine;

    @Inject
    RefreshOwner mRefreshOwner;
    @InjectPresenter
    WeatherPresenter mPresenter;

    private WeatherGL glWeather;


    @ProvidePresenter
    WeatherPresenter providePresenter() {
        return new WeatherPresenter();
    }

    public WeatherFragment() {
    }

    public static WeatherFragment newInstance() {
        WeatherFragment fragment = new WeatherFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fr_weather, container, false);
        ButterKnife.bind(this, view);
        mChartLine.setZoomEnabled(false);
        mChartLine.setOnValueTouchListener(this);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        glWeather = new WeatherGL(getActivity(), mGLSurfaceView);
    }

    @Override
    protected WeatherPresenter getPresenter() {
        return mPresenter;
    }

    @Override
    protected void injectDependencies() {
        AppDelegate.getAppComponent()
                .plusWeatherFragment(new WeatherFragmentModule(this))
                .inject(this);
    }

    @Override
    public void onRefreshData() {
        mPresenter.loadForecastByLocation();
    }

    @Override
    public void onRefreshData(CurrentLocation location) {
        mPresenter.loadForecastByLocation(location);
    }

    public void updateForecast(CurrentAndForecast forecast){
        updateWeather(forecast.getCurrentWeather());
        updateChart(forecast.getWeathers());
    }

    @Override
    public void updateWeather(Weather weather) {
        mWeatherInfoView.setVisibility(View.VISIBLE);
        Date dateBase = new Date(weather.getDateMillis());
        String day = StringUtils.capitalize(new SimpleDateFormat("EEEE", Locale.getDefault()).format(dateBase));
        String date = new SimpleDateFormat("d MMMM, HH:mm", Locale.getDefault()).format(dateBase);
        String description = weather.getDescription().getDescription();
        description = StringUtils.capitalize(description);
        Condition condition = weather.getCondition();
        mDay.setText(day);
        mDate.setText(date);
        mDescription.setText(description);
        mTemp.setMeasureText(condition.getTemp(), MeasureTextView.MEASURE_TEMPERATURE);
        mHumidity.setMeasureText(condition.getHumidity(), MeasureTextView.MEASURE_HUMIDITY);
        mPressure.setMeasureText(condition.getPressure(), MeasureTextView.MEASURE_PRESSURE);
        mWind.setMeasureText(weather.getWind().getSpeed(), MeasureTextView.MEASURE_WIND);

        WeatherDesign design = new WeatherDesign(weather, getResources());
        mIcon.setText(design.getWeatherFontIcon());
        mImage.setImageResource(design.getWeatherIcon());
        glWeather.updateScreen(design.getWeatherState(), design.getColorTop(), design.getColorBottom());
        mChartLine.setAreaColor(Color.TRANSPARENT);
        mRefreshOwner.setRefreshToolbar(design.getColorTop(), design.getColorTop());
    }

    @Override
    public void updateChart(List<Weather> weathers) {
        mChartLine.generateData(weathers);
    }

    @Override
    public void updateNotification(CurrentAndForecast forecast) {
        mRefreshOwner.setNotification(forecast);
    }

    @Override
    public void showRefresh() {
        mRefreshOwner.setRefreshState(true);
    }

    @Override
    public void hideRefresh() {
        mRefreshOwner.setRefreshState(false);
    }

    @Override
    public void showError() {
        //TODO: add view for Error
        Toast.makeText(getActivity(), R.string.error_operation, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onValueSelected(int lineIndex, int pointIndex, PointValue value) {
        mPresenter.showSelectedWeather(pointIndex - 1);
    }

    @Override
    public void onValueDeselected() {

    }

    @Override
    public void onResume() {
        super.onResume();
        mGLSurfaceView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mGLSurfaceView.onPause();
    }


    @Override
    public void onStop() {
        super.onStop();
        mGLSurfaceView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mGLSurfaceView.onPause();
    }
}
