package com.ext1se.weather.data.model.details;

import com.google.gson.annotations.SerializedName;

public class Wind {

    /**
     * speed : 4.1
     * deg : 80
     */

    @SerializedName("speed")
    private float mSpeed;
    @SerializedName("deg")
    private float mDeg;

    public float getSpeed() {
        return mSpeed;
    }

    public void setSpeed(float speed) {
        mSpeed = speed;
    }

    public float getDeg() {
        return mDeg;
    }

    public void setDeg(float deg) {
        mDeg = deg;
    }
}
