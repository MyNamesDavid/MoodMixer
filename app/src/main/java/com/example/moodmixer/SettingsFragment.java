package com.example.moodmixer;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import static com.android.volley.VolleyLog.TAG;


public class SettingsFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    private SettingsFragment.onFragmentInteractionSetting mListener;
    Spinner spinner;
    ArrayAdapter<CharSequence> adapter;
    String SelectedServer;
    String [] genre = {"Alternative","Blues","Classical","Dance",
            "Country","Gospel","Jazz","K-Pop","Pop","Rap","R&B","Rock"};

    public interface onFragmentInteractionSetting {
        void onFragmentInteraction(Uri uri);

        void onFragmentInteractionSetting(Uri uri);
    }

    public SettingsFragment(){

    }

    public static SettingsFragment newInstance(String param1, String param2) {
        SettingsFragment fragment = new SettingsFragment();
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

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        Log.d(TAG,"onCreate - start");

        View rootView = inflater.inflate(R.layout.fragment_settings, null);

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>
                (getContext(),android.R.layout.simple_dropdown_item_1line,genre);
        MaterialBetterSpinner betterSpinnerOne = (MaterialBetterSpinner)rootView.findViewById(R.id.genre_select_one);
        MaterialBetterSpinner betterSpinnerTwo = (MaterialBetterSpinner)rootView.findViewById(R.id.genre_select_two);
        MaterialBetterSpinner betterSpinnerThree = (MaterialBetterSpinner)rootView.findViewById(R.id.genre_select_three);
        MaterialBetterSpinner betterSpinnerFour = (MaterialBetterSpinner)rootView.findViewById(R.id.genre_select_four);
        betterSpinnerOne.setAdapter(arrayAdapter);
        betterSpinnerTwo.setAdapter(arrayAdapter);
        betterSpinnerThree.setAdapter(arrayAdapter);
        betterSpinnerFour.setAdapter(arrayAdapter);
        betterSpinnerOne.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                SelectedServer = adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        return rootView;
    }


    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof SettingsFragment.onFragmentInteractionSetting) {
            mListener = (SettingsFragment.onFragmentInteractionSetting) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public void saveGenreSettings(View view) {
        File txtFolder = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/MyFolder/");
        if (!txtFolder.exists()) {
            txtFolder.mkdir();
        }
        File file = new File(txtFolder, "setting.txt");
        String.valueOf(SelectedServer.getBytes());
        try {
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(SelectedServer.getBytes());
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void loadSavedGenreSettings(){

    }

    public interface onFragmentInteractionListener {
        void onFragmentInteractionSetting(Uri uri);
    }

}
