package com.politechnika.app.astroweather.fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentManager;
import android.view.MenuItem;
import android.widget.Toast;
import com.politechnika.app.astroweather.R;
import com.politechnika.app.astroweather.WeatherActivity;
import com.politechnika.app.astroweather.service.SharedPreference;

import java.util.List;

public class SettingsFragment extends PreferenceFragment implements Preference.OnPreferenceChangeListener, SharedPreferences.OnSharedPreferenceChangeListener {
    private SharedPreferences preferences;
    private EditTextPreference manualLocationPreference;
    private CheckBoxPreference favouritesInsertionPreference;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.app_preferences);

        preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());

        manualLocationPreference = (EditTextPreference) findPreference(getString(R.string.pref_manual_location));
        manualLocationPreference.setEnabled(true);

        favouritesInsertionPreference = (CheckBoxPreference) findPreference(getString(R.string.pref_insert_to_favourites));
        //favouritesInsertionPreference.setChecked(false);

        bindPreferenceSummaryToValue(manualLocationPreference);
        bindPreferenceSummaryToValue(findPreference(getString(R.string.pref_temperature_unit)));

        PreferenceManager.getDefaultSharedPreferences(getActivity()).registerOnSharedPreferenceChangeListener(this);

        onSharedPreferenceChanged(null, null);

        if(!preferences.getBoolean(getString(R.string.pref_needs_setup), false)) {
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean(getString(R.string.pref_needs_setup), false);
            editor.apply();
        }

        setHasOptionsMenu(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            startActivity(new Intent(getActivity(), WeatherActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void bindPreferenceSummaryToValue(Preference preference) {
        preference.setOnPreferenceChangeListener(this);
        onPreferenceChange(preference, preferences.getString(preference.getKey(), null));
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object value) {
        String stringValue = value.toString();

        if (preference instanceof ListPreference) {
            ListPreference listPreference = (ListPreference) preference;
            int index = listPreference.findIndexOfValue(stringValue);

            preference.setSummary(index >= 0 ? listPreference.getEntries()[index] : null);

        } else if (preference instanceof EditTextPreference) {
            preference.setSummary(stringValue);
        }

        return true;
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

    }
}
