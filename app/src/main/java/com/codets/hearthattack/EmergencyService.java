package com.codets.hearthattack;

import android.location.Location;
import android.location.LocationListener;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

public class EmergencyService implements Runnable, LocationListener {

    private final static String TAG = "Emergency Service";
    private final static long DELAY = 5000;

    private static String token = null;

    private boolean isRunning = false;
    private JSONObject requestParameters;
    private Location location;

    public EmergencyService(int role){
        requestParameters = new JSONObject();
        try{
            if(token == null)
                token = getToken();
            requestParameters.put("identifier", token);
            if(role == 0){ //Doctor
                requestParameters.put("role", "DOCTOR");
            }
        }catch (JSONException e){
            Log.e(TAG, "", e);
        }
    }

    private String byteArrayToHexString(String input) {
        byte[] b = input.getBytes();
        String result = "";
        for (int i=0; i < b.length; i++) {
            result +=
                    Integer.toString( ( b[i] & 0xff ) + 0x100, 16).substring( 1 );
        }
        return result;
    }

    private String getToken() {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        if (model.startsWith(manufacturer)) {
            return byteArrayToHexString(capitalize(model) + System.currentTimeMillis());
        } else {
            return byteArrayToHexString(capitalize(manufacturer) + " " + model + System.currentTimeMillis());
        }
    }


    private String capitalize(String s) {
        if (s == null || s.length() == 0) {
            return "";
        }
        char first = s.charAt(0);
        if (Character.isUpperCase(first)) {
            return s;
        } else {
            return Character.toUpperCase(first) + s.substring(1);
        }
    }


    public void start(){
        isRunning = true;

        new Thread(this).start();
    }

    @Override
    public void run() {
        while(isRunning){
            try{
                Thread.sleep(DELAY);
                Log.d(TAG, "EMERGENCY CHECK SEND");
                JSONObject response = HttpClient.sendRequest(HttpClient.REGISTER_REQUEST, requestParameters);
                //TODO Check response and display notif is necessary
            }catch (InterruptedException e){
                Log.e(TAG, "", e);
            }
        }
    }

    public void startEmergency(){
        HttpClient.sendRequest(HttpClient.EMERGENCY_REQUEST, requestParameters);
    }

    @Override
    public void onLocationChanged(Location location) {
        this.location = location;
        String latLong = location.getLatitude() + ", " + location.getLongitude();
        Log.d(TAG, "Location changed !");

        try{
            requestParameters.put("location", latLong);
        }catch (JSONException e){
            Log.e(TAG, "JSON Error", e);
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
