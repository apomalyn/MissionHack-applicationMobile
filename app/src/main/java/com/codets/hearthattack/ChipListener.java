package com.codets.hearthattack;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.app.ActivityCompat;

import java.io.IOException;
import java.util.Set;
import java.util.UUID;


public class ChipListener {

  BluetoothAdapter bluetooth = BluetoothAdapter.getDefaultAdapter();
  private Activity appActivity;

  int REQUEST_ENABLE_BT = 9001;

    // Create a BroadcastReceiver for ACTION_FOUND.
    private final BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            System.out.println("received broadcast");
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                // Discovery has found a device. Get the BluetoothDevice
                // object and its info from the Intent.
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                String deviceName = device.getName();
                String deviceHardwareAddress = device.getAddress(); // MAC address
                System.out.println("Device found !"+ deviceName + deviceHardwareAddress);
            }
        }
    };


    ChipListener(Activity app) {
      this.appActivity = app;

        int MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION = 1;
        ActivityCompat.requestPermissions(appActivity,
                new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION);

        bluetooth.startDiscovery();

        try {
            initConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

  public void initConnection() throws IOException {
      // enable bluetooth
      if (!bluetooth.isEnabled()) {
          Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
          appActivity.startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
      }

      //initDiscovery();

      /*// get previously paired devices
      Set<BluetoothDevice> pairedDevices = bluetooth.getBondedDevices();

      if (pairedDevices.size() > 0) {
          for (BluetoothDevice device : pairedDevices) {
              String deviceName = device.getName();
              String deviceHardwareAddress = device.getAddress(); // MAC address
          }
      }*/

      // launch thread looking for a socket
      AcceptThread connectionThread = new AcceptThread(bluetooth, this);
      connectionThread.start();
  }

  public void handleConnectedSocket(BluetoothSocket socket) {
        System.out.println("CONNECTED SOCKET");
  }

  private void initDiscovery() {
      System.out.println("starting bluetooth discovery");

      // Register for broadcasts when a device is discovered.
      IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);

      appActivity.registerReceiver(new BroadcastReceiver() {
          public void onReceive(Context context, Intent intent) {
              System.out.println("received broadcast");
              String action = intent.getAction();
              if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                  // Discovery has found a device. Get the BluetoothDevice
                  // object and its info from the Intent.
                  BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                  String deviceName = device.getName();
                  String deviceHardwareAddress = device.getAddress(); // MAC address
                  System.out.println("Device found !"+ deviceName + deviceHardwareAddress);
              }
          }
      }, filter);

      bluetooth.startDiscovery();

      //while(!bluetooth.isDiscovering())
        //System.out.println("nope");


  }

  private void cancelDiscovery() {
        bluetooth.cancelDiscovery();
        appActivity.unregisterReceiver(broadcastReceiver);
  }

  private void initDeviceListeneing() {}

  private void handleTransmission() {}

  private void cleanUp() {
        appActivity.unregisterReceiver(broadcastReceiver);

  }



}
