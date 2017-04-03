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
        setButtonOnClickListeners();
    }

    private void setButtonOnClickListeners() {
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(pvpMenu.this, menu.class);
                finish();
                startActivity(intent);
            }
        };
        findViewById(R.id.returnButtonp).setOnClickListener(listener);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(pvpMenu.this, menu.class);
        finish();
        startActivity(intent);
    }
}
