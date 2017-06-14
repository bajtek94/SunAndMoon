package com.politechnika.app.astroweather.listener;

import com.politechnika.app.astroweather.model.Channel;

public interface WeatherServiceListener {
    void serviceSuccess(Channel channel);

    void serviceFailure(Exception exception);
}
