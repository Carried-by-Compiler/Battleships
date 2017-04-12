package com.example.battleships;

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

import org.w3c.dom.Text;

import java.util.ArrayList;

public class deployMenu extends AppCompatActivity {

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

    private TextView startPosField; // initialises textfield for starting coordinate.
    private TextView endPosField;  // initialises textfield for starting coordinate.
    private int Coord;        // initialises int for detecting which stage the activity is in.
    protected static Game game;
    private Point p1;
    private Point p2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.deploy_menu);
        this.startPosField = (TextView) findViewById(R.id.startingCoordinate); // sets the textfield to the startConfirm button.
        this.endPosField = (TextView) findViewById(R.id.endingCoordinate); //sets the textfield to the endConfirm button.
        TextView tv = (TextView)findViewById(R.id.PosMsg);

        AI ai = new AI();
        game = new Game(ai);
        tv.setText("Enter Position For: " + game.getNextBoatName());



        Bundle extras = getIntent().getExtras();
        int message = extras.getInt("DEPLOY_MESSAGE");

        if (message == 1) { // auto generate boats selected
            new BoatGenerator().execute();
            tv.setText("Satisfied with boat placement?");
            Button startGameButton = (Button)findViewById(R.id.endConfirm);
            Button generateAgainButton = (Button)findViewById(R.id.startConfirm);

            startGameButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    playGame();
                }
            });

            generateAgainButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    recreate();
                }
            });
        } else {
            Coord = 1;
            setGridOnClickListener(); //initialises the grid listener and applys to all buttons.
            setButtonOnClickListeners(); // initialises the remaining buttons.
        }

    }

    // Takes in account of which end of boats is being entered.
    private void setGridOnClickListener() {
        Log.d("STEP", "Entered setGridOnClickListener()");
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("STEP", "Entered setGridOnClickListener()");
                Button button = (Button) v;
                if (Coord == 1)
                    // sets the text in the textfield to be the same as text in the button clicked.
                    startPosField.setText(button.getText());
                else if (Coord == 2) {
                    // sets the text in the textfield to be the same as text in the button clicked.
                    endPosField.setText(button.getText());

                }
                Log.d("APP", "Coord #: " + Coord);
            }
        };
        for (int id : gridButtons) {
            findViewById(id).setOnClickListener(listener);
        }
    }

    // Taking input from screen
    private void setButtonOnClickListeners() {

        View.OnClickListener listener2 = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("APP", "startConfirm button pressed");
                Button button = (Button) v;
                //Need to add in taking the value of the start coordinate
                p1 = new Point(startPosField.getText().toString());
                Coord++; // increments Coord so that the user can select the end coordinates.
            }
        };

        findViewById(R.id.startConfirm).setOnClickListener(listener2);

        View.OnClickListener listener3 = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("APP", "endConfirm button pressed");
                Button button = (Button) v;
                if(Coord == 2) {
                    Boat b;
                    TextView tv = (TextView)findViewById(R.id.PosMsg);

                    p2 = new Point(endPosField.getText().toString()); // Create new Point object
                    boolean correct = game.checkIfValid(p1, p2);

                    if (correct) { // if correct, create a boat object and place it in the "virtual grid"
                        b = game.createBoat(p1, p2);
                        placeBoatOnGrid(b);
                        Log.d("APP", "Number of boats: " + game.getNumberOfBoats());
                    }

                    // if there are 7 boats, move to main game
                    if (game.getNumberOfBoats() == 7) {
                        Log.d("APP", "Starting next activity");
                        playGame(); // starts the user portion of the game.
                    } else { // else start choosing again
                        Coord = 1;
                        String nextBoat = game.getNextBoatName();
                        tv.setText("Enter Position For: " + nextBoat);
                    }

                }
            }
        };

        findViewById(R.id.endConfirm).setOnClickListener(listener3);
    }

    private void placeBoatOnGrid(Boat b) {
        ArrayList<Point> points = b.getPoints();
        Point p;
        Button button;
        Log.d("APP", "Entered placeBoatsOnGrid() method");
        for (int i = 0; i < points.size(); i++) {
            p = points.get(i);
            String id = p.getCoordinate();
            Log.d("APP", "String id: " + id);
            int identifier = getStringIdentifier(this, id);
            Log.d("APP", "ID: " + identifier);
            button = (Button)findViewById(identifier);
            button.setBackgroundResource(R.drawable.fire);
        }
    }

    private int getStringIdentifier(Context pContext, String pString) {
        return pContext.getResources().getIdentifier(pString, "id", pContext.getPackageName());
    }

    private void playGame() {
        Intent intent = new Intent(deployMenu.this, playerTurn.class); //initialises the intent.
        finish(); // ends activity.
        startActivity(intent); // starts intent.
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder2 = new AlertDialog.Builder(deployMenu.this); // creates alert dialog.
        builder2.setMessage("Do you want to return to the Main Menu?");
        builder2.setCancelable(true);
        builder2.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                Intent intent = new Intent(deployMenu.this, menu.class); // initialises the intent.
                finish(); // end activity.
                startActivity(intent); // start activity.
            }
        });
        builder2.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();


            }
        });
        AlertDialog alert = builder2.create();
        alert.show();
    }

    private class BoatGenerator extends AsyncTask <Void, Integer, Void> {

        @Override
        protected Void doInBackground(Void... params) {

            Boat b;
            Point p;

            // TODO make code for auto generate boats,
            Log.d("APP", "Entered auto generate boats thread");
            for (int i = 0; i < 7; i++) {
                b = game.autoGenerateBoat(i);
                ArrayList<Point> points = b.getPoints();

                for (int j = 0; j < points.size(); j++) {
                    p = points.get(j);
                    String stringId = p.getCoordinate();
                    int identifier = getStringIdentifier(deployMenu.this, stringId);
                    publishProgress(identifier);

                }
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
            button.setBackgroundResource(R.drawable.fire);
        }
    }
}

