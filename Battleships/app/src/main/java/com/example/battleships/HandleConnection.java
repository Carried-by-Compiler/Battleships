package com.example.battleships;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by johnr on 26/03/2017.
 *
 * This class will connect and communicate using bluetooth.
 * (LOOK AT SAMPLE PROGRAM FOR INSPIRATION
 */

public class HandleConnection {

    private BluetoothAdapter mAdapter;
    private Handler mHandler;

    private AcceptThread acceptThread;
    private ConnectThread cThread;
    private ConnectedThread connectedThread;

    public HandleConnection(BluetoothAdapter a, Handler handler) {
        mAdapter = a;
        mHandler = handler;
    }

    public void connectToDevice(String deviceAddress) {
        BluetoothDevice device = mAdapter.getRemoteDevice(deviceAddress);
        cThread = new ConnectThread(device);
        cThread.start();
    }

    public void startServer(BluetoothAdapter bluetoothAdapter) {
        acceptThread = new AcceptThread(bluetoothAdapter);
        acceptThread.start();
    }

    public synchronized void connected(BluetoothSocket socket) {
        connectedThread = new ConnectedThread(socket);
        connectedThread.start();
    }

    public void sendMessage(byte[] message) {
        Log.d("MESSAGE", "Entered method in HandleConnection");
        ConnectedThread r;

        synchronized (this) {
            r =connectedThread;
        }

        r.write(message);
    }


    private class ConnectThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final BluetoothDevice mmDevice;

        public ConnectThread(BluetoothDevice device) {
            // Use a temporary object that is later assigned to mmSocket
            // because mmSocket is final.
            BluetoothSocket tmp = null;
            mmDevice = device;

            try {
                // Get a BluetoothSocket to connect with the given BluetoothDevice.
                // MY_UUID is the app's UUID string, also used in the server code.
                tmp = device.createRfcommSocketToServiceRecord(MainActivity.MY_UUID);
            } catch (IOException e) {
                Log.e("Thread", "Socket's create() method failed", e);
            }
            mmSocket = tmp;
        }

        public void run() {
            // Cancel discovery because it otherwise slows down the connection.
            mAdapter.cancelDiscovery();

            try {
                // Connect to the remote device through the socket. This call blocks
                // until it succeeds or throws an exception.
                Log.d("THREAD", "ConnectThread running");
                mmSocket.connect();
            } catch (IOException connectException) {
                // Unable to connect; close the socket and return.
                try {
                    mmSocket.close();
                } catch (IOException closeException) {
                    Log.e("Thread", "Could not close the client socket", closeException);
                }
                return;
            }

            // The connection attempt succeeded. Perform work associated with
            // the connection in a separate thread.
            connected(mmSocket);
        }

        // Closes the client socket and causes the thread to finish.
        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) {
                Log.e("Thread", "Could not close the client socket", e);
            }
        }
    }


    private class AcceptThread extends Thread {

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
            Log.d("THREAD", "AcceptThread running");
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
                    connected(socket);
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

    private class ConnectedThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;
        private byte[] mmBuffer; // mmBuffer store for the stream

        public ConnectedThread(BluetoothSocket socket) {
            mmSocket = socket;
            InputStream tmpIn = null;
            OutputStream tmpOut = null;

            // Get the input and output streams; using temp objects because
            // member streams are final.
            try {
                Log.d("THREAD", "ConnectedThread running");
                tmpIn = socket.getInputStream();
                Log.d("THREAD", "Socket input stream found");
            } catch (IOException e) {
                Log.e("THREAD", "Error occurred when creating input stream", e);
            }
            try {
                tmpOut = socket.getOutputStream();
            } catch (IOException e) {
                Log.e("THREAD", "Error occurred when creating output stream", e);
            }

            mmInStream = tmpIn;
            mmOutStream = tmpOut;
            Log.d("THREAD", "Streams established");
        }

        public void run() {
            Log.d("THREAD", "run() or ConnectedThread running");
            mmBuffer = new byte[1024];
            int numBytes; // bytes returned from read()

            // Keep listening to the InputStream until an exception occurs.
            while (true) {
                try {
                    Log.d("THREAD", "Trying to read from message");
                    // Read from the InputStream.
                    numBytes = mmInStream.read(mmBuffer);
                    // Send the obtained bytes to the UI activity.
                    Message readMsg = mHandler.obtainMessage(
                            0, numBytes, -1,
                            mmBuffer);
                    readMsg.sendToTarget();
                } catch (IOException e) {
                    Log.d("THREAD", "Input stream was disconnected", e);
                    break;
                }
            }
        }

        public void write(byte[] buffer) {
            Log.d("THREAD", "Trying to write to other phone lol xD");
            try {
                mmOutStream.write(buffer);

                // Share the sent message back to the UI Activity
                mHandler.obtainMessage(1, -1, -1, buffer)
                        .sendToTarget();
                Log.d("THREAD", "Should have the message sent by now xD");
            } catch (IOException e) {
                Log.e("THREAD", "Exception during write", e);
            }
        }

    }
}
