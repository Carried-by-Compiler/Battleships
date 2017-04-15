package com.example.battleships;


import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

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

    private ArrayList<Point> hitHistory;

    private TextView display, posField;
    private boolean enteredCoord;
    private String chosenPoint;

    public static Game game;
    private Connector connector;

    public static boolean READY = false;
    public static final int HIT = 2;
    public static final int MISS = 3;
    public static final int SUNKSHIP = 4;
    public static final int WIN = 5;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.player_turn);

        // initialize stuff
        hitHistory = new ArrayList<Point>();
        posField = (TextView)findViewById(R.id.posField);
        display = (TextView)findViewById(R.id.textView2);
        display.setText("Game will start once connection is established");

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
                        display.setText("Waiting for opponent to get ready");
                    else {
                        display.setText("Opponent is ready");
                        new ClearBoard().execute();
                    }

                    connector.sendMessage(READY);

                    Button b1 = (Button)findViewById(R.id.manual);
                    b1.setVisibility(View.INVISIBLE);
                    Button b2 = (Button)findViewById(R.id.automate);
                    b2.setVisibility(View.INVISIBLE);
                    TextView v1 = (TextView)findViewById(R.id.textView);
                    if (Connector.TURN)
                        v1.setText("Your guess: ");
                    else
                        v1.setText("Wait");
                }

                // checks if a coordinate has been selected.
                if(enteredCoord && Connector.TURN) {
                    boolean found = false;
                    newPoint = new Point(chosenPoint);
                    for (int i = 0; i < hitHistory.size() && !found; i++) {

                        p = hitHistory.get(i);
                        if (p.equals(newPoint))
                        {
                            found = true;
                            Toast.makeText(PVP.this, "You've chosen this coordinate before!", Toast.LENGTH_SHORT).show();
                        }

                    }
                    if (!found) {
                        Log.d("APP", "not found");
                        connector.sendCoordinate(newPoint);
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
                        display.setText("Opponent is ready");
                        new ClearBoard().execute();
                    }

                    // Taking the coordinate

                    // Hit flag

                    // sunk flag

                    // win flag

                    break;

                case Connector.CONNECTED:
                    display.setText("CONNECTED");
                    automateShipPlacement();
                    break;
            }
        }

    };

    private void automateShipPlacement() {

        TextView v1 = (TextView)findViewById(R.id.textView);
        Button b1 = (Button)findViewById(R.id.manual);
        Button b2 = (Button)findViewById(R.id.automate);
        v1.setText("Automate?"); // TODO make better
        b1.setVisibility(View.VISIBLE);
        b2.setVisibility(View.VISIBLE);

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(PVP.this, deployMenu.class); //initialises the intent.
                intent.putExtra("MULTIPLAYER", 1);
                startActivityForResult(intent, 5); // starts intent.
                display.setText("Press FIRE button to confirm");

            }
        });

        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new BoatGenerator().execute();
                display.setText("Press FIRE button to confirm");

            }
        });



        /*// TODO fix ship placement
        AlertDialog.Builder builder = new AlertDialog.Builder(PVP.this); // creates alert dialog.
        builder.setMessage("Do you want automated ship placement?");
        builder.setCancelable(false);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                *//*
                User decides to allow automated ship placement.
                Start thread to create boats.
                 *//*
                game = new Game(true);
                new BoatGenerator().execute();
                display.setText("Press FIRE button to confirm");

            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                *//*
                User decides he will manually place boats himself.
                Create listeners for the buttons and use game object to store the
                location of boats.
                 *//*
                game = new Game(true);
                Intent intent = new Intent(PVP.this, deployMenu.class); //initialises the intent.
                intent.putExtra("MULTIPLAYER", 1);
                startActivityForResult(intent, 5); // starts intent.
                display.setText("Press FIRE button to confirm");

            }
        });
        AlertDialog alert = builder.create();
        alert.show();*/


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

    private class ClearBoard extends AsyncTask <Void, Integer, Void> {

        @Override
        protected Void doInBackground(Void... params) {

            for (Boat b: game.getBoats()) {

                for (Point p: b.getPoints()) {

                    String stringId = p.getCoordinate();
                    int identifier = getStringIdentifier(PVP.this, stringId);
                    publishProgress(identifier);

                }

            }



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
