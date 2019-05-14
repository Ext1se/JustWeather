package com.ext1se.weather.ui.settings;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.ext1se.weather.ui.weather.WeatherActivity;

public class SettingsActivity extends AppCompatActivity {

    public static final String KEY_UPDATE_WEATHER = "key_update_weather";
    public static final String KEY_UPDATE_SETTINGS = "key_update_settings";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getFragmentManager()
                .beginTransaction()
                .replace(android.R.id.content, new SettingsFragment())
                .commit();
    }

    @Override
    public void onBackPressed() {
        String key = getUpdateSetting();
        if (key == null) {
            super.onBackPressed();
        } else {
            Intent intent = new Intent(this, WeatherActivity.class);
            intent.putExtra(key, true);
            startActivity(intent);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private String getUpdateSetting() {
        Fragment fragment = getFragmentManager().findFragmentById(android.R.id.content);
        if (fragment instanceof SettingsFragment) {
            if (((SettingsFragment) fragment).isUpdateWeather()) {
                return KEY_UPDATE_WEATHER;
            }
            if (((SettingsFragment) fragment).isUpdateSettings()) {
                return KEY_UPDATE_SETTINGS;
            }
        }
        return null;
    }
}
