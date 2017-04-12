package com.example.battleships;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class loseScreen extends AppCompatActivity {

    Button button; // initialises the button.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lose_screen);

        button = (Button) findViewById(R.id.returnButtonl); // links the button to the end turn button.
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(loseScreen.this, menu.class); // initialises the intent.
                finish(); // ends the activity.
                startActivity(intent); // starts the intent.
            }
        });
    }
}
