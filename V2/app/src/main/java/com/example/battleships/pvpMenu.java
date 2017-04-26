package com.example.battleships;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.UUID;

public class pvpMenu extends AppCompatActivity {

    private Button returnToMenu, sLobby, jLobby;
    private TextView displayStatus;
    private BluetoothAdapter mBluetoothAdapter;
    /*private HandleConnection bluetoothService;*/

    private ArrayList<String> deviceNames;
    private ArrayList<String> deviceAddress;
    private ArrayAdapter<String> listAdapter;

    public static final UUID MY_UUID = UUID.fromString("02fdabb6-3100-43a9-96d6-05c46fd1b7c7");
    public static final int BLUETOOTH_SWITCH = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pvp_menu);

        init();
        startBluetooth(); // turn on bluetooth
    }

    // Initializes the required components
    private void init() {

        displayStatus   = (TextView)findViewById(R.id.status);
        returnToMenu    = (Button)findViewById(R.id.returnButton);
        sLobby          = (Button)findViewById(R.id.start_lobby);
        jLobby          = (Button)findViewById(R.id.join_lobby);

        deviceNames     = new ArrayList<String>();
        deviceAddress   = new ArrayList<String>();

        returnToMenu.setOnClickListener(new ButtonListener());
        sLobby.setOnClickListener(new ButtonListener());
        jLobby.setOnClickListener(new ButtonListener());

        ListView lView = (ListView)findViewById(R.id.bluetooth_listview);
        listAdapter = new ArrayAdapter<String>(this, R.layout.device, R.id.deviceView, deviceNames);
        lView.setAdapter(listAdapter);
        lView.setOnItemClickListener(new ListListener());

        // gets adapter to help handle bluetooth operations.
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        // initializes the intent filter which will listen for available devices to connect to.
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(mReceiver, filter); // register the BroadcastReceiver
    }

    /*
    Checks if device is compatible for bluetooth operations.
    If so, program will automatically turn on bluetooth
     */
    private void startBluetooth() {

        // if device does not support bluetooth
        if (mBluetoothAdapter == null) {
            displayStatus.setText("Device does not support bluetooth");
        } else {
            // if bluetooth is not enabled
            if (!mBluetoothAdapter.isEnabled()) {
                // display popup getting user input
                Intent enableBluetoothIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBluetoothIntent, BLUETOOTH_SWITCH);
            } else {
                displayStatus.setText("Bluetooth ON");
            }
        }
    }

    /*
    Getting the result of popup of bluetooth request to turn on.
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 5) {
            if (resultCode == 600) {
                // Initiate the game activity and establish connection with client
                Intent startGameIntent = new Intent(pvpMenu.this, PVP.class);
                startGameIntent.putExtra("ROLE", 0); // 0 as server
                startActivity(startGameIntent);
                pvpMenu.this.finish();
            }
        }
        if (resultCode == RESULT_OK) { // if user accepts to turn on bluetooth
            displayStatus.setText("Bluetooth ON");
        } else {
            displayStatus.setText("Bluetooth cannot initialize");
        }

    }

    /*
    Listens to intents when trying to join another device
     */
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Log.d("Discovery", "Found a new device");
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {

                /*
                Discovery has found a device. Get the BluetoothDevice
                object and its info from the Intent. Store those
                values into its corresponding arraylist.
                */
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                String deviceName = device.getName();
                String deviceHardwareAddress = device.getAddress(); // MAC address

                updateList(deviceName, deviceHardwareAddress);
            }
        }
    };

    private void updateList(String deviceName, String hAddress) {

        deviceNames.add(deviceName); // add the name of the device to the list
        deviceAddress.add(hAddress); // add the address of the device to the deviceAddress list
        if (deviceNames.size() != 0 && deviceAddress.size() != 0)
        {
            listAdapter.notifyDataSetChanged(); // update the listadapter for changes.
        }

    }

    @Override
    public void onBackPressed() {
        mBluetoothAdapter.disable(); // turn off bluetooth
        Intent intent = new Intent(pvpMenu.this, menu.class); // initialises intent.
        finish(); // ends activity.
        startActivity(intent); // starts intent.
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mReceiver);
    }

    private void deviceDiscoverable() {
        Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        // make device discoverable for 10 minutes
        discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 600);
        startActivityForResult(discoverableIntent, 5);
    }

    /*
    Class which handles the listeners of each button on the activity
     */
    private class ButtonListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {

            switch(v.getId()) {

                case R.id.returnButton:
                    mBluetoothAdapter.disable(); // turn off bluetooth
                    Intent intent = new Intent(pvpMenu.this, menu.class); // initialises intent.
                    finish(); // ends activity.
                    startActivity(intent); // starts intent.
                    break;

                case R.id.start_lobby:
                    // Make device discoverable
                    deviceDiscoverable();



                    break;


                /*
                If a user wants to join a lobby, it is him/her that
                attempts to discover other devices (devices that pressed
                the start lobby button).
                 */
                case R.id.join_lobby:
                    mBluetoothAdapter.startDiscovery(); // start the discovery process
                    break;
            }
        }
    }

    /*
    Class handles the selection of an item in the listview.
    This occurs when user wants to connect to an available device.
     */
    private class ListListener implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            // stop looking for devices
            mBluetoothAdapter.cancelDiscovery();
            // get address of device
            String address = String.valueOf(deviceAddress.get(position));

            Intent startGameIntent = new Intent(pvpMenu.this, PVP.class);
            startGameIntent.putExtra("ROLE", 1); // 1 as client
            startGameIntent.putExtra("ADDRESS", address); // send address to next activity
            startActivity(startGameIntent);
            pvpMenu.this.finish();
        }
    }
}
