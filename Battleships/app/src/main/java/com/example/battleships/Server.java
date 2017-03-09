package com.example.battleships;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by johnr on 24/02/2017.
 */

public class Server extends AsyncTask {

    private MainActivity activity;
    private String m;

    public Server(MainActivity a) {
        activity = a;
    }

    @Override
    protected Object doInBackground(Object[] params) {
        try {
            Log.d("NETWORK", "Thread started");
            ServerSocket serverSocket = new ServerSocket(8600);
            Log.d("NETWORK", "Server socket opened");
            Socket client = serverSocket.accept();
            Log.d("NETWORK", "Client accepted. Connection done");

            BufferedReader br = new BufferedReader(new InputStreamReader(client.getInputStream()));
            m = br.readLine();
            Log.d("NETWORK", "Message received");

            br.close();
            client.close();
            serverSocket.close();
        } catch (IOException e) {

        }



        return null;
    }

    @Override
    protected void onPostExecute(Object o) {
        activity.setText(m);
    }
}
