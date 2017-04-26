package com.example.battleships;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
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

public class playerTurn extends AppCompatActivity {

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

    private TextView posField; // initialises the textfield for the position the user wishes to attack.
    private boolean enteredCoord = false; // initialises the boolean to detect that the user has selected a coordinate.
    private String chosenPoint;
    private ArrayList<Point> hitHistory;
    public static Activity playerAct;

    private Game g;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.player_turn);
        playerAct = this;
        this.posField = (TextView) findViewById(R.id.posField); // links the textView to the posField button.
        setButtonOnClickListener(); // initialises the listeners to the buttons.

        hitHistory = new ArrayList<Point>();
        /*ArrayList<Boat> b = deployMenu.game.getBoats();
        Boat c = b.get(0);
        ArrayList<Point> p = c.getPoints();
        posField.setText(p.get(0).getCoordinate());*/

    }

    private void setButtonOnClickListener() {
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Button button = (Button) v;
                posField.setText(button.getText()); // changes text in textView to the text in the button.
                chosenPoint = button.getText().toString();
                enteredCoord = true; // sets to true to show that a coordinate has been chosen.
               /* for (Point p: hitHistory)
                    Log.d("HITHISTORY", p.getCoordinate());*/
            }
        };
        for (int id : gridButtons) {
            findViewById(id).setOnClickListener(listener); // links the listener to the grid buttons.
        }

        View.OnClickListener listener2 = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Button button = (Button) v;
                Point newPoint;
                Point p;
                // checks if a coordinate has been selected.
                if(enteredCoord) {
                    boolean found = false;
                    newPoint = new Point(chosenPoint);
                    for (int i = 0; i < hitHistory.size() && !found; i++) {

                        p = hitHistory.get(i);
                        if (p.equals(newPoint))
                        {
                            found = true;
                            Toast.makeText(playerTurn.this, "You've chosen this coordinate before!", Toast.LENGTH_SHORT).show();
                        }

                    }
                    if (!found) {
                        Log.d("APP", "not found");
                        new HitChecker().execute(new Point(chosenPoint));
                        endTurn(); // ends turn.
                    }

                }

            };
        };
        findViewById(R.id.fireButton).setOnClickListener(listener2); // links listener to the fireButton.
    }

    // end the player's turn
    private void endTurn() {
        Intent intent = new Intent(playerTurn.this, aiTurn.class); // initialises the intent.
        intent.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
        startActivity(intent); // starts the intent.
    }


    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder3 = new AlertDialog.Builder(playerTurn.this); // creates the alert dialog.
        builder3.setMessage("Do you want to return to the Main Menu?");
        builder3.setCancelable(true);
        builder3.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                Intent intent = new Intent(playerTurn.this, menu.class); // initialises the intent.
                BluetoothAdapter mAdapter = BluetoothAdapter.getDefaultAdapter();
                mAdapter.disable();
                finish(); // ends the activity.
                startActivity(intent); // starts the intent.
            }
        });
        builder3.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        AlertDialog alert = builder3.create();
        alert.show();
    }

    private void endGame() {
        Intent intent = new Intent(playerTurn.this, winScreen.class); // initialises the intent.
        intent.putExtra("FINAL_SCORE", deployMenu.game.getScore());
        finish();
        aiTurn.aiAct.finish();
        startActivity(intent);
    }

    private class HitChecker extends AsyncTask <Point, String, Boolean[]> {

        @Override
        protected Boolean[] doInBackground(Point... params) {

            Log.d("THREAD", "Entered hit checker thread.");
            boolean[] hit;
            Boolean hitArray[] = new Boolean[3];

            hit = deployMenu.game.checkIfEnemyWasHit(params[0]);

            for (int i = 0; i < 3; i++)
                hitArray[i] = hit[i];

            if (hitArray[1]) {
                int scoreNow = 100;
                scoreNow = scoreNow * deployMenu.game.getCounter();
                deployMenu.game.setScore(scoreNow);
                deployMenu.game.incrementCounter();
                publishProgress(params[0].getCoordinate());
                hitHistory.add(params[0]);
            } else {
                deployMenu.game.resetCounter();
            }

            return hitArray;
        }

        @Override
        protected void onPostExecute(Boolean[] hit) {
            boolean hitShip = hit[1];
            boolean sunkShip = hit[0];
            boolean defeatedAi = hit[2];

            if (hitShip)
                Toast.makeText(playerTurn.this, "You've hit a battleship!", Toast.LENGTH_SHORT).show();
            if (sunkShip)
                Toast.makeText(playerTurn.this, "You have sunk a battleship!", Toast.LENGTH_SHORT).show();
            if (defeatedAi) {
                Toast.makeText(playerTurn.this, "You won!", Toast.LENGTH_SHORT).show();
                endGame();
            }

            if (!hitShip)
                Toast.makeText(playerTurn.this, "You missed!", Toast.LENGTH_SHORT).show();
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            Button button;
            int identifier = getStringIdentifier(playerTurn.this, values[0]);
            button = (Button)findViewById(identifier);
            button.setBackgroundResource(R.drawable.fire);
        }

        private int getStringIdentifier(Context pContext, String pString) {
            return pContext.getResources().getIdentifier(pString, "id", pContext.getPackageName());
        }
    }
}

