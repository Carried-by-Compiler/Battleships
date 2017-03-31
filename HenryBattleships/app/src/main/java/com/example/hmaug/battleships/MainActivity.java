package com.example.hmaug.battleships;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Button pvp;

        pvp = (Button)findViewById(R.id.PvP);
        pvp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Intent intent = new Intent(MainActivity.this, PvPActivity.class);
                    startActivity(intent);
            }
        });

    }

    public void soundOff() {
        final ImageView imgView = (ImageView) findViewById(R.id.soundButton);

        imgView.setTag(1);
        imgView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (1 == (int) imgView.getTag()) {
                    imgView.setImageResource(R.drawable.ic_lock_silent_mode_off);
                    imgView.setTag(2);
                } else {
                    imgView.setImageResource(R.drawable.ic_lock_silent_mode);
                    imgView.setTag(1);
                }
            }
        });
    }

    public void clickExit(View v) {
        finish();
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setMessage("Do you want to exit?");
        builder.setCancelable(true);
        builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                finish();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

  }



