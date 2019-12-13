package com.example.moodmixer;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;


public class MoodFragment extends Fragment {

    String currentMoodServer;
    String desiredMoodServer;
    String SelectedGenreHappyServer;
    String SelectedGenreCalmServer;
    String SelectedGenreOptimisticServer;
    String SelectedGenreAngryServer;
    String SelectedGenreStressedServer;
    String SelectedGenreSadServer;
    MainActivity newActivity;
    PreferenceManager preferences;

    public String [] currentMood = {"Angry","Calm","Happy","Sad"};
    public String [] desiredMood = {"Calm","Happy"};
    public String [] genre = {"Alternative","Blues","Classical","Dance",
            "Country","Gospel","Jazz","K-Pop","Pop","Rap","R&B","Rock"};
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


        // Spinner for current and Desired mood
        ArrayAdapter<String> arrayAdapterCurrentMood = new ArrayAdapter<String>
                (getContext(), android.R.layout.simple_dropdown_item_1line, currentMood);
        MaterialBetterSpinner currentMoodSpinner = (MaterialBetterSpinner) rootView.findViewById(R.id.current_mood);
        currentMoodSpinner.setAdapter(arrayAdapterCurrentMood);

        ArrayAdapter<String> arrayAdapterDesiredMood = new ArrayAdapter<String>
                (getContext(), android.R.layout.simple_dropdown_item_1line, desiredMood);
        MaterialBetterSpinner desiredMoodSpinner = (MaterialBetterSpinner) rootView.findViewById(R.id.desired_mood);
        desiredMoodSpinner.setAdapter(arrayAdapterDesiredMood);



        // Spinner for Genre mood selection
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>
                (getContext(),android.R.layout.simple_dropdown_item_1line,genre);
        MaterialBetterSpinner happyGenreSpinner = (MaterialBetterSpinner)rootView.findViewById(R.id.happy_genre);
        MaterialBetterSpinner calmGenreSpinner = (MaterialBetterSpinner)rootView.findViewById(R.id.calm_genre);
        //MaterialBetterSpinner optimisticGenreSpinner = (MaterialBetterSpinner)rootView.findViewById(R.id.optimistic_genre);
        MaterialBetterSpinner angryGenreSpinner = (MaterialBetterSpinner)rootView.findViewById(R.id.angry_genre);
        MaterialBetterSpinner sadGenreSpinner = (MaterialBetterSpinner)rootView.findViewById(R.id.sad_genre);
        happyGenreSpinner.setAdapter(arrayAdapter);
        calmGenreSpinner.setAdapter(arrayAdapter);
        //optimisticGenreSpinner.setAdapter(arrayAdapter);
        angryGenreSpinner.setAdapter(arrayAdapter);
        sadGenreSpinner.setAdapter(arrayAdapter);


        // Saves Preference to currentMood data
        currentMoodSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                currentMoodServer = adapterView.getItemAtPosition(i).toString();
                mood.putMoodToCollection(Mood.valueOf(currentMoodServer));
                preferences.setCurrentMood(currentMoodServer);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        // Saves Preference to desiredMood data
        desiredMoodSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                desiredMoodServer = adapterView.getItemAtPosition(i).toString();
                mood.setDesiredMood(Mood.valueOf(desiredMoodServer));
                preferences.setDesiredMood(desiredMoodServer);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        // saves preferences to genre moods
        happyGenreSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                SelectedGenreHappyServer = adapterView.getItemAtPosition(i).toString();
                preferences.setHappyGenre(SelectedGenreHappyServer);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        calmGenreSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                SelectedGenreCalmServer = adapterView.getItemAtPosition(i).toString();
                preferences.setCalmGenre(SelectedGenreCalmServer);

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
/*
        optimisticGenreSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                SelectedGenreOptimisticServer = adapterView.getItemAtPosition(i).toString();
                preferences.setOptimisticGenre(SelectedGenreOptimisticServer);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
*/
        angryGenreSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                SelectedGenreAngryServer = adapterView.getItemAtPosition(i).toString();
                preferences.setOptimisticGenre(SelectedGenreAngryServer);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        sadGenreSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                SelectedGenreSadServer = adapterView.getItemAtPosition(i).toString();
                preferences.setSadGenre(SelectedGenreSadServer);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        return rootView;
    }




}
