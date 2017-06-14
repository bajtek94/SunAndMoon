package com.politechnika.app.astroweather.model;

import org.json.JSONException;
import org.json.JSONObject;

public class Location implements JSONPopulator {
    private String country;
    private String region;
    private String city;
    private String locationString;

    public String getCountry() {
        return country;
    }

    public String getRegion() {
        return region;
    }

    public String getCity() {
        return city;
    }

    public String getLocationString() {
        return String.format("%s, %s", city, (region.length() != 0 ? region : country));
    }

    @Override
    public void populate(JSONObject data) {
        country = data.optString("country");
        region = data.optString("region");
        city = data.optString("city");
    }

    @Override
    public JSONObject toJSON() {
        JSONObject data = new JSONObject();
        try {
            data.put("country", country);
            data.put("region", region);
            data.put("city", city);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return data;
    }
}
