package com.ext1se.weather.data.model.details;

import com.google.gson.annotations.SerializedName;

public class City {

    /**
     * id : 1489425
     * name : Tomsk
     * coord : {"lat":56.4887,"lon":84.9523}
     * country : RU
     * population : 485519
     */

    @SerializedName("id")
    private int mId;
    @SerializedName("name")
    private String mName;
    @SerializedName("coord")
    private Location Location;
    @SerializedName("country")
    private String mCountry;
    @SerializedName("population")
    private int mPopulation;

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public Location getLocation() {
        return Location;
    }

    public void setLocation(Location location) {
        Location = location;
    }

    public String getCountry() {
        return mCountry;
    }

    public void setCountry(String country) {
        mCountry = country;
    }

    public int getPopulation() {
        return mPopulation;
    }

    public void setPopulation(int population) {
        mPopulation = population;
    }
}
