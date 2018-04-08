package com.codets.hearthattack;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.ref.WeakReference;

import static java.util.Arrays.copyOfRange;


public class ChipListeningService extends Handler {

  private BluetoothAdapter bluetooth = BluetoothAdapter.getDefaultAdapter();
  private AppCompatActivity appActivity;
  private AcceptThread connectionThread;
  private ManageConnectionThread connectedThread;

  private DashboardFragment dashboardFragment;

  int REQUEST_ENABLE_BT = 9001;
  int MAXIMUM_HEARTBEAT_ALLOWED = 200;

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

    ChipListeningService(AppCompatActivity app) {
      this.appActivity = app;

      System.out.println("ChipListeningService");

        int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
        ActivityCompat.requestPermissions(this.appActivity,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);

        //bluetooth.startDiscovery();
        tryConnection();

    }

    public void tryConnection() {
        System.out.println("trying to connect");
        try {
            initConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void handleMessage(Message msg) {

        for (Fragment fragment: this.appActivity.getSupportFragmentManager().getFragments()) {
            if (fragment instanceof DashboardFragment) {
                ((DashboardFragment) fragment).hideLoader();
            }
        }

        // make json
        byte[] bytes = (byte[]) msg.obj;
        String data = new String(Base64.decode(copyOfRange(bytes, 0, msg.arg1), 0));
        try {
            JSONObject json = new JSONObject(data);
            System.out.println(json.toString());

            // check if the json has valid parameters
            json.getInt("heartbeat");
            json.getInt("systolic");
            json.getInt("diastolic");

            // check if the heartbeat is worth ringing a bell
            //if (json.getInt("heartbeat") > this.MAXIMUM_HEARTBEAT_ALLOWED )

            try {
                DataCollector.getInstance(this.appActivity.getApplicationContext())
                        .savePulse(json);

            } catch (IOException e) {
                System.out.println("Received input from chip but could'nt write to fs");
                e.printStackTrace();
            }


        } catch (JSONException e) {
            System.out.println("Received input from chip but could'nt parse data to json");
            e.printStackTrace();
        }


    }

  protected void initConnection() throws IOException {
      // enable bluetooth
      if (!bluetooth.isEnabled()) {
          Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
          appActivity.startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
      }

      // launch thread looking for a socket
      connectionThread = new AcceptThread(bluetooth, this);
        for (Fragment fragment: this.appActivity.getSupportFragmentManager().getFragments()) {
            if (fragment instanceof DashboardFragment) {
                ((DashboardFragment) fragment).showLoader();
            }
        }

      System.out.println("starting connection thread ? ");
      connectionThread.start(); // thread will contact us and kill itself, no need to worry
  }

  protected void handleConnectedSocket(BluetoothSocket socket) {
        System.out.println("CONNECTED SOCKET");
        connectionThread.interrupt();

      Looper.prepare();

        // handler to handle other thread messages
        //IncomingHandler incomingHandler = new IncomingHandler(this);
        connectedThread = new ManageConnectionThread(socket, this);
        connectedThread.start();

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



  private void cleanUp() {
        appActivity.unregisterReceiver(broadcastReceiver);

  }



}
