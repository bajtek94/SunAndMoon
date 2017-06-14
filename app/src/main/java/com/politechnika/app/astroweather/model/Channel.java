package com.politechnika.app.astroweather.model;

import org.json.JSONException;
import org.json.JSONObject;

public class Channel implements JSONPopulator {
    private Units units;
    private Item item;
    private Atmosphere atmosphere;
    private Wind wind;
    private Location location;

    public Units getUnits() {
        return units;
    }

    public Item getItem() {
        return item;
    }

    public Location getLocation() {
        return location;
    }

    public Atmosphere getAtmosphere() {
        return atmosphere;
    }

    public Wind getWind() {
        return wind;
    }

    @Override
    public void populate(JSONObject data) {

        units = new Units();
        units.populate(data.optJSONObject("units"));

        item = new Item();
        item.populate(data.optJSONObject("item"));

        atmosphere = new Atmosphere();
        atmosphere.populate(data.optJSONObject("atmosphere"));

        wind = new Wind();
        wind.populate(data.optJSONObject("wind"));

        location = new Location();
        location.populate(data.optJSONObject("location"));
    }

    @Override
    public JSONObject toJSON() {
        JSONObject data = new JSONObject();
        try {
            data.put("units", units.toJSON());
            data.put("item", item.toJSON());
            data.put("atmosphere", atmosphere.toJSON());
            data.put("wind", wind.toJSON());
            data.put("location", location.toJSON());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return data;
    }

}
