package com.example.battleships;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Objects;

public class aiTurn extends AppCompatActivity {

    private int[] gridButtons = {R.id.A1, R.id.A2, R.id.A3, R.id.A4, R.id.A5, R.id.A6, R.id.A7, R.id.A8, R.id.A9, R.id.A10,
            R.id.B1, R.id.B2, R.id.B3, R.id.B4, R.id.B5, R.id.B6, R.id.B7, R.id.B8, R.id.B9, R.id.B10,
            R.id.C1, R.id.C2, R.id.C3, R.id.C4, R.id.C5, R.id.C6, R.id.C7, R.id.C8, R.id.C9, R.id.C10,
            R.id.D1, R.id.D2, R.id.D3, R.id.D4, R.id.D5, R.id.D6, R.id.D7, R.id.D8, R.id.D9, R.id.D10,
            R.id.E1, R.id.E2, R.id.E3, R.id.E4, R.id.E5, R.id.E6, R.id.E7, R.id.E8, R.id.E9, R.id.E10,
            R.id.F1, R.id.F2, R.id.F3, R.id.F4, R.id.F5, R.id.F6, R.id.F7, R.id.F8, R.id.F9, R.id.F10,
            R.id.G1, R.id.G2, R.id.G3, R.id.G4, R.id.G5, R.id.G6, R.id.G7, R.id.G8, R.id.G9, R.id.G10,
            R.id.H1, R.id.H2, R.id.H3, R.id.H4, R.id.H5, R.id.H6, R.id.H7, R.id.H8, R.id.H9, R.id.H10,
            R.id.I1, R.id.I2, R.id.I3, R.id.I4, R.id.I5, R.id.I6, R.id.I7, R.id.I8, R.id.I9, R.id.I10,
            R.id.J1, R.id.J2, R.id.J3, R.id.J4, R.id.J5, R.id.J6, R.id.J7, R.id.J8, R.id.J9, R.id.J10};

    public Button button; // sets up a button for the end turn button.
    private TextView posField;
    ArrayList<String> hitHistory;
    public static Activity aiAct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ai_turn);
        Log.d("AITURN", "ONCREATE called");
        aiAct = this;
        this.posField = (TextView) findViewById(R.id.posField); // links the textfied
        hitHistory = new ArrayList<String>();

        button = (Button) findViewById(R.id.turnButton); // links the button to the end turn button.
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    Intent intent = new Intent(aiTurn.this, playerTurn.class); // initialises the intent.
                    intent.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
                    startActivity(intent); // starts the intent.
            }
        });

        new DisplayBoat().execute();

    }

    @Override
    protected void onResume() {
        super.onResume();
        new AiMakesGuess().execute();
    }

    private void endGame() {
        Intent intent = new Intent(aiTurn.this, loseScreen.class); // initialises the intent.
        finish();
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder4 = new AlertDialog.Builder(aiTurn.this); // creates the alert dialog.
        builder4.setMessage("Do you want to return to the Main Menu?");
        builder4.setCancelable(true);
        builder4.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                Intent intent = new Intent(aiTurn.this, menu.class); // initialises the intent.
                finish(); // ends the activity.
                startActivity(intent); // starts the intent.
            }
        });
        builder4.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        AlertDialog alert = builder4.create();
        alert.show();
    }

    private class DisplayBoat extends AsyncTask<Void, Integer, Void> {

        @Override
        protected Void doInBackground(Void... params) {

            ArrayList<Boat> boats = deployMenu.game.getBoats();
            ArrayList<Point> points;
            Button button;

            for (Boat b : boats) {

                points = b.getPoints();

                for (Point p : points) {

                    String coordinate = p.getCoordinate();
                    int identifier = getStringIdentifier(aiTurn.this, coordinate);
                    if (hitHistory.contains(coordinate))
                        publishProgress(identifier, 1);
                    else
                        publishProgress(identifier, 2);

                }
            }

            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            Button button;
            button = (Button)findViewById(values[0]);
            if (values[1] == 1)
                button.setBackgroundResource(R.drawable.silent);
            else
                button.setBackgroundResource(R.drawable.fire);
        }

        private int getStringIdentifier(Context pContext, String pString) {
            return pContext.getResources().getIdentifier(pString, "id", pContext.getPackageName());
        }
    }



    private class AiMakesGuess extends AsyncTask <Void, Point, Object[]> {

        @Override
        protected Object[] doInBackground(Void... params) {

            Object[] hitArray = new Object[4];
            Point aiGuess;

            do {
                aiGuess = deployMenu.game.aiGenerateGuess();
            } while (hitHistory.contains(aiGuess.getCoordinate()));

            boolean[] hits = deployMenu.game.aiGuesses(aiGuess);

            for (int i = 0; i < 3; i++)
                hitArray[i] = hits[i];

            if ((boolean)hitArray[1]) {
                publishProgress(aiGuess);
                hitHistory.add(aiGuess.getCoordinate());
                Log.d("AI", "" + hitHistory);
            }

            hitArray[3] = aiGuess;

            return hitArray;
        }

        @Override
        protected void onProgressUpdate(Point... values) {
            super.onProgressUpdate(values);
            Button button;
            int identifier = getStringIdentifier(aiTurn.this, values[0].getCoordinate());
            button = (Button)findViewById(identifier);
            button.setBackgroundResource(R.drawable.silent);
        }

        @Override
        protected void onPostExecute(Object[] hit) {
            boolean hitShip = (boolean)hit[1];
            boolean sunkShip = (boolean)hit[0];
            boolean defeatedAi = (boolean)hit[2];
            Point aiGuess = (Point)hit[3];

            if (hitShip)
                posField.setText("You have been hit at: " + aiGuess.getCoordinate());
            if (sunkShip)
                posField.setText("A ship has sunk! AI guessed at: " + aiGuess.getCoordinate());
            if (defeatedAi) {
                endGame();
            }

            if (!hitShip)
                posField.setText("AI Missed at: " + aiGuess.getCoordinate());
        }

        private int getStringIdentifier(Context pContext, String pString) {
            return pContext.getResources().getIdentifier(pString, "id", pContext.getPackageName());
        }
    }
}

