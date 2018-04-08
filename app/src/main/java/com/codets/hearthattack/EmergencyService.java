package com.codets.hearthattack;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.location.Location;
import android.location.LocationListener;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

public class EmergencyService implements Runnable, LocationListener {

    private final static String TAG = "Emergency Service";
    private final static String CHANNEL_ID = "Emergency channel";
    private final static long DELAY = 5000;

    private static String token = null;

    private boolean isRunning = false;
    private JSONObject requestParameters;
    private Location location = null;
    private Context context;

    public EmergencyService(int role, Context context){
        this.context = context;
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

        setupChannel();
    }

    private void setupChannel(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Create the NotificationChannel, but only on API 26+ because
            // the NotificationChannel class is new and not in the support library
            CharSequence name = context.getString(R.string.channel_name);
            String description = context.getString(R.string.channel_description);
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, NotificationManager.IMPORTANCE_HIGH);
            channel.setDescription(description);
            // Register the channel with the system
            NotificationManager notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(channel);
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
                JSONObject response = HttpClient.sendRequest(HttpClient.REGISTER_REQUEST, requestParameters);

                if(response != null && location != null && response.length() > 0){
                    Log.d(TAG, "EMERGENCY REQUEST RECEIVE !");
                    try{
                        Location locationDistress = new Location("");
                        String locationString = response.getString("location");
                        String[] locationStringExplode = locationString.split(",");
                        locationDistress.setLatitude(Double.parseDouble(locationStringExplode[0]));
                        locationDistress.setLongitude(Double.parseDouble(locationStringExplode[1]));

                        int distance = Math.round(this.location.distanceTo(locationDistress));

                        Uri gmmIntentUri = Uri.parse("geo:" + locationString);
                        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                        mapIntent.setPackage("com.google.android.apps.maps");
                        mapIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, mapIntent, 0);

                        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, CHANNEL_ID)
                                .setSmallIcon(R.drawable.ic_report_black_24dp)
                                .setContentTitle(context.getText(R.string.distress_notification_title))
                                .setContentText(context.getString(R.string.distress_notification_content, distance))
                                .setPriority(NotificationCompat.PRIORITY_MAX)
                                // Set the intent that will fire when the user taps the notification
                                .setContentIntent(pendingIntent)
                                .setAutoCancel(true);

                        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
                        notificationManager.notify(0, mBuilder.build());

                    }catch (JSONException e){
                        Log.e(TAG, "JSON error", e);
                    }

                }

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
