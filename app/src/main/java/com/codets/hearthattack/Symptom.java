package com.codets.hearthattack;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

public class Symptom {

    private int id;

    private String name;

    private boolean isSelected = false;

    public Symptom(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public JSONObject toJSONObject(){
        JSONObject json = new JSONObject();
        try{
            json.put("id", id);
        }catch (JSONException e){
            Log.e("JSON", "", e);
        }

        return json;
    }
}
