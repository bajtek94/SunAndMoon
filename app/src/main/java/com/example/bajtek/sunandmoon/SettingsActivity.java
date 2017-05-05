package com.example.bajtek.sunandmoon;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        ((TextView)findViewById(R.id.latitudeValue)).setText(Double.toString(MainActivity.latitude));
        ((TextView)findViewById(R.id.longitudeValue)).setText(Double.toString(MainActivity.longitude));
        ((TextView)findViewById(R.id.refreshTimeValue)).setText(Integer.toString(MainActivity.refreshTime));
    }


    public void save(View view) {

        startActivity(new Intent(SettingsActivity.this, MainActivity.class));
        MainActivity.latitude = Double.parseDouble((((TextView)findViewById(R.id.latitudeValue)).getText()).toString());
        MainActivity.longitude = Double.parseDouble((((TextView)findViewById(R.id.longitudeValue)).getText()).toString());
        MainActivity.refreshTime = Integer.parseInt((((TextView)findViewById(R.id.refreshTimeValue)).getText()).toString());
    }
    public void cancel(View view) {
        startActivity(new Intent(SettingsActivity.this, MainActivity.class));
    }


}
