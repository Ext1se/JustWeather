package com.ext1se.weather.ui;

import android.content.res.Resources;
import android.graphics.Color;
import android.text.format.DateFormat;

import com.ext1se.opengl.weather.WorldWeather;
import com.ext1se.weather.R;
import com.ext1se.weather.data.model.Weather;

import java.util.Date;

public class WeatherDesign {
    private String mWeatherFontIcon;
    private int mWeatherIcon;
    private int mWeatherImage;
    private int mWeatherState;
    private int mColorTop;
    private int mColorBottom;
    private int mColorPrimary;
    private int mColorPrimaryDark;

    public WeatherDesign(){
    }

    public WeatherDesign(Weather weather) {
        createWeatherState(weather);
        createColorsByTemp(weather.getCondition().getTemp());
    }

    public WeatherDesign(Weather weather, Resources resources) {
        createWeatherState(weather);
        createColorsByDay(weather.getDescription().getIcon(), resources);
    }

    public void createWeatherState(Weather weather) {
        mWeatherState = WorldWeather.WEATHER_CLEAR;
        mWeatherFontIcon = "\uf00d";
        mWeatherIcon = R.drawable.ic_wi_clear_d;
        mWeatherImage = R.drawable.wi_clear_d;
        int actualId = weather.getDescription().getId();
        boolean day = weather.getDescription().getIcon().contains("d");
        int id = actualId / 100;
        if (actualId == 800) {
            if (day) {
                mWeatherState = WorldWeather.WEATHER_SUN;
                mWeatherFontIcon = "\uf00d";
                mWeatherIcon = R.drawable.ic_wi_clear_d;
                mWeatherImage = R.drawable.wi_clear_d;
            } else {
                mWeatherState = WorldWeather.WEATHER_NIGHT;
                mWeatherFontIcon = "\uf02e";
                mWeatherIcon = R.drawable.ic_wi_clear_n;
                mWeatherImage = R.drawable.wi_clear_n;
            }
        } else {
            switch (id) {
                case 2:
                    mWeatherState = WorldWeather.WEATHER_RAIN;
                    if (day) {
                        mWeatherFontIcon = "\uf010";
                        mWeatherIcon = R.drawable.ic_wi_storm_d;
                        mWeatherImage = R.drawable.wi_storm_d;
                    } else {
                        mWeatherFontIcon = "\uf02d";
                        mWeatherIcon = R.drawable.ic_wi_storm_n;
                        mWeatherImage = R.drawable.wi_storm_n;
                    }
                    break;
                case 3:
                    mWeatherState = WorldWeather.WEATHER_RAIN;
                    if (day) {
                        mWeatherFontIcon = "\uf00b";
                        mWeatherIcon = R.drawable.ic_wi_sprinkle_d;
                        mWeatherImage = R.drawable.wi_sprinkle_d;
                    } else {
                        mWeatherFontIcon = "\uf02b";
                        mWeatherIcon = R.drawable.ic_wi_sprinkle_n;
                        mWeatherImage = R.drawable.wi_sprinkle_n;
                    }
                    break;
                case 5:
                    mWeatherState = WorldWeather.WEATHER_RAIN;
                    if (day) {
                        mWeatherFontIcon = "\uf008";
                        mWeatherIcon = R.drawable.ic_wi_rain_d;
                        mWeatherImage = R.drawable.wi_rain_d;
                    } else {
                        mWeatherFontIcon = "\uf028";
                        mWeatherIcon = R.drawable.ic_wi_rain_n;
                        mWeatherImage = R.drawable.wi_rain_n;
                    }
                    break;
                case 6:
                    mWeatherState = WorldWeather.WEATHER_SNOW;
                    if (day) {
                        mWeatherFontIcon = "\uf065";
                        mWeatherIcon = R.drawable.ic_wi_snow_d;
                        mWeatherImage = R.drawable.wi_snow_d;
                    } else {
                        mWeatherFontIcon = "\uf02a";
                        mWeatherIcon = R.drawable.ic_wi_snow_n;
                        mWeatherImage = R.drawable.wi_snow_n;
                    }
                    break;
                case 7: {
                    mWeatherState = WorldWeather.WEATHER_CLEAR;
                    mWeatherFontIcon = "\uf014";
                    mWeatherIcon = R.drawable.ic_wi_fog;
                    mWeatherImage = R.drawable.wi_fog;
                }
                break;
                case 8: {
                    mWeatherState = WorldWeather.WEATHER_CLOUD;
                    mWeatherFontIcon = "\uf013";
                    mWeatherIcon = R.drawable.ic_wi_cloudy;
                    mWeatherImage = R.drawable.wi_cloudy;
                }
                break;
            }
        }
    }

    private void createColorsByTemp(float degree) {
        int max = 280;
        int min = 0;
        int center = 180;
        float step = 20.0f;
        float stepCold = 2.0f;
        float stepHot = 4.5f;
        float saturationPrimary = 0.5f;
        float lightnessPrimary = 0.5f;
        float saturationPrimaryDark = 0.4f;
        float lightnessPrimaryDark = 0.4f;
        float huePrimary, hueSecondary;
        if (degree >= 0) {
            huePrimary = center - stepHot * degree;
            hueSecondary = huePrimary - step;
        } else {
            huePrimary = center - stepCold * degree;
            hueSecondary = huePrimary - step;
        }
        if (huePrimary < min) {
            huePrimary = min;
        }
        if (huePrimary > max) {
            huePrimary = max;
        }
        float[] hsv1 = new float[3];
        float[] hsv2 = new float[3];
        hsv1[0] = huePrimary;
        hsv1[1] = saturationPrimary;
        hsv1[2] = lightnessPrimary;
        hsv2[0] = hueSecondary;
        hsv2[1] = saturationPrimaryDark;
        hsv2[2] = lightnessPrimaryDark;
        mColorPrimary = Color.HSVToColor(hsv1);
        mColorPrimaryDark = Color.HSVToColor(hsv2);
    }

    private void createColorsByTime(long date, Resources resources) {
        String h = DateFormat.format("HH", new Date(date)).toString();
        int pos = Integer.parseInt(h);
        if (pos >= 0 && pos < 6){
            mColorTop = resources.getColor(R.color.color_night_top);
            mColorBottom = resources.getColor(R.color.color_night_bottom);
            mColorPrimary = resources.getColor(R.color.color_night_primary);
            mColorPrimaryDark = resources.getColor(R.color.color_night_primary_dark);
        }
        if (pos >= 6 && pos < 12){
            mColorTop = resources.getColor(R.color.color_day_top);
            mColorBottom = resources.getColor(R.color.color_day_bottom);
            mColorPrimary = resources.getColor(R.color.color_morning_primary);
            mColorPrimaryDark = resources.getColor(R.color.color_morning_primary_dark);
        }
        if (pos >= 12 && pos < 18){
            mColorTop = resources.getColor(R.color.color_day_top);
            mColorBottom = resources.getColor(R.color.color_day_bottom);
            mColorPrimary = resources.getColor(R.color.color_day_primary);
            mColorPrimaryDark = resources.getColor(R.color.color_day_primary_dark);
        }
        if (pos >= 18 && pos < 24){
            mColorTop = resources.getColor(R.color.color_night_top);
            mColorBottom = resources.getColor(R.color.color_night_bottom);
            mColorPrimary = resources.getColor(R.color.color_evening_primary);
            mColorPrimaryDark = resources.getColor(R.color.color_evening_primary_dark);
        }
    }

    private void createColorsByDay(String icon, Resources resources) {
        boolean day = icon.contains("d");
        if (day){
            mColorTop = resources.getColor(R.color.color_day_top);
            mColorBottom = resources.getColor(R.color.color_day_bottom);
            mColorPrimary = resources.getColor(R.color.color_day_primary);
            mColorPrimaryDark = resources.getColor(R.color.color_day_primary_dark);
        }
        else{
            mColorTop = resources.getColor(R.color.color_night_top);
            mColorBottom = resources.getColor(R.color.color_night_bottom);
            mColorPrimary = resources.getColor(R.color.color_night_primary);
            mColorPrimaryDark = resources.getColor(R.color.color_night_primary_dark);
        }
    }

    public int getWeatherIcon() {
        return mWeatherIcon;
    }

    public String getWeatherFontIcon() {
        return mWeatherFontIcon;
    }

    public int getColorPrimary() {
        return mColorPrimary;
    }

    public int getColorPrimaryDark() {
        return mColorPrimaryDark;
    }

    public int getWeatherState() {
        return mWeatherState;
    }

    public int getWeatherImage() {
        return mWeatherImage;
    }

    public int getColorTop() {
        return mColorTop;
    }

    public int getColorBottom() {
        return mColorBottom;
    }
}
