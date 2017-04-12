package com.example.battleships;


import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
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

    private Game game;
    private Connector connector;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.player_turn);

        hitHistory = new ArrayList<Point>();
        posField = (TextView)findViewById(R.id.posField);
        display = (TextView)findViewById(R.id.textView2);
        display.setText("Game will start once connection is established");

        setButtonOnClickListeners();

        // TODO get game logic implemented



        connector = new Connector(mHandler);

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
                // checks if a coordinate has been selected.
                if(enteredCoord) {
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

                }

            };
        };

        findViewById(R.id.fireButton).setOnClickListener(listener2); // links listener to the fireButton.

    }

    private final Handler mHandler = new Handler() {
        // TODO fix handler
        @Override
        public void handleMessage(Message msg) {
            /*super.handleMessage(msg);*/
            switch (msg.what) {

                case Connector.READ_MESSAGE:
                    byte[] readBuff = (byte[])msg.obj;
                    String newMessage = new String(readBuff);
                    TextView view = (TextView)findViewById(R.id.textView2);
                    view.setText("Received Coordinate: " + newMessage);
                    break;

                case Connector.CONNECTED:
                    display.setText("CONNECTED");
                    break;
            }
        }

    };


}
