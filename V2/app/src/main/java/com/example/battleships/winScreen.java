package com.example.battleships;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class winScreen extends AppCompatActivity {

    Button button; // initialises the button.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.win_screen);

        TextView v = (TextView)findViewById(R.id.scoreValue);
        int totalScore = getIntent().getIntExtra("TOTAL_SCORE", 100);
        v.setText(String.valueOf(totalScore));
        button = (Button) findViewById(R.id.returnButtonW); // links the button to the end turn button.
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(winScreen.this, menu.class); // initialises the intent.
                finish(); // ends the activity.
                startActivity(intent); // starts the intent.
            }
        });
    }
}
