package com.ext1se.weather.common;

import com.ext1se.weather.preferences.CurrentLocation;

public interface Refreshable {
    void onRefreshData();
    void onRefreshData(CurrentLocation location);
}
