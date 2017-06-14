package com.politechnika.app.astroweather.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.politechnika.app.astroweather.R;
import com.politechnika.app.astroweather.model.Channel;


public class AdditionalConditionFragment extends Fragment {
    private TextView humidityTextView;
    private TextView visibilityTextView;
    private TextView windTextView;

    public AdditionalConditionFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_additional_condition, container, false);

        humidityTextView = (TextView) view.findViewById(R.id.humidityTextView);
        visibilityTextView = (TextView) view.findViewById(R.id.visibilityTextView);
        windTextView = (TextView) view.findViewById(R.id.windTextView);

        return view;
    }

    public void loadData(Channel channel) {
        humidityTextView.setText(String.format("Humidity: %s", channel.getAtmosphere().getHumidity()));
        visibilityTextView.setText(String.format("Visibility: %.1f", channel.getAtmosphere().getVisibility()));
        windTextView.setText(String.format("Strenght: %s, Direction: %s", channel.getWind().getSpeed(), channel.getWind().getStringDirection()));
    }
}
