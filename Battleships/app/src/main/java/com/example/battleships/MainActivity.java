package com.example.battleships;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.net.wifi.WpsInfo;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collection;

public class MainActivity extends AppCompatActivity  {

    private WifiP2pManager mManager;
    private WifiP2pManager.Channel mChannel;
    private BroadcastReceiver mReceiver;
    private final IntentFilter intentFilter = new IntentFilter();
    private ArrayAdapter<String> adapter;
    private ArrayList<WifiP2pDevice> deviceList;


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

    public void setWifiText(int indicator) {
        TextView v = (TextView)findViewById(R.id.wifi_indicator);

        if (indicator == 1) { // wifi is on
            v.setText("Wifi is turned on");
        }
        else {
            v.setText("Wifi is turned off");
        }
    }

    public void displayPeers(ArrayList<WifiP2pDevice> peers) {
        /*TextView v = (TextView)findViewById(R.id.peer_name);
        v.setText(device.deviceName);*/
        deviceList = peers;
        adapter.clear();
        for (int i = 0; i < peers.size(); i++) {
            adapter.add(peers.get(i).deviceName);
        }
        adapter.notifyDataSetChanged();
    }

    public void peerFound() {
        Toast.makeText(this, "Peer found", Toast.LENGTH_SHORT).show();
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
}
