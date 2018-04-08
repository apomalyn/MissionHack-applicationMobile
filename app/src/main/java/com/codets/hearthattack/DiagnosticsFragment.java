package com.codets.hearthattack;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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

    private View view;
    private ListView listView = null;

    private OnFragmentInteractionListener mListener;

    public DiagnosticsFragment() {
        currentSymptom = new ArrayList<>();
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment DiagnosticsFragment.
     */
    public static DiagnosticsFragment newInstance() {
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
        listView = (ListView)view.findViewById(R.id.list_symptoms);

        displayListView();

        final FloatingActionButton fab = view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                        WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                fab.setEnabled(false);
                view.findViewById(R.id.loading_panel).setVisibility(View.VISIBLE);

//                JSONObject response = HttpClient.sendRequest(HttpClient.DIAGNOSTICS_REQUEST, )

                //DELETE AFTER SERVER IN PLACE
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
                        getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                        fab.setEnabled(true);
                        view.findViewById(R.id.loading_panel).setVisibility(View.GONE);
                        CharSequence[] diagnostics = {"Grippe", "Cancer", "Tu as vu le film Alien ?"};
                        DialogFragment dialog = DiseasesDialogsFragment.newInstance(diagnostics);
                        dialog.show(getActivity().getSupportFragmentManager(), "DiagnosticsDialog");
                    }
                }, 5000);   //5 seconds
            }
        });

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            Toast.makeText(context, "Diagnostics Fragment attached", Toast.LENGTH_SHORT).show();
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
                JSONObject obj = JSON.AssetJSONFile(getContext(), SYMPTOM_LIST_FILENAME);
                JSONObject m_jArry = obj.getJSONObject("symptoms");
                String key;

                Iterator<?> keys = m_jArry.keys();

                while(keys.hasNext()){
                    key = (String)keys.next();
                    symptomsList.add(new Symptom(Integer.parseInt(key), m_jArry.getString(key)));
                }

                Collections.sort(symptomsList, new Comparator<Symptom>() {
                    @Override
                    public int compare(Symptom text1, Symptom text2)
                    {
                        return text1.getName().compareToIgnoreCase(text2.getName());
                    }
                });

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

        try{
             //listView = (ListView)view.findViewById(R.id.list_symptoms);
            listView.setAdapter(symptomsAdapter);
        }catch (Exception e){
            e.printStackTrace();
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
        void onFragmentInteraction(Uri uri);
    }
}
