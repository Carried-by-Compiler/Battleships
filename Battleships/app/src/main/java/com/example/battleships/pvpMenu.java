package com.example.battleships;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class pvpMenu extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pvp_menu);
        setButtonOnClickListeners(); //  initialises the button listeners.
    }

    private void setButtonOnClickListeners() {
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(pvpMenu.this, menu.class); // initialises intent.
                finish(); // ends activity.
                startActivity(intent); // starts intent.
            }
        };
        findViewById(R.id.returnButtonp).setOnClickListener(listener);  // initialises listener.
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(pvpMenu.this, menu.class); // initialises intent.
        finish(); // ends activity.
        startActivity(intent); // starts intent.
    }
}
