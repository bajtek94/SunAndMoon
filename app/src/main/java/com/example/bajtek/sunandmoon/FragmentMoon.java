package com.example.bajtek.sunandmoon;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.astrocalculator.AstroCalculator;
import com.astrocalculator.AstroDateTime;

import java.sql.Time;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by Bajtek on 04.05.2017.
 */

public class FragmentMoon extends Fragment {
    View view;
    AstroCalculator astroCalculator;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.moon_fragment, container, false);

        AstroCalculator.Location location = new AstroCalculator.Location(MainActivity.latitude, MainActivity.longitude);
        Calendar calendar = Calendar.getInstance();
        TimeZone timeZone = TimeZone.getDefault();
        Date now = new Date();
        int offset = timeZone.getOffset(now.getTime()) / 1000;
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
                        Thread.sleep(60000*MainActivity.refreshTime);
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


        return view;
    }

    private void setMoonrise(View view, AstroCalculator astroCalculator) {
        AstroDateTime astroDateTime = astroCalculator.getMoonInfo().getMoonrise();
        ((TextView)view.findViewById(R.id.moon_TV_moonrise_value)).setText(formatToTwoDigits(astroDateTime.getHour()) + ":" + formatToTwoDigits(astroDateTime.getMinute()) + ":" + formatToTwoDigits(astroDateTime.getSecond()));
    }
    private void setMoonset(View view, AstroCalculator astroCalculator) {
        AstroDateTime astroDateTime = astroCalculator.getMoonInfo().getMoonset();
        ((TextView)view.findViewById(R.id.moon_TV_moonset_value)).setText(formatToTwoDigits(astroDateTime.getHour()) + ":" + formatToTwoDigits(astroDateTime.getMinute()) + ":" + formatToTwoDigits(astroDateTime.getSecond()));
    }
    private void setNewmoon(View view, AstroCalculator astroCalculator) {
        AstroDateTime astroDateTime = astroCalculator.getMoonInfo().getNextNewMoon();
        Date date = new Date();
        Time timeOfNewmoon = new Time(astroDateTime.getHour(), astroDateTime.getMinute(), astroDateTime.getSecond());
        long timeToNewmoon = timeOfNewmoon.getTime() - date.getTime();
        Time time = new Time(timeToNewmoon);

//        Calendar cal = Calendar.getInstance();
//
//
//        long different = timeOfNewmoon.getTime() - cal.getTime().getTime();
//
//        long secondsInMilli = 1000;
//        long minutesInMilli = secondsInMilli * 60;
//        long hoursInMilli = minutesInMilli * 60;
//        long daysInMilli = hoursInMilli * 24;
//
//        long elapsedDays = different / daysInMilli;
//        different = different % daysInMilli;
//
//        long elapsedHours = different / hoursInMilli;
//        different = different % hoursInMilli;
//
//        long elapsedMinutes = different / minutesInMilli;
//        different = different % minutesInMilli;
//
//        long elapsedSeconds = different / secondsInMilli;


        ((TextView)view.findViewById(R.id.moon_TV_newmoon_value)).setText(formatToTwoDigits(astroDateTime.getHour()) + ":" + formatToTwoDigits(astroDateTime.getMinute()) + ":" + formatToTwoDigits(astroDateTime.getSecond()));
        //((TextView)view.findViewById(R.id.moon_TV_newmoon_value)).setText(elapsedDays + "d " + elapsedHours + "h " + elapsedMinutes + "m" );
    }
    private void setFullmoon(View view, AstroCalculator astroCalculator) {
        AstroDateTime astroDateTime = astroCalculator.getMoonInfo().getNextFullMoon();
        ((TextView)view.findViewById(R.id.moon_TV_full_value)).setText(formatToTwoDigits(astroDateTime.getHour()) + ":" + formatToTwoDigits(astroDateTime.getMinute()) + ":" + formatToTwoDigits(astroDateTime.getSecond()));
    }
    private void setPhase(View view, AstroCalculator astroCalculator) {
        double moonPhase = astroCalculator.getMoonInfo().getIllumination();
        moonPhase *= 100;
        ((TextView)view.findViewById(R.id.moon_TV_phase_value)).setText(String.format("%.2f", moonPhase) + "%");
    }
    private void setSynodicDay(View view, AstroCalculator astroCalculator) {
        ///AstroDateTime astroDateTime = astroCalculator.getMoonInfo()....;
        ((TextView)view.findViewById(R.id.moon_TV_synodic_value)).setText("6 dzień");
        ///((TextView)view.findViewById(R.id.moon_TV_synodic_value)).setText(formatToTwoDigits(astroDateTime.getHour()) + ":" + formatToTwoDigits(astroDateTime.getMinute()) + ":" + formatToTwoDigits(astroDateTime.getSecond()));
    }
    private  void setMoonData() {
        setMoonrise(view, astroCalculator);
        setMoonset(view, astroCalculator);
        setNewmoon(view, astroCalculator);
        setFullmoon(view, astroCalculator);
        setPhase(view, astroCalculator);
        setSynodicDay(view, astroCalculator);
    }

    private String formatToTwoDigits(int number) {
        return String.format("%1$02d", number);
    }
}
