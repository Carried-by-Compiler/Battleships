package com.example.battleships;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void setWifiText(int indicator) {
        TextView v = (TextView)findViewById(R.id.wifi_indicator);

        if (indicator == 1) { // wifi is on
            v.setText("Wifi is turned on");
        }
        else {
            v.setText("Wifi us turned off");
        }
    }
}
