package com.example.battleships;


import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDialog;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Set;

/**
 * Created by johnr on 12/04/2017.
 */

public class PVP extends AppCompatActivity {

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

    private ArrayList<String> hitHistory;
    public static ArrayList<Point> hitPoints;

    private TextView tv1, tv2, posField, resultField;
    private Button viewYourBoat;
    private boolean enteredCoord;
    private String chosenPoint;
    private boolean buttonForBoatPlacementPressed;

    public static Game game;
    private Connector connector;



    public static boolean READY = false;
    public static final int HIT = 1;
    public static final int MISS = 2;
    public static final int SUNKSHIP = 3;
    public static final int DEFEATED = 4;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.player_turn);

        // initialize stuff
        viewYourBoat = (Button)findViewById(R.id.view_boats);
        viewYourBoat.setVisibility(View.INVISIBLE);
        viewYourBoat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Button to view your boats
                Intent intent = new Intent(PVP.this, ViewBoats.class);
                startActivity(intent);
            }
        });
        buttonForBoatPlacementPressed = false;
        hitHistory = new ArrayList<String>();
        hitPoints = new ArrayList<Point>();
        tv1 = (TextView)findViewById(R.id.textView1);
        tv2 = (TextView)findViewById(R.id.status_message);
        posField = (TextView)findViewById(R.id.posField);
        this.resultField = (TextView)findViewById(R.id.hit_marker);
        tv2.setText("Game will start once connection is established");

        setButtonOnClickListeners();

        connector = new Connector(mHandler); // initialize connector object to help communicate with other device
        game = new Game(true);

        int role = getIntent().getIntExtra("ROLE", 0);

        // if you are the server
        if (role == 0) {
            Log.d("PVP", "Entered PVP class as server");
            connector.startServer();
        } else if (role == 1) { // if you are the client
            Log.d("PVP", "Entered PVP class as client");
            String address = getIntent().getStringExtra("ADDRESS");

            connector.connectToDevice(address);
        }
    }

    @Override
    public void onBackPressed() {

        AlertDialog.Builder builder = new AlertDialog.Builder(PVP.this);
        builder.setMessage("Leave this game?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //do things
                        dialog.dismiss();
                        BluetoothAdapter bA = BluetoothAdapter.getDefaultAdapter();
                        bA.disable();
                        Intent intent = new Intent(PVP.this, menu.class);
                        startActivity(intent);
                        finish();
                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    // TODO Bug where connection is still remember even after leaving or finishing a game

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 5 && resultCode == RESULT_OK) {
            displayBoats();
        }
    }

    private void displayBoats() {
        for (Boat b: game.getBoats()) {
            for (Point p: b.getPoints()) {
                int identifier = getStringIdentifier(PVP.this, p.getCoordinate());
                Button button = (Button)findViewById(identifier);
                button.setBackgroundResource(R.drawable.fire);
            }
        }
    }

    private void displayHitBoat(String stringId) {
        int identifier = getStringIdentifier(PVP.this, stringId);
        Button button = (Button)findViewById(identifier);
        button.setBackgroundResource(R.drawable.flame);
    }

    /*
        Puts functionality to the buttons on the grid.
         */
    private void setButtonOnClickListeners() {

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
                // if fire button pressed to confirm user is ready
                if (!READY) {

                    // let opponent know that you are ready.
                    READY = true;
                    if (!Connector.IS_OPP_READY)
                        tv2.setText("Waiting for opponent to get ready");
                    else { // if BOTH you and the opponent are ready
                        tv2.setText("Opponent is ready");
                        new ClearBoard().execute(1);
                        if (Connector.TURN)
                            tv1.setText("Your guess: ");
                        else
                            tv1.setText("Wait");

                        tv2.setVisibility(View.INVISIBLE);
                        viewYourBoat.setVisibility(View.VISIBLE);
                    }

                    connector.sendMessage(READY, "NA", -1);

                    Button b1 = (Button)findViewById(R.id.manual);
                    b1.setVisibility(View.INVISIBLE);
                    Button b2 = (Button)findViewById(R.id.automate);
                    b2.setVisibility(View.INVISIBLE);

                }

                // checks if a coordinate has been selected.
                if(enteredCoord && Connector.TURN) {
                    boolean found = false;
                    newPoint = new Point(chosenPoint);
                    /*for (int i = 0; i < hitHistory.size() && !found; i++) {

                        p = hitHistory.get(i);
                        if (p.equals(newPoint))
                        {
                            found = true;
                            Toast.makeText(PVP.this, "You've chosen this coordinate before!", Toast.LENGTH_SHORT).show();
                        }

                    }*/
                    if (hitHistory.contains(newPoint.getCoordinate())) {
                        found = true;
                        Toast.makeText(PVP.this, "You've selected this point before!", Toast.LENGTH_SHORT).show();
                    }
                    if (!found) {
                        Log.d("APP", "not found");
                        hitHistory.add(newPoint.getCoordinate());
                        connector.sendMessage(false, chosenPoint, -1);
                        Connector.TURN = false;
                        tv1.setText("Wait");
                    }

                } else if (!Connector.TURN) {
                    Toast.makeText(PVP.this, "It is not your turn!", Toast.LENGTH_SHORT).show();
                }

            };
        };

        findViewById(R.id.fireButton).setOnClickListener(listener2); // links listener to the fireButton.

    }

    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            /*super.handleMessage(msg);*/
            switch (msg.what) {

                case Connector.READ_MESSAGE:
                    byte[] readBuff = (byte[])msg.obj;
                    String newMessage = new String(readBuff);

                    String segments[] = newMessage.split(",");

                    Log.d("MESSAGE", "Message: " + newMessage);

                    // if opponent is ready
                    if (Boolean.parseBoolean(segments[0])) {
                        Log.d("MESSAGE", "Entered ready checker");
                        Connector.IS_OPP_READY = true;
                        tv2.setText("Opponent is ready");
                        // if BOTH you and your opponent are ready
                        if (READY) {
                            new ClearBoard().execute(1);
                            if (Connector.TURN)
                                tv1.setText("Your guess: ");
                            else
                                tv1.setText("Wait");

                            tv2.setText("Result: ");
                            viewYourBoat.setVisibility(View.VISIBLE);
                        }

                    }

                    // segments containing the coordinate
                    if (!segments[1].equals("NA")) {
                        int currentState = 0;
                        // check if sent coordinate is successful
                        Point sentPoint = new Point(segments[1]);

                        // flags[0] = sunk, flags[1] = hit, flags[2] = defeated
                        boolean[] flags = game.checkIfBoatWasHit(sentPoint);
                        if (flags[2]) {
                            currentState = DEFEATED;
                            hitPoints.add(sentPoint);
                            Intent intent = new Intent(PVP.this, loseScreen.class);
                            intent.putExtra("TOTAL_SCORE", game.getScore());
                            startActivity(intent);
                            finish();
                        } else if (flags[0]) {
                            currentState = SUNKSHIP;
                            resultField.setText("SUNK AT: " + sentPoint.getCoordinate());
                            hitPoints.add(sentPoint);

                        } else if (flags[1]) {
                            currentState = HIT;
                            resultField.setText("HIT AT: " + sentPoint.getCoordinate());
                            hitPoints.add(sentPoint);

                        } else {
                            currentState = MISS;
                            resultField.setText("MISS AT: " + sentPoint.getCoordinate());
                        }


                        connector.sendMessage(false, "NA", currentState);
                        Connector.TURN = true;
                        tv1.setText("Your guess:");
                    }

                    // contains flags (hit/miss/sunk/win)
                    if (Integer.parseInt(segments[2]) != -1) {

                        int currentScore = 100;
                        switch (Integer.parseInt(segments[2])) {

                            case HIT:
                                resultField.setText("You've hit a ship at: " + chosenPoint);
                                displayHitBoat(chosenPoint);
                                currentScore = currentScore * game.getCounter();
                                game.setScore(currentScore);
                                game.incrementCounter();
                                break;

                            case MISS:
                                resultField.setText("You missed a ship at: " + chosenPoint);
                                game.resetCounter();
                                currentScore = currentScore * game.getCounter();
                                game.setScore(currentScore);
                                break;

                            case SUNKSHIP:
                                resultField.setText("You've sunk a ship at: " + chosenPoint);
                                displayHitBoat(chosenPoint);
                                currentScore = currentScore * game.getCounter();
                                game.setScore(currentScore);
                                game.incrementCounter();
                                break;

                            case DEFEATED:
                                Intent intent = new Intent(PVP.this, winScreen.class);
                                intent.putExtra("TOTAL_SCORE", game.getScore());
                                startActivity(intent);
                                finish();
                                break;
                        }
                    }

                    break;

                case Connector.CONNECTED:
                    tv2.setText("CONNECTED");
                    automateShipPlacement();
                    break;

                case Connector.DISCONNECTED:

                    //Device has disconnected
                    AlertDialog.Builder builder = new AlertDialog.Builder(PVP.this);
                    builder.setMessage("Opponent has disconnected!")
                            .setCancelable(false)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    //do things
                                    Intent intent = new Intent(PVP.this, winScreen.class);
                                    intent.putExtra("TOTAL_SCORE", game.getScore());
                                    startActivity(intent);
                                    finish();
                                }
                            });
                    AlertDialog alert = builder.create();
                    alert.show();

                    break;
            }
        }

    };

    /*
    Appears when connection has been established.
    Displays option whether you want to automate boat placement or manually place
    boats yourself.
     */
    private void automateShipPlacement() {

        Button b1 = (Button)findViewById(R.id.manual);
        Button b2 = (Button)findViewById(R.id.automate);
        tv1.setText("Boat Placement");
        b1.setVisibility(View.VISIBLE);
        b2.setVisibility(View.VISIBLE);

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!buttonForBoatPlacementPressed) {
                    buttonForBoatPlacementPressed = true;
                    Intent intent = new Intent(PVP.this, deployMenu.class); //initialises the intent.
                    intent.putExtra("MULTIPLAYER", 1);
                    startActivityForResult(intent, 5); // starts intent.
                    tv2.setText("Press FIRE button to confirm");
                }

            }
        });

        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!buttonForBoatPlacementPressed) {
                    buttonForBoatPlacementPressed = true;
                    new BoatGenerator().execute();
                    tv2.setText("Press FIRE button to confirm");
                } else {
                    new ClearBoard().execute(0);
                    // These threads are occurring at the same time and can influence each other
                    new BoatGenerator().execute();
                }
            }
        });
    }

    private int getStringIdentifier(Context pContext, String pString) {
        return pContext.getResources().getIdentifier(pString, "id", pContext.getPackageName());
    }


    private class BoatGenerator extends AsyncTask<Void, Integer, Void> {

        @Override
        protected Void doInBackground(Void... params) {

            Boat b;
            Point p;

            Log.d("APP", "Entered auto generate boats thread");
            // Go through each boat providing the boat number to method
            for (int i = 0; i < 7; i++) {
                b = game.autoGenerateBoat(i); // create a boat object
                ArrayList<Point> points = b.getPoints(); // take the points from the boat object

                // go through each point placing the images on the grid corresponding to the point
                for (int j = 0; j < points.size(); j++) {
                    p = points.get(j);
                    String stringId = p.getCoordinate();
                    int identifier = getStringIdentifier(PVP.this, stringId);
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

    private class ClearBoard extends AsyncTask <Integer, Integer, Void> {

        @Override
        protected Void doInBackground(Integer... params) {

            int flag = params[0].intValue();

            for (Boat b: game.getBoats()) {

                for (Point p: b.getPoints()) {

                    String stringId = p.getCoordinate();
                    int identifier = getStringIdentifier(PVP.this, stringId);
                    publishProgress(identifier);

                }

            }

            if (flag == 0)
                game.removeBoats();

            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            Button button = (Button)findViewById(values[0]);
            button.setBackgroundResource(R.drawable.outline);
        }
    }


}
