package com.ext1se.weather.common;

import com.ext1se.weather.data.model.CurrentAndForecast;

public interface RefreshOwner {
    void setRefreshState(boolean refreshing);
    void setRefreshToolbar(int colorPrimary, int colorPrimaryDark);
    void setNotification(CurrentAndForecast forecast);
}