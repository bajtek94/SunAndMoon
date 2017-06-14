package com.politechnika.app.astroweather.model;

import org.json.JSONException;
import org.json.JSONObject;


public class Query implements JSONPopulator{
    private int count;

    public int getCount() {
        return count;
    }

    @Override
    public void populate(JSONObject data) {
        count = data.optInt("count");
    }

    @Override
    public JSONObject toJSON() {
        JSONObject data = new JSONObject();
        try {
            data.put("count", count);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return data;
    }
}
