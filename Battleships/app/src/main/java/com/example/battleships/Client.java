package com.example.battleships;

import android.content.ContentResolver;
import android.content.Context;
import android.net.wifi.p2p.WifiP2pInfo;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * Created by johnr on 24/02/2017.
 */

public class Client extends AsyncTask<Object, Object, String>{

    private String messageToSend;
    private MainActivity activity;
    private WifiP2pInfo ownerInfo;

    public Client(MainActivity context, WifiP2pInfo info) {
        this.activity = context;
        ownerInfo = info;
    }

    @Override
    protected String doInBackground(Object[] params) {
        String messageToSend = "";
        String messageReceived = "";
        boolean send = (boolean)params[0];
        if (params[1] != null)
            messageToSend = (String)params[1];
        Socket socket = new Socket();

        try {
            Log.d("PATH", "Opening client socket -");
            socket.bind(null);

            socket.connect(new InetSocketAddress(ownerInfo.groupOwnerAddress.getHostAddress(), 8600), 10000);
            Log.d("NETWORK", "Connected with server");

            if (send == true) {
                PrintWriter pw = new PrintWriter(socket.getOutputStream(), true);


                pw.write(messageToSend);
                pw.flush();
                Log.d("NETWORK", "Message sent");
                pw.close();
            } else {
                Log.d("NETWORK", "Take in message");
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                messageReceived = in.readLine();
                Log.d("NETWORK", "Message: " + messageReceived);
                Log.d("NETWORK", "Message received");
                in.close();
            }


            socket.close();

        } catch (IOException exception) {
            Log.d("EXCEPTION", exception.toString());
        }
        return messageReceived;
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
