package com.codets.hearthattack;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.List;

public class JSON {

    private final static String TAG = "JSON";

    public static JSONObject AssetJSONFile (Context context, String filename) throws IOException, JSONException {
        AssetManager manager = context.getAssets();
        InputStream file = manager.open(filename);
        byte[] formArray = new byte[file.available()];
        file.read(formArray);
        file.close();

        return new JSONObject(new String(formArray));
    }

    public static JSONObject readInputStream(InputStream inputStream) throws IOException, JSONException{
        byte[] inputConvert = new byte[inputStream.available()];
        inputStream.read(inputConvert);
        inputStream.close();

        return new JSONObject(new String(inputConvert));
    }

    public static JSONObject convertArrayList(List<Symptom> symptoms){
        JSONObject json = new JSONObject();

        try{
            //TODO add identifier
            int[] ids = new int[symptoms.size()];

            for (int i = 0; i < symptoms.size(); i++) {
                ids[i] = symptoms.get(i).getId();
            }

            json.put("symptoms_id", ids);
        }catch (JSONException e){
            Log.e(TAG, "", e);
        }

        return json;
    }

    public static CharSequence[] convertToCharSequenceWithoutKeys(JSONObject json){
        CharSequence[] response = new CharSequence[json.length()];
        Iterator<?> keys = json.keys();

        try{
            for (int i = 0; i < json.length(); i++) {
                response[i] = json.getString(keys.next().toString());
            }
        }catch (JSONException e){
            Log.e(TAG, "", e);
        }

        return response;
    }
}
