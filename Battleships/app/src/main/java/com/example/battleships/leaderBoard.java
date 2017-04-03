package com.example.battleships;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class leaderBoard extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.highscores);
        setButtonOnClickListeners();
    }

    private void setButtonOnClickListeners() {
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(leaderBoard.this, menu.class);
                finish();
                startActivity(intent);
            }
        };
        findViewById(R.id.returnButton).setOnClickListener(listener);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(leaderBoard.this, menu.class);
        finish();
        startActivity(intent);
    }
}
