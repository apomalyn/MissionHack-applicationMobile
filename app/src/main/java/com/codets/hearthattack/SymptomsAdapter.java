package com.codets.hearthattack;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class SymptomsAdapter extends ArrayAdapter<Symptom> {

    private ArrayList<Symptom> symptomList;

    private Context context;

    public SymptomsAdapter(Context context, int textViewResourceId,
                           ArrayList<Symptom> symptomList) {
        super(context, textViewResourceId, symptomList);
        this.context = context;
        this.symptomList = new ArrayList<Symptom>();
        this.symptomList.addAll(symptomList);
    }

    private class ViewHolder {
        CheckBox name;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder = null;
        Log.v("ConvertView", String.valueOf(position));

        if (convertView == null) {
            LayoutInflater vi = (LayoutInflater) context.getSystemService(
                    Context.LAYOUT_INFLATER_SERVICE);
            convertView = vi.inflate(R.layout.symptom_layout, null);

            holder = new ViewHolder();
            holder.name = (CheckBox) convertView.findViewById(R.id.checkbox);
            convertView.setTag(holder);

            holder.name.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    CheckBox cb = (CheckBox) v;
                    Symptom symptom = (Symptom) cb.getTag();
                    Toast.makeText(context,
                            "Clicked on Checkbox: " + cb.getText() +
                                    " is " + cb.isChecked(),
                            Toast.LENGTH_LONG).show();
                    symptom.setSelected(cb.isChecked());
                }
            });
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Symptom symptom = symptomList.get(position);
        holder.name.setText(symptom.getName());
        holder.name.setChecked(symptom.isSelected());
        holder.name.setTag(symptom);

        return convertView;

    }


    private void checkButtonClick() {
        Toast.makeText(context, "here", Toast.LENGTH_LONG);
    }
}
