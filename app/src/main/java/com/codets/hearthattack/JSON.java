package com.codets.hearthattack;

import android.content.Context;
import android.content.res.AssetManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;

public class JSON {

    public static JSONObject AssetJSONFile (Context context, String filename) throws IOException, JSONException {
        AssetManager manager = context.getAssets();
        InputStream file = manager.open(filename);
        byte[] formArray = new byte[file.available()];
        file.read(formArray);
        file.close();

        return new JSONObject(new String(formArray));
    }

    public static void addInJSONFileAsset(Context context, String filename, JSONObject json){

    }


}
