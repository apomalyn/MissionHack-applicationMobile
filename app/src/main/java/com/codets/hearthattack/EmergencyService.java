package com.codets.hearthattack;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

public class EmergencyService implements Runnable {

    private final static String TAG = "Emergency Service";
    private final static long DELAY = 5000;

    private boolean isRunning = false;
    private JSONObject requestParameters;

    public void start(){
        isRunning = true;
        requestParameters = new JSONObject();
        try{
            requestParameters.put("identifier", "CECIestUNtest");
            requestParameters.put("role", "DOCTOR");
        }catch (JSONException e){
            Log.e(TAG, "", e);
        }

        new Thread(this).start();
    }

    @Override
    public void run() {
        while(isRunning){
            try{
                Thread.sleep(DELAY);
                Log.d(TAG, "EMERGENCY CHECK SEND");
                HttpClient.sendRequest(HttpClient.REGISTER_REQUEST, requestParameters);
            }catch (InterruptedException e){
                Log.e(TAG, "", e);
            }
        }
    }
}
