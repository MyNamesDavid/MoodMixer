package com.example.moodmixer;

import com.spotify.android.appremote.api.SpotifyAppRemote;

public class MoodMusicPlayer {
    private static final String CLIENT_ID = "a6d6003f62b54f1c9a3ea665f4ded656";
    private static final String REDIRECT_URI = "com.example.moodmixer://callback/";
    private SpotifyAppRemote musicPlayer;
    private Mood getCurrentMood;
    private Mood getDesiredMood;
    public void setMoodMusicPlayer(Mood getCurrentMood, Mood getDesiredMood){
        this.getCurrentMood = getCurrentMood;

        //musicPlayer.getPlayerApi().
    }
}
