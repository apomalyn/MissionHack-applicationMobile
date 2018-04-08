package com.codets.hearthattack;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutionException;

public class HttpClient {
    private final static String TAG = "HTTP_CLIENT";
    //private final static String CONFIGURATION_FILE = "app.json";

    private final static String URL = "http://159.89.112.179:5000";

    public final static String REGISTER_REQUEST = "/register";
    public final static String DIAGNOSTICS_REQUEST = "";

    public final static String EMERGENCY_REQUEST = "";

    public static JSONObject sendRequest(String method, JSONObject parameters) {
        try{
            return new PostClass(URL + method, parameters).execute().get();
        }catch (InterruptedException e){
            Log.e(TAG, "", e);
        }catch (ExecutionException e){
            Log.e(TAG, "", e);
        }

        return null;
    }

    private static class PostClass extends AsyncTask<Void, Void, JSONObject> {
        String url;
        JSONObject parameters;

        public PostClass(String url, JSONObject parameters){
            this.url = url;
            this.parameters = parameters;
        }

        @Override
        protected JSONObject doInBackground(Void... params) {

            try {
                URL url = new URL(this.url);
                HttpURLConnection connection = (HttpURLConnection)url.openConnection();

                connection.setRequestMethod("POST");
                connection.setRequestProperty("USER-AGENT", "Mozilla/5.0");
                connection.setRequestProperty("ACCEPT-LANGUAGE", "en-US,en;0.5");
                connection.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
                connection.setDoOutput(true);
                DataOutputStream dStream = new DataOutputStream(connection.getOutputStream());
                dStream.writeBytes(this.parameters.toString());

                dStream.flush();
                dStream.close();

                return JSON.readInputStream(connection.getInputStream());
            } catch (JSONException e) {
                Log.e(TAG, "JSON error in the response", e);
            }catch (IOException e){
                Log.e(TAG, "", e);
            }

            return new JSONObject();
        }

        @Override
        protected void onPostExecute(JSONObject s) {
            super.onPostExecute(s);
        }
    }
}
