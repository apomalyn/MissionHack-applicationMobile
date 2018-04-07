package com.codets.hearthattack;

import android.content.Context;
import android.content.res.AssetManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DiagnosticsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link DiagnosticsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DiagnosticsFragment extends Fragment {
    private static final String SYMPTOM_LIST_FILENAME = "symptoms.json";

    private static ArrayList<Symptom> symptomsList = null;
    private static SymptomsAdapter symptomsAdapter = null;
    private static ArrayList<Symptom> currentSymptom;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private View view;


    private OnFragmentInteractionListener mListener;

    public DiagnosticsFragment() {
        currentSymptom = new ArrayList<>();
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DiagnosticsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DiagnosticsFragment newInstance(String param1, String param2) {
        DiagnosticsFragment fragment = new DiagnosticsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        this.view = inflater.inflate(R.layout.fragment_diagnostics, container, false);
        displayListView();
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            Toast.makeText(context, "Diagnostics Fragment attached", Toast.LENGTH_SHORT);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    private void displayListView() {
        //Array list of countries
        if(symptomsList == null){
            try {
                symptomsList = new ArrayList<>();
                JSONObject obj = new JSONObject(AssetJSONFile(getContext()));
                JSONObject m_jArry = obj.getJSONObject("symptoms");
                String key;
                JSONObject jo_inside;

                Iterator<?> keys = m_jArry.keys();

                while(keys.hasNext()){
                    key = (String)keys.next();
                    symptomsList.add(new Symptom(Integer.parseInt(key), m_jArry.getString(key)));
                }
            } catch (JSONException e) {
                e.printStackTrace();
                return;
            } catch (IOException e){
                e.printStackTrace();
                return;
            }
        }

        if(symptomsAdapter == null){
            symptomsAdapter = new SymptomsAdapter(getContext(),
                    R.layout.symptom_layout, symptomsList);
        }

        //create an ArrayAdaptar from the String Array
        ListView listView;

        try{
            listView = (ListView)view.findViewById(R.id.list_symptoms);
            listView.setAdapter(symptomsAdapter);
        }catch (NullPointerException e){
            e.printStackTrace();
            return;
        }
        // Assign adapter to ListView


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // When clicked, show a toast with the TextView text
                Symptom symptom= (Symptom) parent.getItemAtPosition(position);
                currentSymptom.add(symptom);
            }
        });

    }

    public static String AssetJSONFile (Context context) throws IOException {
        AssetManager manager = context.getAssets();
        InputStream file = manager.open(DiagnosticsFragment.SYMPTOM_LIST_FILENAME);
        byte[] formArray = new byte[file.available()];
        file.read(formArray);
        file.close();

        return new String(formArray);
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
