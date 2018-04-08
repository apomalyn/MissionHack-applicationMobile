package com.codets.hearthattack;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

public class DataCollector {

    private final String PULSES_FILE_NAME = "pulses.json";
    private final String DIAGNOSTICS_FILE_NAME = "diagnostics.json";

    private final String TAG = "DataCollector.java";
    private Context fsContext;

    private static DataCollector instance = null;

    private DataCollector(Context context) {
        File pulses = new File(context.getFilesDir(), PULSES_FILE_NAME);
        File diagnostics = new File(context.getFilesDir(), DIAGNOSTICS_FILE_NAME);

        this.fsContext = context;


        // TODO: remove comments and seeding function
        //if(!pulses.exists())
            this.resetPulses();

        //if(!diagnostics.exists())
            this.resetDiagnostics();

            this.seed();

    }

    public static DataCollector getInstance(Context context){
        if(instance == null){
            instance = new DataCollector(context);
        }
        return instance;
    }

    public static DataCollector getInstance(){
        return instance;
    }

    public void savePulse(JSONObject newPulse) throws IOException, JSONException {
        JSONArray newPulsesArray = getPulses().put(newPulse);
        String newJsonString = "{\"pulses\":"+ newPulsesArray.toString() +"}";
        writeToFile(this.PULSES_FILE_NAME, newJsonString);
    }

    public void saveDiagnostic(JSONObject newDiagnostic) throws IOException, JSONException {
        JSONArray newDiagnosticsArray = getDiagnostics().put(newDiagnostic);
        String newJsonString = "{\"diagnostics\":"+ newDiagnosticsArray.toString() +"}";
        writeToFile(this.DIAGNOSTICS_FILE_NAME, newJsonString);
    }

    public JSONArray getPulses() throws JSONException {
        String jsonString = "Couldn't parse to json";
        try {
            jsonString = this.readFile(this.PULSES_FILE_NAME);
        } catch(IOException err) {
            Log.e(TAG, err.toString());
        }

        return new JSONObject(jsonString).getJSONArray("pulses");
    }

    public JSONArray getDiagnostics() throws JSONException {
        String jsonString = "Couldn't parse to json";
        try {
            jsonString = this.readFile(this.DIAGNOSTICS_FILE_NAME);
        } catch(IOException err) {
            Log.e(TAG, err.toString());
        }

        return new JSONObject(jsonString).getJSONArray("diagnostics");
    }



    private String readFile(String fileName) throws IOException {
        FileInputStream readingStream = this.fsContext.openFileInput(fileName);
        InputStreamReader streamReader = new InputStreamReader(readingStream);
        BufferedReader r = new BufferedReader(streamReader);
        StringBuilder output = new StringBuilder();
        String line;
        while ((line = r.readLine()) != null) {
            output.append(line).append('\n');
        }
        return output.toString();
    }

    private void writeToFile(String fileName, String data) throws IOException {
        OutputStream writingStream = this.fsContext.openFileOutput(fileName, Context.MODE_PRIVATE);
        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(writingStream);
        outputStreamWriter.write(data);
        outputStreamWriter.close();

    }

    private void resetPulses() {
        try {
            writeToFile(PULSES_FILE_NAME, "{\"pulses\": []}");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void resetDiagnostics() {
        try {
            writeToFile(DIAGNOSTICS_FILE_NAME, "{\"diagnostics\": []}");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void seed() {

        String pulses = "{'pulses': [" +
                "{'heartbeat': '150', 'systolic': '80', 'diastelic': '110'}," +
                "{'heartbeat': '145', 'systolic': '79', 'diastelic': '108'}," +
                "{'heartbeat': '148', 'systolic': '80', 'diastelic': '107'}," +
                "{'heartbeat': '146', 'systolic': '80', 'diastelic': '108'}," +
                "{'heartbeat': '149', 'systolic': '81', 'diastelic': '107'}" +
                "]}";

        String diag = "{'diagnostics': [" +
                "{'symptoms': [14,65,24,32], 'diag': 24, 'timestamp':'1523153786407', 'correct': 'true'}," +
                "{'symptoms': [25,65], 'diag': 11, 'timestamp':'1523153845561', 'correct': 'false'}," +
                "{'symptoms': [14,2,8,65,31,32], 'diag': 12, 'timestamp':'1523153847882', 'correct': 'false'}," +
                "{'symptoms': [14,7,54,67,21,13,33], 'diag': 8, 'timestamp':'1523153853635', 'correct': 'true'}," +
                "]}";

        try {
            this.writeToFile(this.PULSES_FILE_NAME, pulses);
            this.writeToFile(this.DIAGNOSTICS_FILE_NAME, diag);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
