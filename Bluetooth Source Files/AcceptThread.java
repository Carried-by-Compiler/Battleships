package com.example.battleships;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

import java.io.IOException;
import java.util.UUID;

/**
 * Created by johnr on 26/03/2017.
 */

public class AcceptThread extends Thread {

    private final BluetoothServerSocket mmServerSocket;

    public AcceptThread(BluetoothAdapter mBluetoothAdapter) {
        // Use a temporary object that is later assigned to mmServerSocket
        // because mmServerSocket is final.
        BluetoothServerSocket tmp = null;
        try {
            // MY_UUID is the app's UUID string, also used by the client code.
            tmp = mBluetoothAdapter.listenUsingRfcommWithServiceRecord("Battleships", MainActivity.MY_UUID);
        } catch (IOException e) {
            Log.e("THREAD", "Socket's listen() method failed", e);
        }
        mmServerSocket = tmp;
    }

    public void run() {
        BluetoothSocket socket = null;
        // Keep listening until exception occurs or a socket is returned.
        while (true) {
            try {
                socket = mmServerSocket.accept();
            } catch (IOException e) {
                Log.e("THREAD", "Socket's accept() method failed", e);
                break;
            }
            // if connection is accepted
            if (socket != null) {
                // A connection was accepted. Perform work associated with
                // the connection in a separate thread.
                try {
                    mmServerSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            }
        }
    }

    // Closes the connect socket and causes the thread to finish.
    public void cancel() {
        try {
            mmServerSocket.close();
        } catch (IOException e) {
            Log.e("THREAD", "Could not close the connect socket", e);
        }
    }
}
