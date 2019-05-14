package com.ext1se.weather.ui.settings;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.annotation.Nullable;

import com.ext1se.weather.AppDelegate;
import com.ext1se.weather.R;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;

public class SettingsFragment extends PreferenceFragment implements Preference.OnPreferenceChangeListener {

    @Inject
    @Named("Settings")
    SharedPreferences mSharedPreferences;
    private String mOldValueUnit;
    private Map<String, Object> mOldValues = new HashMap<>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppDelegate.getAppComponent().inject(this);
        addPreferencesFromResource(R.xml.preferences);
        setHasOptionsMenu(true);
        initOldValues();
        bindPreferenceSummaryToValue(findPreference(getString(R.string.key_units)));
        bindPreferenceSummaryToValue(findPreference(getString(R.string.key_update_time)));
        bindPreferenceSummaryToValue(findPreference(getString(R.string.key_notification_custom_enable)));
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        if (preference instanceof ListPreference) {
            String value = newValue.toString();
            ListPreference listPreference = (ListPreference) preference;
            int index = listPreference.findIndexOfValue(value);
            if (index >= 0) {
                listPreference.setSummary(listPreference.getEntries()[index]);
            }
        }

        if (preference.getKey().equalsIgnoreCase(getResources().getString(R.string.key_notification_custom_enable))) {
            boolean value = (boolean) newValue;
            if (value) {
                preference.setSummary(getResources().getString(R.string.setting_notification_custom_enabled));
            } else {
                preference.setSummary(getResources().getString(R.string.setting_notification_custom_disabled));
            }
        }
        return true;
    }

    private void bindPreferenceSummaryToValue(Preference preference) {
        preference.setOnPreferenceChangeListener(this);
        if (preference instanceof ListPreference) {
            onPreferenceChange(preference, mSharedPreferences.getString(preference.getKey(), ""));
        }
        if (preference instanceof CheckBoxPreference) {
            onPreferenceChange(preference, mSharedPreferences.getBoolean(preference.getKey(), true));
        }
    }

    private String getUnitValue() {
        return mSharedPreferences.getString(getString(R.string.key_units), getResources().getStringArray(R.array.units_format_value)[0]);
    }

    private void initOldValues() {
        mOldValueUnit = getUnitValue();
        mOldValues.put(getString(R.string.key_update_enable),
                mSharedPreferences.getBoolean(getString(R.string.key_update_enable), true));
        mOldValues.put(getString(R.string.key_update_time),
                mSharedPreferences.getString(getString(R.string.key_update_time), getResources().getStringArray(R.array.intervals_value)[0]));
        mOldValues.put(getString(R.string.key_notification_enable),
                mSharedPreferences.getBoolean(getString(R.string.key_notification_enable), true));
        mOldValues.put(getString(R.string.key_notification_custom_enable),
                mSharedPreferences.getBoolean(getString(R.string.key_notification_custom_enable), true));
    }

    public boolean isUpdateWeather() {
        return !mOldValueUnit.equals(getUnitValue());
    }

    public boolean isUpdateSettings() {
        Iterator iterator = mOldValues.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry setting = (Map.Entry) iterator.next();
            String key = (String) setting.getKey();
            Object value = setting.getValue();
            if (value instanceof Boolean) {
                if (mSharedPreferences.getBoolean(key, false) != ((Boolean) value)) {
                    return true;
                }
            }
            if (value instanceof String) {
                if (!mSharedPreferences.getString(key, " ").equalsIgnoreCase((String) value)) {
                    return true;
                }
            }
        }
        return false;
    }
}
