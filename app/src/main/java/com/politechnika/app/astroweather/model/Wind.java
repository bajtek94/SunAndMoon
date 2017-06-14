package com.politechnika.app.astroweather.model;

import org.json.JSONException;
import org.json.JSONObject;

public class Wind implements JSONPopulator {
    private int direction;
    private double speed;

    public int getDirection() {
        return direction;
    }

    public double getSpeed() {
        return speed;
    }

    public String getStringDirection() {
        return directionToString(direction);
    }

    @Override
    public void populate(JSONObject data) {
        direction = data.optInt("direction");
        speed = data.optDouble("speed");
    }

    @Override
    public JSONObject toJSON() {
        JSONObject data = new JSONObject();
        try {
            data.put("direction", direction);
            data.put("speed", speed);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return data;
    }


    private String directionToString(int direction){
        String directions[] = {"N", "NE", "E", "SE", "S", "SW", "W", "NW"};
        return directions[ (int)Math.round((  ((double)direction % 360) / 45)) % 8 ];
    }
}
