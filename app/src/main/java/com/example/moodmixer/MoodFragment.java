package com.example.moodmixer;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


public class MoodFragment extends Fragment {


    private Button angryDesiredButton;
    private Button happyDesiredButton;
    private Button sadDesiredButton;
    private Button stressedDesiredButton;
    private Button calmDesiredButton;
    private Button angryCurrentButton;
    private Button happyCurrentButton;
    private Button sadCurrentButton;
    private Button stressedCurrentButton;
    private Button calmCurrentButton;
    private Moods mood;

    public MoodFragment() {
        // Required empty public constructor
        this.mood = new Moods();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_mood, null);

        setUpHappyMoodCurrentButton(rootView);
        setUpSadMoodCurrentButton(rootView);
        setUpAngryMoodCurrentButton(rootView);
        setUpStressedCurrentButton(rootView);
        setUpCalmCurrentButton(rootView);
        setUpHappyMoodDesiredButton(rootView);
        setUpSadMoodDesiredButton(rootView);
        setUpStressedDesiredButton(rootView);
        setUpAngryDesiredButton(rootView);
        setUpCalmDesiredButton(rootView);
        return rootView;
    }

    private void setUpAngryDesiredButton(View rootView) {
        angryDesiredButton = rootView.findViewById(R.id.angry_mood_desired);
        angryDesiredButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onAngryDesiredButtonTapped();
            }
        });
    }

    private void onAngryDesiredButtonTapped() {
        this.mood.setDesiredMood(Mood.ANGRY);
    }

    private void setUpStressedDesiredButton(View rootView) {
        stressedDesiredButton = rootView.findViewById(R.id.stressed_mood_desired);
        stressedDesiredButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onStressedDesiredButtonTapped();
            }
        });
    }

    private void onStressedDesiredButtonTapped() {
        this.mood.setDesiredMood(Mood.STRESSED);
    }

    private void setUpSadMoodDesiredButton(View rootView) {
        sadDesiredButton = rootView.findViewById(R.id.sad_mood_desired);
        sadDesiredButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onSadDesiredButtonTapped();
            }
        });
    }

    private void onSadDesiredButtonTapped() {
        this.mood.setDesiredMood(Mood.SAD);
    }

    private void setUpHappyMoodDesiredButton(View rootView) {
        happyDesiredButton = rootView.findViewById(R.id.happy_mood_desired);
        happyDesiredButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onHappyDesiredButtonTapped();
            }

        });
    }

    private void onHappyDesiredButtonTapped() {
        this.mood.setDesiredMood(Mood.HAPPY);
    }

    private void setUpCalmCurrentButton(View rootView) {
        calmCurrentButton = rootView.findViewById(R.id.calm_mood_current);
        calmCurrentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onCalmCurrentButtonTapped();
            }
        });
    }

    private void onCalmCurrentButtonTapped() {
        this.mood.putMoodToCollection(Mood.CALM);
    }

    private void setUpStressedCurrentButton(View rootView) {
        stressedCurrentButton = rootView.findViewById(R.id.stressed_mood_current);
        stressedCurrentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onStressedCurrentButtonTapped();
            }
        });
    }

    private void onStressedCurrentButtonTapped() {
        this.mood.putMoodToCollection(Mood.STRESSED);
    }

    private void setUpCalmDesiredButton(View rootView) {
        calmDesiredButton = rootView.findViewById(R.id.calm_mood_current);
        calmDesiredButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onCalmDesiredButtonTapped();
            }
        });
    }

    private void onCalmDesiredButtonTapped() {
        this.mood.setDesiredMood(Mood.CALM);
    }

    private void setUpAngryMoodCurrentButton(View rootView) {
        angryCurrentButton = rootView.findViewById(R.id.angry_mood_current);
        angryCurrentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onAngryCurrentButtonTapped();
            }
        });
    }

    private void onAngryCurrentButtonTapped() {
        this.mood.putMoodToCollection(Mood.ANGRY);
    }

    private void setUpSadMoodCurrentButton(View rootView) {
        sadCurrentButton = rootView.findViewById(R.id.sad_mood_current);
        sadCurrentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onSadCurrentButtonTapped();
            }
        });
    }

    private void onSadCurrentButtonTapped() {
        this.mood.putMoodToCollection(Mood.SAD);
    }

    private void setUpHappyMoodCurrentButton(View rootView) {
        happyCurrentButton = rootView.findViewById(R.id.happy_mood_current);
        happyCurrentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onHappyCurrentButtonTapped();
            }
        });
    }

    private void onHappyCurrentButtonTapped() {
        this.mood.putMoodToCollection(Mood.HAPPY);
    }


}
