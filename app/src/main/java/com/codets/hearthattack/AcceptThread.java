package com.codets.hearthattack;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

import java.io.IOException;
import java.util.UUID;

class AcceptThread extends Thread {

    private static final String TAG = "AcceptThread.java";
    private final BluetoothServerSocket mmServerSocket;

    private ChipListeningService referee;
    private BluetoothAdapter bluetooth;

    private final UUID SERVICE_UUID = UUID.fromString("c1c3b6c6-8ca2-4ef2-94e4-805e0f6cfe93");
    private final String SERVICE_NAME = "codets.heartattack";


    public AcceptThread( BluetoothAdapter appBluetooth, ChipListeningService appReferee ) {
        this.bluetooth = appBluetooth;
        this.referee = appReferee;
        // Use a temporary object that is later assigned to mmServerSocket
        // because mmServerSocket is final.
        BluetoothServerSocket tmp = null;
        try {
            // MY_UUID is the app's UUID string, also used by the client code.
            tmp = bluetooth.listenUsingRfcommWithServiceRecord(SERVICE_NAME, SERVICE_UUID);
        } catch (IOException e) {
            Log.e(TAG, "Socket's listen() method failed", e);
        }
        mmServerSocket = tmp;
    }

    public void run() {
        BluetoothSocket socket = null;
        // Keep listening until exception occurs or a socket is returned.
        System.out.println("Connection acceptance thread started");
        while (true) {
            try {
                socket = mmServerSocket.accept();
            } catch (IOException e) {
                Log.e(TAG, "Socket's accept() method failed", e);
                break;
            }

            if (socket != null) {
                // A connection was accepted. Perform work associated with
                // the connection in a separate thread.
                referee.handleConnectedSocket(socket);
                this.cancel();
                break;
            }
        }
    }

    // Closes the connect socket and causes the thread to finish.
    public void cancel() {
        try {
            mmServerSocket.close();
        } catch (IOException e) {
            Log.e(TAG, "Could not close the connect socket", e);
        }
    }
}