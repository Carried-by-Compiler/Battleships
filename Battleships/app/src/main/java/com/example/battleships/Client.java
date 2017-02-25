package com.example.battleships;

import android.content.ContentResolver;
import android.content.Context;
import android.net.wifi.p2p.WifiP2pInfo;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * Created by johnr on 24/02/2017.
 */

public class Client extends AsyncTask{

    private String messageToSend;
    private Context context;
    private WifiP2pInfo ownerInfo;

    public Client(Context context, String message, WifiP2pInfo info) {
        messageToSend = message;
        this.context = context;
        ownerInfo = info;
    }

    @Override
    protected Object doInBackground(Object[] params) {
        Socket socket = new Socket();

        try {
            Log.d("PATH", "Opening client socket -");
            socket.bind(null);

            socket.connect(new InetSocketAddress(ownerInfo.groupOwnerAddress.getHostAddress(), 8600), 10000);
            Log.d("NETWORK", "Connected with server");

            PrintWriter pw = new PrintWriter(socket.getOutputStream(), true);
            Log.d("NETWORK", "Message sent");

            pw.write(messageToSend);
            pw.flush();

            pw.close();
            socket.close();

        } catch (IOException exception) {
            Log.d("EXCEPTION", exception.toString());
        }
        return null;
    }
}
