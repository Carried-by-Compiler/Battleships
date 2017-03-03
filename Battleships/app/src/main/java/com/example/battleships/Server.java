package com.example.battleships;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by johnr on 24/02/2017.
 */

public class Server extends AsyncTask<Object, Object, String> {

    private MainActivity activity;
    private String m;

    public Server(MainActivity a) {
        activity = a;
    }

    @Override
    protected String doInBackground(Object[] params) {
        String message = "";
        String messageToSend = "";
        boolean send = (boolean)params[0];
        if (params[1] != null) {
            messageToSend = (String)params[1];
        }
        try {
            Log.d("NETWORK", "Thread started");
            ServerSocket serverSocket = new ServerSocket(8600);
            Log.d("NETWORK", "Server socket opened");
            Socket client = serverSocket.accept();
            Log.d("NETWORK", "Client accepted. Connection done");

            if (send == true) {
                DataOutputStream stream = new DataOutputStream(client.getOutputStream());
                // TODO have this send out whatever is in the EditText
                stream.writeUTF(messageToSend);
                Log.d("NETWORK", "Server sent message to client!");

                stream.flush();
                stream.close();

            }
            else {
                // TODO figure out how to receive messages from the server
                Log.d("NETWORK", "Receive file");
                BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
                message = in.readLine();
                Log.d("NETWORK", "Message received");
                in.close();
            }

            client.close();
            serverSocket.close();
        } catch (IOException e) {

        }

        return message;
    }

    @Override
    protected void onProgressUpdate(Object... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(String s) {
        if (!s.isEmpty()) {
            activity.setText(s);
        }
    }
}
