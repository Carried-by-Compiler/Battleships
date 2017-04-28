package com.example.battleships;

import android.content.Intent;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class loseScreen extends AppCompatActivity {

    Button button; // initialises the button.
    private MediaPlayer mp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lose_screen);

        mp = MediaPlayer.create(this, R.raw.background_music);
        mp.setLooping(true);
        mp.start();

        button = (Button) findViewById(R.id.returnButtonl); // links the button to the end turn button.
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(loseScreen.this, menu.class); // initialises the intent.
                mp.stop();
                finish(); // ends the activity.
                startActivity(intent); // starts the intent.
            }
        });
    }
}
