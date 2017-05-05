package com.example.bajtek.sunandmoon;

import android.app.Fragment;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.astrocalculator.AstroCalculator;
import com.astrocalculator.AstroDateTime;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by Bajtek on 04.05.2017.
 */

public class FragmentSun extends Fragment {
    View view;
    AstroCalculator astroCalculator;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
       view = inflater.inflate(R.layout.sun_fragment, container, false);

        AstroCalculator.Location location = new AstroCalculator.Location(MainActivity.latitude, MainActivity.longitude);
        Calendar calendar = Calendar.getInstance();
        TimeZone timeZone = TimeZone.getDefault();
        Date now = new Date();
        int offset = timeZone.getOffset(now.getTime()) / 1000;
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
                    Thread.sleep(60000*MainActivity.refreshTime);
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

        return view;
    }

    private void setSunrise(View view, AstroCalculator astroCalculator) {
        Date now = new Date();
        AstroDateTime astroDateTime = astroCalculator.getSunInfo().getSunrise();

        ((TextView)view.findViewById(R.id.sun_TV_sunrise_value)).setText(formatToTwoDigits(astroDateTime.getHour()) + ":" + formatToTwoDigits(astroDateTime.getMinute()) + ":" + formatToTwoDigits(astroDateTime.getSecond()) + ", azymut: " + String.format("%.2f",astroCalculator.getSunInfo().getAzimuthRise()) );
    }
    private void setSunset(View view, AstroCalculator astroCalculator) {
        AstroDateTime astroDateTime = astroCalculator.getSunInfo().getSunset();
        ((TextView)view.findViewById(R.id.sun_TV_sunset_value)).setText(formatToTwoDigits(astroDateTime.getHour()) + ":" + formatToTwoDigits(astroDateTime.getMinute()) + ":" + formatToTwoDigits(astroDateTime.getSecond()) + ", azymut: " + String.format("%.2f",astroCalculator.getSunInfo().getAzimuthSet()) );
    }
    private void setTwilight(View view, AstroCalculator astroCalculator) {
        AstroDateTime astroDateTime = astroCalculator.getSunInfo().getTwilightEvening();
        ((TextView)view.findViewById(R.id.sun_TV_twilight_value)).setText(formatToTwoDigits(astroDateTime.getHour()) + ":" + formatToTwoDigits(astroDateTime.getMinute())  + ":" + formatToTwoDigits(astroDateTime.getSecond()));
    }
    private void setCivilTwilight(View view, AstroCalculator astroCalculator) {
        AstroDateTime astroDateTime = astroCalculator.getSunInfo().getTwilightMorning();
        ((TextView)view.findViewById(R.id.sun_TV_civilTwilight_value)).setText(formatToTwoDigits(astroDateTime.getHour()) + ":" + formatToTwoDigits(astroDateTime.getMinute()) + ":" + formatToTwoDigits(astroDateTime.getSecond()));
    }
    private  void setSunData() {
        setSunrise(view, astroCalculator);
        setSunset(view, astroCalculator);
        setTwilight(view, astroCalculator);
        setCivilTwilight(view, astroCalculator);
    }

    private String formatToTwoDigits(int number) {
        return String.format("%1$02d", number);
    }

}

