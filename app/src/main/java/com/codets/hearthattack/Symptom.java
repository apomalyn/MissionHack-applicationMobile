package com.codets.hearthattack;

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
}
