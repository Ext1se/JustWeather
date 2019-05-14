package com.ext1se.weather.data.model.details;

import com.google.gson.annotations.SerializedName;

public class Condition {

    /**
     * temp : 280.32
     * pressure : 1012
     * humidity : 81
     * temp_min : 279.15
     * temp_max : 281.15
     */

    @SerializedName("temp")
    private float mTemp;
    @SerializedName("pressure")
    private float mPressure;
    @SerializedName("humidity")
    private float mHumidity;
    @SerializedName("temp_min")
    private float mTempMin;
    @SerializedName("temp_max")
    private float mTempMax;

    public float getTemp() {
        return mTemp;
    }

    public void setTemp(float temp) {
        mTemp = temp;
    }

    public float getPressure() {
        return mPressure;
    }

    public void setPressure(float pressure) {
        mPressure = pressure;
    }

    public float getHumidity() {
        return mHumidity;
    }

    public void setHumidity(float humidity) {
        mHumidity = humidity;
    }

    public float getTempMin() {
        return mTempMin;
    }

    public void setTempMin(float tempMin) {
        mTempMin = tempMin;
    }

    public float getTempMax() {
        return mTempMax;
    }

    public void setTempMax(float tempMax) {
        mTempMax = tempMax;
    }

}
