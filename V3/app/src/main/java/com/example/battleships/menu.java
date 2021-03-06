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

    /*boolean isPressed = false; // used boolean for the sound button to detected if the button has been pressed before.
    MediaPlayer background; // used for the background music.*/

    public Button  btnViewData;
    DatabaseHelper mDatabaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu);
        btnViewData = (Button) findViewById((R.id.highscoresButton));
        mDatabaseHelper = new com.example.battleships.DatabaseHelper(this);

        btnViewData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(menu.this, leaderBoard.class);
                startActivity(intent);
            }
        });


        setButtonOnClickListeners(); //  initialises the button listeners.
    }

    private void setButtonOnClickListeners(){
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(menu.this); // creates alert dialog.
                builder.setMessage("Do You want automated ship placement?");
                builder.setCancelable(true);
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        /*dialog.cancel();*/
                        Intent intent = new Intent(menu.this, deployMenu.class);
                        intent.putExtra("DEPLOY_MESSAGE", 1);
                        intent.putExtra("MULTIPLAYER", 0);
                        finish();
                        startActivity(intent);
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        Intent intent = new Intent(menu.this, deployMenu.class);
                        intent.putExtra("DEPLOY_MESSAGE", 0);
                        finish(); // Ends activity.
                        startActivity(intent); // starts intent.
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();
            }
        };
        findViewById(R.id.pveButton).setOnClickListener(listener); // initialises listener.

        View.OnClickListener listener2 = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Button button = (Button) v;
                Intent intent = new Intent(menu.this, pvpMenu.class); // initialises intent.
                finish(); // Ends activity.
                startActivity(intent); // starts intent.
            }
        };
        findViewById(R.id.pvpButton).setOnClickListener(listener2); // initialises listener.

        View.OnClickListener listener3 = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Button button = (Button) v;
                Intent intent = new Intent(menu.this, leaderBoard.class); // initialises intent.
                finish(); // ends activity.
                startActivity(intent); // starts intent.
            }
        };
        findViewById(R.id.highscoresButton).setOnClickListener(listener3); // initialises listener.


       /* View.OnClickListener listener4 = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Button button = (Button) v;
                if(!isPressed) {
                    button.setBackgroundResource(R.drawable.silent); // switches icon.
                    background.pause(); // pauses music.
                    isPressed = true; // sets to button has been pressed.
                }
                else{
                    button.setBackgroundResource(R.drawable.silent_n); // switches icon.
                    background.start(); // turns music back on.
                    isPressed = false; // sets to button has not been pressed.
                }
            }
        };
        findViewById(R.id.soundButton).setOnClickListener(listener4); // initialises listener.*/

    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(menu.this); // creates alert dialog.
        builder.setMessage("Do you want to exit?");
        builder.setCancelable(true);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                finishAffinity(); // Ends application
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


