package com.example.battleships;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by johnr on 26/04/2017.
 */

public class ViewBoats extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.player_turn);

        new DisplayBoats().execute();
        Button fireButton = (Button)findViewById(R.id.fireButton);
        fireButton.setVisibility(View.INVISIBLE);
        TextView textView1 = (TextView)findViewById(R.id.textView1);
        textView1.setText("Score:");
        TextView posField = (TextView)findViewById(R.id.posField);
        posField.setText(String.valueOf(PVP.game.getScore()));
        TextView hitMarker = (TextView)findViewById(R.id.hit_marker);
        hitMarker.setVisibility(View.INVISIBLE);
        TextView status = (TextView)findViewById(R.id.status_message);
        hitMarker.setVisibility(View.INVISIBLE);
        Button viewBoats = (Button)findViewById(R.id.view_boats);
        viewBoats.setVisibility(View.VISIBLE);
        viewBoats.setText("Go Back");
        viewBoats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewBoats.this.finish();
            }
        });
    }

    private int getStringIdentifier(Context pContext, String pString) {
        return pContext.getResources().getIdentifier(pString, "id", pContext.getPackageName());
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        ViewBoats.this.finish();
    }

    private class DisplayBoats extends AsyncTask<Void, Integer, Void> {

        @Override
        protected Void doInBackground(Void... params) {

            for (Boat b: PVP.game.getBoats()) {
                for (Point p: b.getPoints()) {
                    String stringId = p.getCoordinate();
                    int identifier = getStringIdentifier(ViewBoats.this, stringId);
                    publishProgress(identifier, 0);
                }
            }

            for (Point p: PVP.hitPoints) {
                String stringId = p.getCoordinate();
                int identifier = getStringIdentifier(ViewBoats.this, stringId);
                publishProgress(identifier, 1);
            }



            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            Button button;
            button = (Button)findViewById(values[0]);

            if (values[1] == 1)
                button.setBackgroundResource(R.drawable.fire);
            else
                button.setBackgroundResource(R.drawable.ship_hit);
        }
    }
}
