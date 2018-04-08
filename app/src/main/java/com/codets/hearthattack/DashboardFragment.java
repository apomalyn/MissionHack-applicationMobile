package com.codets.hearthattack;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.Viewport;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.Random;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DashboardFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link DashboardFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DashboardFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private View view;

    private static final Random RANDOM = new Random();
    private LineGraphSeries<DataPoint> series;

    private final Handler mHandler = new Handler();
    private Runnable mTimer1;

    public DashboardFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DashboardFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DashboardFragment newInstance(String param1, String param2) {
        DashboardFragment fragment = new DashboardFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        this.view = inflater.inflate(R.layout.fragment_dashboard, container, false);
        this.showLoader();
        GraphView graph = (GraphView) view.findViewById(R.id.graph);

        series = new LineGraphSeries<>(generateData());
        graph.addSeries(series);
        graph.getGridLabelRenderer().setGridStyle(GridLabelRenderer.GridStyle.NONE);// It will remove the background grids

        graph.getGridLabelRenderer().setHorizontalLabelsVisible(false);// remove horizontal x labels and line

        return this.view;
    }

    /*// TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }*/

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            Toast.makeText(context, "Dashboard Fragment attached", Toast.LENGTH_SHORT);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public void showLoader() {

        this.view.findViewById(R.id.bluetoothLoading).setVisibility(View.VISIBLE);
        this.view.findViewById(R.id.connectionStatusBox).setVisibility(View.INVISIBLE);
        this.view.findViewById(R.id.connectionStatusBox).setClickable(false);
        ((TextView) this.view.findViewById(R.id.connectionStatusText)).setText("Chip Disconnected");

    }

    public void hideLoader() {
        this.view.findViewById(R.id.bluetoothLoading).setVisibility(View.INVISIBLE);
        this.view.findViewById(R.id.connectionStatusBox).setVisibility(View.VISIBLE);
        ((TextView) this.view.findViewById(R.id.connectionStatusText)).setText("Chip Connected");


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

    @Override
    public void onResume() {
        super.onResume();

        mTimer1 = new Runnable() {
            @Override
            public void run() {
                series.resetData(generateData());
                mHandler.postDelayed(this, 1500);
            }
        };
        mHandler.postDelayed(mTimer1, 1500);

    }

    private DataPoint[] generateData() {
/*
        DataCollector dc = DataCollector.getInstance();
        try {
            JSONArray pulses = dc.getPulses();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        y = */
        int count =7;
        DataPoint[] values = new DataPoint[count];
        Random r = new Random();

        for (int i=0; i<count; i++) {
            double x = i;
            int Low = 0;
            int High = 200;
            int Result = r.nextInt(High-Low) + Low;
            double y = Result;
            DataPoint v = new DataPoint(x, y);
            values[i] = v;
        }
        return values;
    }
}
