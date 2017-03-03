package com.example.battleships;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.net.wifi.WpsInfo;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pInfo;
import android.net.wifi.p2p.WifiP2pManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collection;

public class MainActivity extends AppCompatActivity implements WifiP2pManager.ConnectionInfoListener {

    private WifiP2pManager mManager;
    private WifiP2pManager.Channel mChannel;
    private BroadcastReceiver mReceiver;
    private final IntentFilter intentFilter = new IntentFilter();
    private ArrayAdapter<String> adapter;
    private ArrayList<WifiP2pDevice> deviceList;
    private WifiP2pInfo i;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        adapter = new ArrayAdapter<String>(this, R.layout.devices, R.id.deviceView);
        for (int i = 0; i < 10; i++) {
            adapter.add(String.valueOf(i));
        }
        ListView list = (ListView)findViewById(R.id.peer_list);
        list.setAdapter(adapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d("Path", "onItemClick() entered");
                connect(position);
            }
        });

        Button b = (Button)findViewById(R.id.button);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startPeerDiscovery();
            }
        });

        Button send = (Button)findViewById(R.id.send_message);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText m = (EditText)findViewById(R.id.Message);
                // If the group owner pressed the send button
                if (i.groupFormed && i.isGroupOwner) {
                    EditText e = (EditText)findViewById(R.id.Message);
                    Log.d("NETWORK", "Server switched on");
                    new Server(MainActivity.this).execute(true, String.valueOf(e.getText()));
                } else {
                    Log.d("NETWORK", "Send button pressed");

                    Log.d("NETWORK", "Edit Text reference taken/Client starting");
                    Client client = new Client(MainActivity.this,  i);
                    Log.d("NETWORK", "Sending should start now xD");
                    client.execute(true, String.valueOf(m.getText()));
                }

                /*TextView messageView = (TextView)findViewById(R.id.message_display);*/
                /*Server server = new Server(MainActivity.this, messageView);*/
                Toast.makeText(MainActivity.this, "Sent", Toast.LENGTH_SHORT).show();
                m.setText("");
            }
        });

        // Button to turn on the server when the server wishes to receive a message from the client
        Button start = (Button)findViewById(R.id.start_server);
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "Server Turned ON", Toast.LENGTH_SHORT).show();
                if (i.groupFormed && i.isGroupOwner) {
                    TextView go_indicator = (TextView)findViewById(R.id.go_indicator);
                    go_indicator.setText("You are group owner/server");
                    new Server(MainActivity.this).execute(false, "");
                    Log.d("NETWORK", "Server has turned on");

                } else {
                    TextView go_indicator = (TextView)findViewById(R.id.go_indicator);
                    go_indicator.setText("You are the client");
                }
            }
        });

        // Button is used to connect to the server when the server wants to send a message to the client
        Button c = (Button)findViewById(R.id.connect_to_server);
        c.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("NETWORK", "Connect to server button pressed");
                if (i.groupFormed && !i.isGroupOwner) {
                    Log.d("NETWORK", "Run thread (Client)");
                    new Client(MainActivity.this, i).execute(false, "");
                }
            }
        });

        intentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);

        mManager = (WifiP2pManager) getSystemService(Context.WIFI_P2P_SERVICE);
        mChannel = mManager.initialize(this, getMainLooper(), null);
    }

    @Override
    protected void onResume() {
        /* register the broadcast receiver with the intent values to be matched */
        super.onResume();
        mReceiver = new WiFiDirectBroadcastReceiver(mManager, mChannel, this);
        registerReceiver(mReceiver, intentFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(mReceiver);
    }

    private void startPeerDiscovery() {
        mManager.discoverPeers(mChannel, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {
                Toast.makeText(MainActivity.this, "Started peer discovery", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(int reason) {

            }
        });
    }

    public void displayPeers(ArrayList<WifiP2pDevice> peers) {
        deviceList = peers;
        adapter.clear();
        for (int i = 0; i < peers.size(); i++) {
            adapter.add(peers.get(i).deviceName);
        }
        adapter.notifyDataSetChanged();
    }

    public void displayConnectionFailure() {
        Toast.makeText(this, "Connect failed xD. Retry.",
                Toast.LENGTH_SHORT).show();
    }

    public void displayConnectionSuccess() {
        Toast.makeText(this, "Connect success!. Retry.",
                Toast.LENGTH_SHORT).show();
    }

    private void connect(int position) {
        WifiP2pDevice device = deviceList.get(position);
        WifiP2pConfig config = new WifiP2pConfig();
        config.deviceAddress = device.deviceAddress;
        // TODO figure out how to switch group owners.
        Log.d("Identification", "Owner intent value: " + config.groupOwnerIntent);
        config.wps.setup = WpsInfo.PBC;
        Log.d("Path", "connect() called");
        mManager.connect(mChannel, config, new WifiP2pManager.ActionListener() {

            @Override
            public void onSuccess() {
                // WiFiDirectBroadcastReceiver will notify us. Ignore for now.
                Toast.makeText(MainActivity.this, "Connection Successful", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onFailure(int reason) {
                Toast.makeText(MainActivity.this, "Connect failed. Retry.",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void setWifiText(boolean indicator) {
        TextView view = (TextView)findViewById(R.id.message_display);

        if (indicator) {
            view.setText("Wifi is on");
        } else {
            view.setText("Wifi is off");
        }
    }

    public void setText(String m) {
        TextView v = (TextView)findViewById(R.id.message_display);
        v.setText(m);
    }

    @Override
    public void onConnectionInfoAvailable(WifiP2pInfo info) {

        this.i = info;
        Log.d("NETWORK", "Connection INFO available" + i.groupOwnerAddress.getHostAddress());
        if (i.groupFormed && i.isGroupOwner) {
            TextView go_indicator = (TextView)findViewById(R.id.go_indicator);
            go_indicator.setText("You are group owner/server");
        }
        /*if (i.groupFormed && i.isGroupOwner) {
            TextView go_indicator = (TextView)findViewById(R.id.go_indicator);
            go_indicator.setText("You are group owner/server");
            new Server(MainActivity.this).execute();
            Log.d("NETWORK", "Server has turned on");
        }*/
    }
}
