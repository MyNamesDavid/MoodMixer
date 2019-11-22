package com.example.moodmixer;
import android.content.SharedPreferences;

import android.content.Context;


enum PreferenceKey {
    LastPlayedSongName,
    LastPlayedSongArtistName
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
}
