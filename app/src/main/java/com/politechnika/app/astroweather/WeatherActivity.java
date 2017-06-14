package com.politechnika.app.astroweather;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.preference.PreferenceManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.politechnika.app.astroweather.connection.Connectivity;
import com.politechnika.app.astroweather.fragments.FragmentMoon;
import com.politechnika.app.astroweather.fragments.FragmentSun;
import com.politechnika.app.astroweather.model.Channel;
import com.politechnika.app.astroweather.model.Condition;
import com.politechnika.app.astroweather.fragments.ActualConditionFragment;
import com.politechnika.app.astroweather.fragments.AdditionalConditionFragment;
import com.politechnika.app.astroweather.fragments.WeatherConditionFragment;
import com.politechnika.app.astroweather.listener.WeatherServiceListener;
import com.politechnika.app.astroweather.service.SharedPreference;
import com.politechnika.app.astroweather.service.WeatherCacheService;
import com.politechnika.app.astroweather.service.YahooWeatherService;

import java.util.List;


public class WeatherActivity extends AppCompatActivity implements WeatherServiceListener {
    private YahooWeatherService weatherService;
    private WeatherCacheService cacheService;
    private ProgressDialog loadingDialog;
    private SharedPreferences preferences = null;

    private Connectivity connectivity;

    private boolean orientationChanged;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        //set preferences
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        weatherService = new YahooWeatherService(this);
        weatherService.setTemperatureUnit(preferences.getString(getString(R.string.pref_temperature_unit), null));
        cacheService = new WeatherCacheService(this);
        connectivity = new Connectivity();

        orientationChanged = false;

        if (preferences.getBoolean(getString(R.string.pref_needs_setup), true)) {
            startSettingsActivity();
        }

        SharedPreference sharedPreference = new SharedPreference();
        List<String> listOfFavouriteCities = sharedPreference.getFavorites(getApplicationContext());

        if (connectivity.isConnected(this.getApplicationContext()) && listOfFavouriteCities != null && listOfFavouriteCities.size() > 0){
            for (String city : listOfFavouriteCities){
                weatherService.getFavouriteLocation(city, cacheService);
            }
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            orientationChanged = true;
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            orientationChanged = true;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        Log.i("onStart", "connection: " + connectivity.isConnected(this.getApplicationContext()));

        loadingDialog = new ProgressDialog(this);
        loadingDialog.setMessage(getString(R.string.loading));
        loadingDialog.setCancelable(false);
        loadingDialog.show();

        String location = null;
        location = preferences.getString(getString(R.string.pref_manual_location), null);
        weatherService.setTemperatureUnit(preferences.getString(getString(R.string.pref_temperature_unit), null));

        if (location != null && connectivity.isConnected(this.getApplicationContext()) && !orientationChanged) {
            Log.i("onStart", "connection: " + connectivity.isConnected(this.getApplicationContext()) + ", refreshing weather");

            weatherService.refreshWeather(location);
        } else if (location != null && !connectivity.isConnected(this.getApplicationContext()) && !orientationChanged) {
            loadingDialog.hide();

            //check if location exist in cache
            Toast.makeText(this, "No internet connection, loading older data.", Toast.LENGTH_SHORT).show();
            cacheService.load(this, location);
        }

    }


    @Override
    public void serviceSuccess(Channel channel) {
        loadingDialog.hide();
        Condition[] forecast = channel.getItem().getForecast();

        int actualConditionViewId = getResources().getIdentifier("actualCondition", "id", getPackageName());
        ActualConditionFragment actualConditionFragment = (ActualConditionFragment) getSupportFragmentManager().findFragmentById(actualConditionViewId);

        if (actualConditionFragment != null) {
            actualConditionFragment.loadData(channel);
        }

        int additionalConditionId = getResources().getIdentifier("additionalCondition", "id", getPackageName());
        AdditionalConditionFragment additionalConditionFragment = (AdditionalConditionFragment) getSupportFragmentManager().findFragmentById(additionalConditionId);

        if (additionalConditionFragment != null) {
            additionalConditionFragment.loadData(channel);
        }

        int moonFragmentId = getResources().getIdentifier("moonInfo", "id", getPackageName());
        FragmentMoon moonFragment = (FragmentMoon) getSupportFragmentManager().findFragmentById(moonFragmentId);

        if (moonFragment != null) {
            moonFragment.loadData(channel.getItem().getLatitude(), channel.getItem().getLongtitude(), 15);
        }

        int sunFragmentId = getResources().getIdentifier("sunInfo", "id", getPackageName());
        FragmentSun sunFragment = (FragmentSun) getSupportFragmentManager().findFragmentById(sunFragmentId);

        if (sunFragment != null) {
            sunFragment.loadData(channel.getItem().getLatitude(), channel.getItem().getLongtitude(), 15);
        }

        for (int day = 1; day < forecast.length; day++) {
            if (day >= 5) {
                break;
            }

            Condition currentCondition = forecast[day];

            int viewId = getResources().getIdentifier("forecast_" + day, "id", getPackageName());
            WeatherConditionFragment fragment = (WeatherConditionFragment) getSupportFragmentManager().findFragmentById(viewId);

            if (fragment != null) {
                fragment.loadForecast(currentCondition, channel.getUnits());
            }
        }

        cacheService.save(channel);

        if (connectivity.isConnected(this.getApplicationContext())){
            Toast.makeText(this, "Loaded actual data!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void serviceFailure(Exception exception) {
        loadingDialog.hide();
        // OPTIONAL: let the user know an error has occurred then fallback to the cached data
        if (!connectivity.isConnected(this.getApplicationContext())) {
            //Toast.makeText(this, "No internet connection, loading older data.", Toast.LENGTH_SHORT).show();
            String location = null;
            location = preferences.getString(getString(R.string.pref_manual_location), null);
            cacheService.load(this, location);
        } else {
            Toast.makeText(this, exception.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    private void startSettingsActivity() {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

    private void startFavouritesActivity() {
        Intent intent = new Intent(this, FavouritesActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.settings:
                orientationChanged = false;
                startSettingsActivity();
                return true;
            case R.id.refresh:
                String location = null;
                location = preferences.getString(getString(R.string.pref_manual_location), null);
                orientationChanged = false;

                if (location != null && !connectivity.isConnected(this.getApplicationContext())) {
                    Toast.makeText(this, "No internet connection, loading older data.", Toast.LENGTH_SHORT).show();
                    cacheService.load(this, location);
                } else if (location != null && connectivity.isConnected(this.getApplicationContext())) {
                    Log.i("refresh button", "opening refresh while connected");
                    weatherService.refreshWeather(location);
                }
                return true;
            case R.id.favourites:
                startFavouritesActivity();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
