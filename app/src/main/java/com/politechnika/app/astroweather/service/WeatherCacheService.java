package com.politechnika.app.astroweather.service;

import android.content.Context;
import android.os.AsyncTask;
import android.util.JsonWriter;
import android.util.Log;

import com.politechnika.app.astroweather.R;
import com.politechnika.app.astroweather.model.Atmosphere;
import com.politechnika.app.astroweather.model.Channel;
import com.politechnika.app.astroweather.model.Condition;
import com.politechnika.app.astroweather.model.Item;
import com.politechnika.app.astroweather.model.Location;
import com.politechnika.app.astroweather.model.Units;
import com.politechnika.app.astroweather.model.Wind;
import com.politechnika.app.astroweather.listener.WeatherServiceListener;
import org.json.JSONObject;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;

public class WeatherCacheService {
    private Context context;
    private Exception error;
    private final String CACHED_WEATHER_FILE = "weather.json";

    public WeatherCacheService(Context context) {
        this.context = context;
    }

    public void save(final Channel channel) {
        new AsyncTask<Channel, Void, Void>() {
            @Override
            protected Void doInBackground(Channel[] channels) {

                FileOutputStream outputStream;
                FileWriter fw;

                try {
                    outputStream = context.openFileOutput(channels[0].getLocation().getCity()+ ".json", Context.MODE_PRIVATE);
                    fw = new FileWriter(outputStream.getFD());
                    writeJsonStream(fw, channels[0]);
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                return null;
            }
        }.execute(channel);
    }

    public void load(final WeatherServiceListener listener, final String location) {
        new AsyncTask<WeatherServiceListener, Void, Channel>() {
            private WeatherServiceListener weatherListener;

            @Override
            protected Channel doInBackground(WeatherServiceListener[] serviceListeners) {
                weatherListener = serviceListeners[0];
                try {
                    FileInputStream inputStream = context.openFileInput(location + ".json");

                    StringBuilder cache = new StringBuilder();
                    int content;
                    while ((content = inputStream.read()) != -1) {
                        cache.append((char) content);
                    }

                    inputStream.close();

                    JSONObject jsonCache = new JSONObject(cache.toString());
                    Log.i("Load data: JSON", jsonCache.toString());

                    Channel channel = new Channel();
                    try{
                        channel.populate(jsonCache);
                    }catch(Exception e){
                        e.printStackTrace();
                    }

                    return channel;
                } catch (FileNotFoundException e) { // cache file doesn't exist
                    error = new CacheException(context.getString(R.string.cache_exception));
                } catch (Exception e) {
                    error = e;
                }

                return null;
            }

            @Override
            protected void onPostExecute(Channel channel) {
                if (channel == null && error != null) {
                    weatherListener.serviceFailure(error);
                } else {
                    weatherListener.serviceSuccess(channel);
                }
            }
        }.execute(listener);
    }

    private class CacheException extends Exception {
        CacheException(String detailMessage) {
            super(detailMessage);
        }
    }

    public void writeJsonStream(FileWriter fw, Channel channel) throws IOException {
        JsonWriter writer = new JsonWriter(fw);
        writer.setIndent("  ");
        writeObject(writer, channel);
        writer.close();
    }

    public void writeObject(JsonWriter writer, Channel channel) throws IOException {
        writer.beginObject();
        writeUnits(writer, channel.getUnits());
        writeItem(writer, channel.getItem());
        writeAtmosphere(writer, channel.getAtmosphere());
        writeWind(writer, channel.getWind());
        writeLocation(writer, channel.getLocation());
        writer.endObject();
    }

    private void writeLocation(JsonWriter writer, Location location) throws IOException {
        writer.name("location");
        writer.beginObject();
        writer.name("city").value(location.getCity());
        writer.name("country").value(location.getCountry());
        writer.name("region").value(location.getRegion());
        writer.endObject();
    }

    private void writeWind(JsonWriter writer, Wind wind) throws IOException {
        writer.name("wind");
        writer.beginObject();
        writer.name("direction").value(wind.getDirection());
        writer.name("speed").value(wind.getSpeed());
        writer.endObject();
    }

    private void writeAtmosphere(JsonWriter writer, Atmosphere atmosphere) throws IOException {
        writer.name("atmosphere");
        writer.beginObject();
        writer.name("humidity").value(atmosphere.getHumidity());
        writer.name("pressure").value(atmosphere.getPressure());
        writer.name("visibility").value(atmosphere.getVisibility());
        writer.endObject();
    }

    private void writeItem(JsonWriter writer, Item item) throws IOException {
        writer.name("item");
        writer.beginObject();
        writer.name("lat").value(item.getLatitude());
        writer.name("long").value(item.getLongtitude());
        writeCondition(writer, item.getCondition());
        writeForecast(writer, item.getForecast());
        writer.endObject();
    }

    private void writeForecast(JsonWriter writer, Condition[] forecasts) throws IOException {
        writer.name("forecast");
        writer.beginArray();
        for (Condition forecast : forecasts) {
            //Log.i("Log cache", "Forecast code: "  + forecast.getCode() + ", date: " + forecast.getDay() + ", day" + forecast.getDay());
            writeOneForecast(writer, forecast);
        }
        writer.endArray();
    }

    private void writeOneForecast(JsonWriter writer, Condition forecast) throws IOException {
        writer.beginObject();
        writer.name("code").value(forecast.getCode());
        writer.name("date").value(forecast.getDate());
        writer.name("day").value(forecast.getDay());
        writer.name("high").value(forecast.getHighTemperature());
        writer.name("low").value(forecast.getLowTemperature());
        writer.name("text").value(forecast.getDescription());
        writer.endObject();
    }


    private void writeCondition(JsonWriter writer, Condition condition) throws IOException {
        writer.name("condition");
        writer.beginObject();
        writer.name("code").value(condition.getCode());
        writer.name("temp").value(condition.getTemperature());
        writer.name("high").value(condition.getHighTemperature());
        writer.name("low").value(condition.getLowTemperature());
        writer.name("text").value(condition.getDescription());
        writer.name("day").value(condition.getDay());
        writer.endObject();
    }

    private void writeUnits(JsonWriter writer, Units units) throws IOException {
        writer.name("units");
        writer.beginObject();
        writer.name("temperature").value(units.getTemperature());
        writer.endObject();
    }
}
