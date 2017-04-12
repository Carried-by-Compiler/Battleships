package com.example.battleships;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
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
import java.util.UUID;

import static android.R.attr.filter;

public class MainActivity extends AppCompatActivity  {
    private TextView v;
    private Button on;
    private BluetoothAdapter mBluetoothAdapter;
    private ArrayList<String> deviceNames = new ArrayList<String>();
    private ArrayList<String> deviceAddress = new ArrayList<String>();
    private ArrayAdapter<String> listAdapter;
    private HandleConnection bluetoothService;

    public static final UUID MY_UUID = UUID.fromString("02fdabb6-3100-43a9-96d6-05c46fd1b7c7");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();






    }

    private void init() {

        v = (TextView)findViewById(R.id.received_message);
        on = (Button)findViewById(R.id.bluetooth_on);
        ListView lView = (ListView)findViewById(R.id.available_device);
        listAdapter = new ArrayAdapter<String>(this, R.layout.devices, R.id.deviceView, deviceNames);
        lView.setAdapter(listAdapter);
        lView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String address = String.valueOf(deviceAddress.get(position));
                Toast.makeText(MainActivity.this, "Device Address: " + address, Toast.LENGTH_SHORT).show();
                Log.d("BLUETOOTH", "Trying to connect to device: " + address);
                bluetoothService.connectToDevice(address);
            }
        });
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        bluetoothService = new HandleConnection(mBluetoothAdapter, mHandler);

        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(mReceiver, filter);

    }

    // Handles message taken from the bluetooth threads
    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            /*super.handleMessage(msg);*/
            switch (msg.what) {
                case 0:
                    byte[] readBuff = (byte[])msg.obj;
                    String newMessage = new String(readBuff);
                    TextView view = (TextView)findViewById(R.id.received_message);
                    view.setText(newMessage);
                    break;


            }
        }
    };

    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {

        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Log.d("Discovery", "Found a new device");
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                // Discovery has found a device. Get the BluetoothDevice
                // object and its info from the Intent.
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                String deviceName = device.getName();
                String deviceHardwareAddress = device.getAddress(); // MAC address
                /*listAdapter.insert(deviceName, listAdapter.getCount());
                listAdapter.notifyDataSetChanged();*/
                updateList(deviceName, deviceHardwareAddress);
                v.setText(deviceName);
            }
        }

    };

    private void updateList(String deviceName, String hAddress) {
        Log.d("APP", "Update List");
        deviceNames.add(deviceName);
        deviceAddress.add(hAddress);
        Log.d("APP", "Added details to lists");
        if (deviceNames.size() != 0 && deviceAddress.size() != 0)
        {
            listAdapter.notifyDataSetChanged();
            Log.d("APP", "Updated list");
        }

    }

    public void startBluetooth(View view) {
        Log.d("APP", "Button pressed for bluetooth initiation");


        if (mBluetoothAdapter == null) {
            v.setText("Device does not support bluetooth");
        } else {
            if (!mBluetoothAdapter.isEnabled()) {
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, 1);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            v.setText("Bluetooth will turn on");
        } else {
            v.setText("Bluetooth will not turn on");
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mReceiver);
    }

    public void startDiscovery(View view) {
        mBluetoothAdapter.startDiscovery();
    }

    public void becomeServer(View view) {
        bluetoothService.startServer(mBluetoothAdapter);
    }

    public void sendMessage(View view) {
        Log.d("MESSAGE", "Send Button Pressed");
        EditText text = (EditText)findViewById(R.id.editText);
        byte[] send = String.valueOf(text.getText()).getBytes();
        bluetoothService.sendMessage(send);
    }
}
