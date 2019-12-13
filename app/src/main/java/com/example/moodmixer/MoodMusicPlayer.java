package com.example.moodmixer;

import com.spotify.android.appremote.api.SpotifyAppRemote;
import android.media.AudioManager;


public class MoodMusicPlayer {
    private static final String CLIENT_ID = "a6d6003f62b54f1c9a3ea665f4ded656";
    private static final String REDIRECT_URI = "com.example.moodmixer://callback/";
    private SpotifyAppRemote musicPlayer;
    PreferenceManager preferenceManager;
    private Mood getCurrentMood = null;
    private Mood getDesiredMood = null;
    String currentMoodGenre;
    String desiredMoodGenre;
    private String GenreId;
    private SettingsFragment settingInfo = null;



    public void setMoodMusicPlayer(){

        getCurrentMood = Mood.valueOf(preferenceManager.getCurrentMoodSavedData());
        getDesiredMood = Mood.valueOf(preferenceManager.getDesiredMoodSavedData());

        // checks the currentMood and sets the currentMoodGenre
        if(getCurrentMood.name.equals("Happy")){
            currentMoodGenre =  preferenceManager.getHappyGenre();
        } else if(getCurrentMood.name.equals("Calm")){
            currentMoodGenre = preferenceManager.getCalmGenre();
        } else if(getCurrentMood.name.equals("Sad")){
            currentMoodGenre = preferenceManager.getSadGenre();
        } else if(getCurrentMood.name.equals("Angry")){
            currentMoodGenre = preferenceManager.getCalmGenre();
        }

        // checks the desiredMood and sets the desiredMoodGenre
        if(getDesiredMood.name.equals("Happy")){
            desiredMoodGenre =  preferenceManager.getHappyGenre();
        } else if(getDesiredMood.name.equals("Calm")){
            desiredMoodGenre = preferenceManager.getCalmGenre();
        } else if(getDesiredMood.name.equals("Sad")){
            desiredMoodGenre = preferenceManager.getSadGenre();
        } else if(getDesiredMood.name.equals("Angry")){
            desiredMoodGenre = preferenceManager.getCalmGenre();
        }


    }

    public void playMoodSongs(){
        int counter = 0;
        while(true){
            if(counter == 0){

            }
        }
    }

    public void setSongGenreId(String genre){
        
    }
}
