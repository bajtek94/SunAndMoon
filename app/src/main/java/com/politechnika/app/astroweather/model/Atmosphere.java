package com.politechnika.app.astroweather.model;

import org.json.JSONException;
import org.json.JSONObject;

public class Atmosphere implements JSONPopulator {
    private int humidity;
    private double pressure;
    private double visibility;

    public int getHumidity() {
        return humidity;
    }

    public double getPressure() {
        return pressure;
    }

    public double getVisibility() {
        return visibility;
    }

    @Override
    public void populate(JSONObject data) {
        humidity = data.optInt("humidity");
        pressure = data.optDouble("pressure");
        visibility = data.optDouble("visibility");
    }

    @Override
    public JSONObject toJSON() {
        JSONObject data = new JSONObject();
        try {
            data.put("humidity", humidity);
            data.put("pressure", pressure);
            data.put("visibility", visibility);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return data;
    }
}
