package com.example.moodmixer;

import com.spotify.android.appremote.api.SpotifyAppRemote;
import android.media.AudioManager;


public class MoodMusicPlayer {
    private static final String CLIENT_ID = "a6d6003f62b54f1c9a3ea665f4ded656";
    private static final String REDIRECT_URI = "com.example.moodmixer://callback/";
    private SpotifyAppRemote musicPlayer;
    private Mood getCurrentMood = null;
    private Mood getDesiredMood = null;
    private int currentBPM;
    private int desiredBPM;
    private int currentKey;
    private int desiredKey;
    private int currentMode;
    private int desiredMode;
    private float currentEnergy;
    private float desiredEnergy;
    private float currentInstrumentalness;
    private float desiredInstrumentalness;
    float currentLiveness;
    float desiredLiveness;
    private SettingsFragment settingInfo = null;

    

    public void setMoodMusicPlayer(Mood getCurrentMood, Mood getDesiredMood){
        this.getCurrentMood = getCurrentMood;
        this.getDesiredMood = getDesiredMood;


        // set the song attributes for the
        if(this.getCurrentMood == Mood.ANGRY){
            currentBPM = 180;
            currentMode  = 0;
            currentKey = 100;
            currentEnergy = 1.0f;
        } else if (this.getCurrentMood == Mood.NERVOUS){
            currentBPM = 180;
            currentMode  = 0;
            currentKey = 100;
            currentEnergy = 0.75f;
        } else if (this.getCurrentMood == Mood.SAD){
            currentBPM = 180;
            currentMode  = 70;
            currentKey = 100;
            currentEnergy = 0.0f;
        } else if (this.getCurrentMood == Mood.STRESSED){
            currentBPM = 180;
            currentMode  = 0;
            currentKey = 100;
            currentEnergy = 0.75f;
        }

        if(this.getDesiredMood == Mood.HAPPY){
            desiredBPM = 180;
            desiredMode  = 1;
            desiredMode = 100;
        } else if (this.getDesiredMood == Mood.CALM){
            desiredBPM = 180;
            desiredMode  = 70;
            desiredKey = 100;
        } else if (this.getCurrentMood == Mood.OPTIMISTIC) {
            desiredBPM = 180;
            desiredMode = 70;
            desiredKey = 100;
        }

    }

    //public void searchSongById()
}
