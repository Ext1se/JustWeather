package com.ext1se.weather.data.model.details;

import com.google.gson.annotations.SerializedName;

public class WeatherDescription {

    /**
     * id : 300
     * main : Drizzle
     * description : light intensity drizzle
     * icon : 09d
     */

    @SerializedName("id")
    private int mId;
    @SerializedName("main")
    private String mMain;
    @SerializedName("description")
    private String mDescription;
    @SerializedName("icon")
    private String mIcon;

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    public String getMain() {
        return mMain;
    }

    public void setMain(String main) {
        mMain = main;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        mDescription = description;
    }

    public String getIcon() {
        return mIcon;
    }

    public void setIcon(String icon) {
        mIcon = icon;
    }

}
