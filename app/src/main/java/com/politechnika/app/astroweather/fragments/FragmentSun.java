package com.politechnika.app.astroweather.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.astrocalculator.AstroCalculator;
import com.astrocalculator.AstroDateTime;
import com.politechnika.app.astroweather.R;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class FragmentSun extends Fragment {
    private AstroCalculator astroCalculator;

    private TextView sunriseValueTextView;
    private TextView sunsetValueTextView;
    private TextView twilightValueTextView;
    private TextView civilTwilightValueTextView;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.sun_fragment, container, false);

        sunriseValueTextView = (TextView) view.findViewById(R.id.sun_TV_sunrise_value);
        sunsetValueTextView = (TextView) view.findViewById(R.id.sun_TV_sunset_value);
        twilightValueTextView = (TextView) view.findViewById(R.id.sun_TV_twilight_value);
        civilTwilightValueTextView = (TextView) view.findViewById(R.id.sun_TV_civilTwilight_value);

        return view;
    }

    public void loadData(double latitude, double longtitude, final int refreshTime){
        AstroCalculator.Location location = new AstroCalculator.Location(latitude, longtitude);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeZone(TimeZone.getTimeZone("GMT+2:00"));
        TimeZone timeZone = TimeZone.getDefault();
        int offset = 2;
        AstroDateTime astroDateTime = new AstroDateTime(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE),
                calendar.get(Calendar.SECOND), offset, timeZone.useDaylightTime());
        astroCalculator = new AstroCalculator(astroDateTime, location);

        setSunData();

        Thread t = new Thread() {

            @Override
            public void run() {
                try {
                    while (!isInterrupted()) {
                        Thread.sleep(60000 * refreshTime);
                        if (isAdded()) {
                            getActivity().runOnUiThread(new Runnable() {
                                                            @Override
                                                            public void run() {
                                                                setSunData();
                                                            }
                                                        }
                            );
                        }
                    }
                } catch (InterruptedException e) {
                }
            }
        };
        t.start();

    }

    private void setSunrise(View view, AstroCalculator astroCalculator) {
        Date now = new Date();
        AstroDateTime astroDateTime = astroCalculator.getSunInfo().getSunrise();

        ((TextView) view.findViewById(R.id.sun_TV_sunrise_value)).setText(formatToTwoDigits(astroDateTime.getHour()) + ":" + formatToTwoDigits(astroDateTime.getMinute()) + ":" + formatToTwoDigits(astroDateTime.getSecond()) + ", azymut: " + String.format("%.2f", astroCalculator.getSunInfo().getAzimuthRise()));
    }

    private void setSunset(View view, AstroCalculator astroCalculator) {
        AstroDateTime astroDateTime = astroCalculator.getSunInfo().getSunset();
        ((TextView) view.findViewById(R.id.sun_TV_sunset_value)).setText(formatToTwoDigits(astroDateTime.getHour()) + ":" + formatToTwoDigits(astroDateTime.getMinute()) + ":" + formatToTwoDigits(astroDateTime.getSecond()) + ", azymut: " + String.format("%.2f", astroCalculator.getSunInfo().getAzimuthSet()));
    }

    private void setTwilight(View view, AstroCalculator astroCalculator) {
        AstroDateTime astroDateTime = astroCalculator.getSunInfo().getTwilightEvening();
        ((TextView) view.findViewById(R.id.sun_TV_twilight_value)).setText(formatToTwoDigits(astroDateTime.getHour()) + ":" + formatToTwoDigits(astroDateTime.getMinute()) + ":" + formatToTwoDigits(astroDateTime.getSecond()));
    }

    private void setCivilTwilight(View view, AstroCalculator astroCalculator) {
        AstroDateTime astroDateTime = astroCalculator.getSunInfo().getTwilightMorning();
        ((TextView) view.findViewById(R.id.sun_TV_civilTwilight_value)).setText(formatToTwoDigits(astroDateTime.getHour()) + ":" + formatToTwoDigits(astroDateTime.getMinute()) + ":" + formatToTwoDigits(astroDateTime.getSecond()));
    }

    private void setSunData() {
        setSunrise(this.getView(), astroCalculator);
        setSunset(this.getView(), astroCalculator);
        setTwilight(this.getView(), astroCalculator);
        setCivilTwilight(this.getView(), astroCalculator);
    }

    private String formatToTwoDigits(int number) {
        return String.format("%1$02d", number);
    }

}

