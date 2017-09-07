package com.example.battleships;

import android.content.Intent;
<<<<<<< HEAD
import android.media.MediaPlayer;
=======
>>>>>>> origin/master
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class loseScreen extends AppCompatActivity {

    Button button; // initialises the button.
<<<<<<< HEAD
    private MediaPlayer mp;
=======
>>>>>>> origin/master

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lose_screen);

<<<<<<< HEAD
        mp = MediaPlayer.create(this, R.raw.background_music);
        mp.setLooping(true);
        mp.start();

=======
>>>>>>> origin/master
        button = (Button) findViewById(R.id.returnButtonl); // links the button to the end turn button.
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(loseScreen.this, menu.class); // initialises the intent.
<<<<<<< HEAD
                mp.stop();
=======
>>>>>>> origin/master
                finish(); // ends the activity.
                startActivity(intent); // starts the intent.
            }
        });
    }
}
