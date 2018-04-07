package com.codets.hearthattack;


import android.content.res.AssetManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;


public class SimpleWebServer extends AppCompatActivity {

    private static Socket s;
    private static ServerSocket ss;
    private static PrintWriter pw;

    String message = "Est-ce que ça fonctionne ???";
    private static String ip = "172.20.8.138";



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void send_text(){
        myTask mt = new myTask();
        mt.execute();

    }

    class myTask extends AsyncTask<Void, Void, Void>{


        @Override
        protected Void doInBackground(Void... voids) {
            try {
                s = new Socket(ip, 5000);
                pw = new PrintWriter(s.getOutputStream());
                pw.write(message);
                pw.flush();
                pw.close();
                s.close();

            }catch (IOException e){
                e.printStackTrace();
            }

            return null;
        }
    }
}