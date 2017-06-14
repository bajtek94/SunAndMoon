package com.politechnika.app.astroweather.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.astrocalculator.AstroCalculator;
import com.astrocalculator.AstroDateTime;
import com.politechnika.app.astroweather.R;

import java.util.Calendar;
import java.util.TimeZone;

public class FragmentMoon extends Fragment {
    private AstroCalculator astroCalculator;
    private TextView moonriseValueTextView;
    private TextView moonsetValueTextView;
    private TextView newmoonValueTextView;
    private TextView fullmoonValueTextView;
    private TextView phaseValueTextView;
    private TextView synodicValueTextView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.moon_fragment, container, false);

        moonriseValueTextView = (TextView) view.findViewById(R.id.moon_TV_moonrise_value);
        moonsetValueTextView = (TextView) view.findViewById(R.id.moon_TV_moonset_value);
        newmoonValueTextView = (TextView) view.findViewById(R.id.moon_TV_newmoon_value);
        fullmoonValueTextView = (TextView) view.findViewById(R.id.moon_TV_full_value);
        phaseValueTextView = (TextView) view.findViewById(R.id.moon_TV_phase_value);
        synodicValueTextView = (TextView) view.findViewById(R.id.moon_TV_synodic_value);

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

        setMoonData();

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
                                                                setMoonData();
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

    private void setMoonrise(View view, AstroCalculator astroCalculator) {
        AstroDateTime astroDateTime = astroCalculator.getMoonInfo().getMoonrise();
        ((TextView) view.findViewById(R.id.moon_TV_moonrise_value)).setText(formatToTwoDigits(astroDateTime.getHour()) + ":" + formatToTwoDigits(astroDateTime.getMinute()) + ":" + formatToTwoDigits(astroDateTime.getSecond()));
    }

    private void setMoonset(View view, AstroCalculator astroCalculator) {
        AstroDateTime astroDateTime = astroCalculator.getMoonInfo().getMoonset();
        ((TextView) view.findViewById(R.id.moon_TV_moonset_value)).setText(formatToTwoDigits(astroDateTime.getHour()) + ":" + formatToTwoDigits(astroDateTime.getMinute()) + ":" + formatToTwoDigits(astroDateTime.getSecond()));
    }

    private void setNewmoon(View view, AstroCalculator astroCalculator) {
        AstroDateTime astroDateTime = astroCalculator.getMoonInfo().getNextNewMoon();
        ((TextView) view.findViewById(R.id.moon_TV_newmoon_value)).setText(formatToTwoDigits(astroDateTime.getDay()) + "." + formatToTwoDigits(astroDateTime.getMonth()) + "." + formatToTwoDigits(astroDateTime.getYear()) + "r., " + formatToTwoDigits(astroDateTime.getHour()) + ":" + formatToTwoDigits(astroDateTime.getMinute()) + ":" + formatToTwoDigits(astroDateTime.getSecond()));
    }

    private void setFullmoon(View view, AstroCalculator astroCalculator) {
        AstroDateTime astroDateTime = astroCalculator.getMoonInfo().getNextFullMoon();
        ((TextView) view.findViewById(R.id.moon_TV_full_value)).setText(formatToTwoDigits(astroDateTime.getDay()) + "." + formatToTwoDigits(astroDateTime.getMonth()) + "." + formatToTwoDigits(astroDateTime.getYear()) + "r., " + formatToTwoDigits(astroDateTime.getHour()) + ":" + formatToTwoDigits(astroDateTime.getMinute()) + ":" + formatToTwoDigits(astroDateTime.getSecond()));
    }

    private void setPhase(View view, AstroCalculator astroCalculator) {
        double moonPhase = astroCalculator.getMoonInfo().getIllumination();
        moonPhase *= 100;
        ((TextView) view.findViewById(R.id.moon_TV_phase_value)).setText(String.format("%.2f", moonPhase) + "%");
    }

    private void setSynodicDay(View view, AstroCalculator astroCalculator) {
        Double moonAge = astroCalculator.getMoonInfo().getAge();
        ((TextView) view.findViewById(R.id.moon_TV_synodic_value)).setText(moonAge.intValue() + " dzień");
    }

    private void setMoonData() {
        setMoonrise(this.getView(), astroCalculator);
        setMoonset(this.getView(), astroCalculator);
        setNewmoon(this.getView(), astroCalculator);
        setFullmoon(this.getView(), astroCalculator);
        setPhase(this.getView(), astroCalculator);
        setSynodicDay(this.getView(), astroCalculator);
    }

    private String formatToTwoDigits(int number) {
        return String.format("%1$02d", number);
    }
}
