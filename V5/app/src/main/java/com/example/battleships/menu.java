package com.example.battleships;

import android.bluetooth.BluetoothAdapter;
import android.content.DialogInterface;
import android.content.Intent;
<<<<<<< HEAD
import android.media.MediaPlayer;
=======
>>>>>>> origin/master
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.MutableInt;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


public class menu extends AppCompatActivity {

    DatabaseHelper mDatabaseHelper;
    private Button  highscoresButton;
    private boolean MusicRunning = false;
<<<<<<< HEAD
    private MediaPlayer mp;
=======
>>>>>>> origin/master

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu);

<<<<<<< HEAD
        mp = MediaPlayer.create(this, R.raw.background_music);
        mp.setLooping(true);
        mp.start();

=======
>>>>>>> origin/master
        highscoresButton = (Button) findViewById((R.id.highscoresButton));
        mDatabaseHelper = new DatabaseHelper(this);


        highscoresButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(menu.this, ListDataActivity.class);
<<<<<<< HEAD
                mp.stop();
=======
>>>>>>> origin/master
                startActivity(intent);
            }
        });

        setButtonOnClickListeners(); //  initialises the button listeners.
    }

    private  void toastMessage(String message){
        Toast.makeText(this,message, Toast.LENGTH_SHORT).show();
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
<<<<<<< HEAD
                        mp.stop();
=======
>>>>>>> origin/master
                        finish();
                        startActivity(intent);
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        Intent intent = new Intent(menu.this, deployMenu.class);
                        intent.putExtra("DEPLOY_MESSAGE", 0);
<<<<<<< HEAD
                        mp.stop();
=======
>>>>>>> origin/master
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
<<<<<<< HEAD
                mp.stop();
=======
>>>>>>> origin/master
                finish(); // Ends activity.
                startActivity(intent); // starts intent.
            }
        };
        findViewById(R.id.pvpButton).setOnClickListener(listener2); // initialises listener.

        View.OnClickListener listener3 = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Button button = (Button) v;
<<<<<<< HEAD
                Intent intent = new Intent(menu.this, leaderBoard.class); // initialises intent.
                mp.stop();
=======
                Intent intent = new Intent(menu.this, ListDataActivity.class); // initialises intent.
>>>>>>> origin/master
                finish(); // ends activity.
                startActivity(intent); // starts intent.
            }
        };
        findViewById(R.id.highscoresButton).setOnClickListener(listener3); // initialises listener.

    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(menu.this); // creates alert dialog.
        builder.setMessage("Do you want to exit?");
        builder.setCancelable(true);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                BluetoothAdapter b = BluetoothAdapter.getDefaultAdapter();
                b.disable();
<<<<<<< HEAD
                mp.stop();
=======
>>>>>>> origin/master
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


