package com.example.battleships;

import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class menu extends AppCompatActivity {

    boolean isPressed = false;
    MediaPlayer background;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu);
        background = MediaPlayer.create(this, R.raw.menu_music);
        background.setVolume(0.5f, 0.5f);
        background.start();

        setButtonOnClickListeners();
    }

    private void setButtonOnClickListeners(){
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(menu.this);
                builder.setMessage("Do You want automated ship placement?");
                builder.setCancelable(true);
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        Intent intent = new Intent(menu.this, deployMenu.class);
                        background.stop();
                        finish();
                        startActivity(intent);
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();
            }
        };
        findViewById(R.id.pveButton).setOnClickListener(listener);

        View.OnClickListener listener2 = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Button button = (Button) v;
                Intent intent = new Intent(menu.this, pvpMenu.class);
                background.stop();
                finish();
                startActivity(intent);
            }
        };
        findViewById(R.id.pvpButton).setOnClickListener(listener2);

        View.OnClickListener listener3 = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Button button = (Button) v;
                Intent intent = new Intent(menu.this, leaderBoard.class);
                background.stop();
                finish();
                startActivity(intent);
            }
        };
        findViewById(R.id.highscoresButton).setOnClickListener(listener3);


        View.OnClickListener listener4 = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Button button = (Button) v;
                if(!isPressed) {
                    button.setBackgroundResource(R.drawable.silent);
                    background.pause();
                    isPressed = true;
                }
                else{
                    button.setBackgroundResource(R.drawable.silent_n);
                    background.start();
                    isPressed = false;
                }
            }
        };
        findViewById(R.id.soundButton).setOnClickListener(listener4);

    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(menu.this);
        builder.setMessage("Do you want to exit?");
        builder.setCancelable(true);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                background.stop();
                finish();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }
}


