package com.codets.hearthattack;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;
import java.util.logging.Logger;

public class Server extends AsyncTask<Integer, String, Integer>{

    /*private static Socket s;
    private static ServerSocket ss;
    private static PrintWriter pw;
    private static BufferedReader br;
    private static InputStreamReader isr;
    private static String message = "";*/


    private int id;
    private String TAG = "AsyncTaskSocketServer";

    private Server() {
        super();
        Random generator = new Random();
        id = generator.nextInt();
        Log.d(TAG, "created with id: " + id);
    }

    @Override
    protected Integer doInBackground(Integer... ports) {

        int port = ports[0];
        Log.v(TAG, "Trying to start on port: " + port + " with id: " + id);

        try {
            ServerSocket serverSocket = new ServerSocket(port);
            Log.d(TAG, "" + serverSocket.getInetAddress());

            while (!isCancelled()) {
                Socket client = serverSocket.accept();
                try {
                    Log.v(TAG, "Listening on port: "
                            + port);
                    BufferedReader in = new BufferedReader(
                            new InputStreamReader(client.getInputStream()));
                    String str = in.readLine();
                    publishProgress(str);

                } catch (Exception e) {
                    e.printStackTrace();
                    Log.v(TAG, "Exception while socket.accept"+ id);
                } finally {
                    client.close();
                }
                client.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.v(TAG, "Exception in SocketServer creation" + id);
        }
        return port;
    }

    @Override
    protected void onProgressUpdate(String... values) {
        super.onProgressUpdate(values);
        String message = values[0];
        try {
            NetworkQueue.MESSAGE_IN_QUEUE.put(message);
            Log.v(TAG, "received: " + message);
        } catch (Exception e) {
            Logger.log("AsyncTaskSocketServer: Exception while writing to IN_QUEUE");
        }
    }
/*
    public Server() {
        start();
    }

    private void start() {
        try {
            ss = new ServerSocket(5000);
            System.out.println(NetworkInterface.getNetworkInterfaces());
            System.out.println("Server running on port 5000.");
            s = ss.accept();
            isr = new InputStreamReader(s.getInputStream());
            br = new BufferedReader(isr);
            message = br.readLine();
            System.out.println(message);
            isr.close();
            br.close();
            ss.close();
            s.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

*/
}
