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

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by Bajtek on 04.05.2017.
 */

public class FragmentTimeLocation extends Fragment {
    View view;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.time_location_fragment, container, false);
        updateTimeField(view);

        AstroCalculator.Location location = new AstroCalculator.Location(MainActivity.latitude, MainActivity.longitude);
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();
        String latitudeText;
        String longitudeText;
        if(latitude < 0) {
            latitude -= 2 * latitude;
            latitudeText = Double.toString(latitude) + "S";
        }
        else if ( latitude > 0) {
            latitudeText = Double.toString(latitude) + "N";
        }
        else {
            latitudeText = Double.toString(latitude);
        }
        if(longitude < 0) {
            longitude -= 2 * longitude;
            longitudeText = Double.toString(longitude) + "W";
        }
        else if ( latitude > 0) {
            longitudeText = Double.toString(longitude) + "E";
        }
        else {
            longitudeText = Double.toString(longitude);
        }


        ((TextView)view.findViewById(R.id.time_TV_localization_value)).setText("Szer: " + latitudeText +", dł: " + longitudeText);

        Thread t = new Thread() {

            @Override
            public void run() {
                try {
                    while (!isInterrupted()) {
                        Thread.sleep(1000);
                        if (isAdded()) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                updateTimeField(view);
                            }
                        }

                        );}
                    }
                } catch (InterruptedException e) {
                }
            }
        };

        t.start();

        //((TextView)view.findViewById(R.id.time_TV_time_value)).setText(time);
        return view;
    }

    private void updateTimeField(View view) {
        Calendar calendar = Calendar.getInstance();
        TimeZone timeZone = TimeZone.getDefault();
        Date now = new Date();
        int offset = timeZone.getOffset(now.getTime()) / 1000;
        AstroDateTime astroDateTime = new AstroDateTime(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE),
                calendar.get(Calendar.SECOND), offset, timeZone.useDaylightTime());
        String time = formatToTwoDigits(astroDateTime.getHour()) + ":" + formatToTwoDigits(astroDateTime.getMinute()) + ":" + formatToTwoDigits(astroDateTime.getSecond());
        ((TextView)view.findViewById(R.id.time_TV_time_value)).setText(time);
    }

    private String formatToTwoDigits(int number) {
        return String.format("%1$02d", number);
    }

}
