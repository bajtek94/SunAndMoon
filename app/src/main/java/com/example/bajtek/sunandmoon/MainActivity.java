package com.example.bajtek.sunandmoon;

//import android.support.v4.app.FragmentManager;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.res.Configuration;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    static double latitude = 52.23;
    static double longitude = 19.52;
    static  int refreshTime = 15;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
    }

    public void openSettings(View view) {
        startActivity(new Intent(MainActivity.this, SettingsActivity.class));
    }

}
