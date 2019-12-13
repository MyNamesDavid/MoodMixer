package com.example.moodmixer;
import android.content.SharedPreferences;

import android.content.Context;


enum PreferenceKey {
    LastPlayedSongName,
    LastPlayedSongArtistName,
    CurrentMoood,
    DesiredMood,
    happyGenre,
    calmGenre,
    optimisticGenre,
    sadGenre,
    angryGenre,
    stressedGenre
    // ADD KEYS HERE TO ACT AS ACCESSORS TO THE DEVICES STORAGE
}

public class PreferenceManager {

    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    PreferenceManager(Context context) {
         preferences = context.getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
    }

    public String getSongName() {
        return preferences.getString(PreferenceKey.LastPlayedSongName.toString(), "");
    }

    public String getArtistName() {
        return preferences.getString(PreferenceKey.LastPlayedSongArtistName.toString(), "");
    }

    public void setLastPlayedSongName(String songName) {
        editor = preferences.edit();
        editor.putString(PreferenceKey.LastPlayedSongName.toString(), songName);
        editor.apply();
    }

    public void setLastPlayedSongArtistName(String artistName) {
        editor = preferences.edit();
        editor.putString(PreferenceKey.LastPlayedSongArtistName.toString(), artistName);
        editor.apply();
    }

    public String getCurrentMoodSavedData(){
        return preferences.getString(PreferenceKey.CurrentMoood.toString(),"");
    }

    public String getDesiredMoodSavedData(){
        return preferences.getString(PreferenceKey.DesiredMood.toString(),"");
    }


    public String getHappyGenre(){
        return preferences.getString(PreferenceKey.happyGenre.toString(),"");
    }

    public void setCurrentMood(String currentMood){
        editor = preferences.edit();
        editor.putString(PreferenceKey.CurrentMoood.toString(), currentMood);
        editor.apply();
    }
    public void setDesiredMood(String desiredMood){
        editor = preferences.edit();
        editor.putString(PreferenceKey.DesiredMood.toString(), desiredMood);
        editor.apply();
    }

    public void setHappyGenre(String happyGenre){
        editor = preferences.edit();
        editor.putString(PreferenceKey.happyGenre.toString(), happyGenre);
        editor.apply();
    }

    public void setCalmGenre(String calmGenre){
        editor = preferences.edit();
        editor.putString(PreferenceKey.optimisticGenre.toString(), calmGenre);
        editor.apply();
    }

    public void setOptimisticGenre(String optimisticGenre){
        editor = preferences.edit();
        editor.putString(PreferenceKey.optimisticGenre.toString(), optimisticGenre);
        editor.apply();
    }

    public void setSadGenre(String sadGenre){
        editor = preferences.edit();
        editor.putString(PreferenceKey.sadGenre.toString(), sadGenre);
        editor.apply();
    }

    public void setAngryGenre(String angryGenre){
        editor = preferences.edit();
        editor.putString(PreferenceKey.angryGenre.toString(), angryGenre);
        editor.apply();
    }

    public void setStressedGenre(String stressedGenre){
        editor = preferences.edit();
        editor.putString(PreferenceKey.stressedGenre.toString(), stressedGenre);
        editor.apply();
    }
}
