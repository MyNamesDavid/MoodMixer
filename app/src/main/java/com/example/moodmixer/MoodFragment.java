package com.example.moodmixer;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;


public class MoodFragment extends Fragment {

    String SelectedServerOne;
    String SelectedServerTwo;
    MainActivity newActivity;

    public String [] currentMood = {"Angry","Nervous","Sad","Stressed"};
    public String [] desiredMood = {"Calm","Happy","Optimistic"};
    private Moods mood;
    private String firstMood;

    public MoodFragment() {
        // Required empty public constructor
        this.mood = new Moods();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_mood, null);

        View view = inflater.inflate(R.layout.fragment_mood, null);
        TextView tv = (TextView) view.findViewById(R.id.firstMood);
        tv.setText("hello");
        ArrayAdapter<String> arrayAdapterOne = new ArrayAdapter<String>
                (getContext(), android.R.layout.simple_dropdown_item_1line, currentMood);
        MaterialBetterSpinner betterSpinnerOne = (MaterialBetterSpinner) rootView.findViewById(R.id.current_mood);
        betterSpinnerOne.setAdapter(arrayAdapterOne);

        ArrayAdapter<String> arrayAdapterTwo = new ArrayAdapter<String>
                (getContext(), android.R.layout.simple_dropdown_item_1line, desiredMood);
        MaterialBetterSpinner betterSpinnerTwo = (MaterialBetterSpinner) rootView.findViewById(R.id.desired_mood);
        betterSpinnerTwo.setAdapter(arrayAdapterTwo);
        betterSpinnerOne.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                SelectedServerOne = adapterView.getItemAtPosition(i).toString();
                mood.putMoodToCollection(Mood.valueOf(SelectedServerOne));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        betterSpinnerTwo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                SelectedServerTwo = adapterView.getItemAtPosition(i).toString();
                mood.setDesiredMood(Mood.valueOf(SelectedServerTwo));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        return rootView;
    }

    public void playSong() {
        newActivity.
    }


}
