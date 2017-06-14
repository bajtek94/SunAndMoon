package com.politechnika.app.astroweather.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Item implements JSONPopulator {
    private Condition condition;
    private Condition[] forecast;
    private String dayOfTheWeek;
    private String actualTime;
    private double latitude;
    private double longtitude;

    public Condition getCondition() {
        return condition;
    }

    public Condition[] getForecast() {
        return forecast;
    }

    public String getActualTime() {
        return actualTime;
    }

    public String getDayOfTheWeek() {
        return dayOfTheWeek;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongtitude() {
        return longtitude;
    }

    @Override
    public void populate(JSONObject data) {
        condition = new Condition();
        condition.populate(data.optJSONObject("condition"));

        latitude = data.optDouble("lat");
        longtitude = data.optDouble("long");

        DateFormat sdf = new SimpleDateFormat("EEEE");
        Date d = new Date();
        dayOfTheWeek = sdf.format(d);

        DateFormat dfTime = new SimpleDateFormat("HH:mm");
        actualTime = dfTime.format(d.getTime());

        JSONArray forecastData = data.optJSONArray("forecast");
        forecast = new Condition[forecastData.length()];

        //Log.i("Loguj", "ForecastDataArray in Item: " + forecastData.toString());

        for (int i = 0; i < forecastData.length(); i++) {
            forecast[i] = new Condition();
            try {
                forecast[i].populate(forecastData.getJSONObject(i));
                //Log.i("Loguj", "Dzien " + forecast[i].getDay() + ", description " + forecast[i].getDescription() + ", code " + forecast[i].getCode());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public JSONObject toJSON() {
        JSONObject data = new JSONObject();
        try {
            data.put("condition", condition.toJSON());
            data.put("forecast", new JSONArray(forecast));
            data.put("lat", latitude);
            data.put("long", longtitude);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return data;
    }
}
