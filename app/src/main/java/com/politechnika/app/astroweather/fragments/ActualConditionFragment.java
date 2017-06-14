package com.politechnika.app.astroweather.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.politechnika.app.astroweather.R;
import com.politechnika.app.astroweather.model.Atmosphere;
import com.politechnika.app.astroweather.model.Channel;
import com.politechnika.app.astroweather.model.Condition;
import com.politechnika.app.astroweather.model.Item;
import com.politechnika.app.astroweather.model.Location;
import com.politechnika.app.astroweather.model.Units;


public class ActualConditionFragment extends Fragment {
    private ImageView weatherIconImageView;
    private TextView temperatureTextView;
    private TextView dayTextView;
    private TextView pressureTextView;
    private TextView descriptionTextView;
    private TextView locationTextView;
    private TextView coordinatesTextView;

    public ActualConditionFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_actual_condition, container, false);

        weatherIconImageView = (ImageView) view.findViewById(R.id.weatherIconImageView);
        temperatureTextView = (TextView) view.findViewById(R.id.temperatureTextView);
        dayTextView = (TextView) view.findViewById(R.id.windTextView);
        pressureTextView = (TextView) view.findViewById(R.id.pressureTextView);
        descriptionTextView = (TextView) view.findViewById(R.id.descriptionTextView);
        locationTextView = (TextView) view.findViewById(R.id.locationTextView);
        coordinatesTextView = (TextView) view.findViewById(R.id.coordinatesTextView);

        return view;
    }

    public void loadData(Channel channel) {
        Condition condition = channel.getItem().getCondition();
        Units units = channel.getUnits();
        Item item = channel.getItem();
        Atmosphere atmosphere = channel.getAtmosphere();
        Location location = channel.getLocation();

        int weatherIconImageResource = getResources().getIdentifier("icon_" + condition.getCode(), "drawable", getContext().getPackageName());
        weatherIconImageView.setImageResource(weatherIconImageResource);
        temperatureTextView.setText(getString(R.string.temperature_output, condition.getTemperature(), units.getTemperature()));
        dayTextView.setText(String.format("%s, %s", item.getDayOfTheWeek(), item.getActualTime()));
        pressureTextView.setText(String.format("%.1f", atmosphere.getPressure()));
        descriptionTextView.setText(condition.getDescription());
        locationTextView.setText(location.getLocationString());
        coordinatesTextView.setText(String.format("lat: %.2f, long: %.2f", item.getLatitude(), item.getLongtitude()));
    }
}
