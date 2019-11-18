package com.example.moodmixer;

import java.util.ArrayList;
import java.util.List;

public class Playlists {

    String playlistName;

    public Playlists(String playlist){
        this.playlistName = playlist;
    }

    public String getPlaylistName(){
        return playlistName;
    }

    public static final List<Playlists> PLAYLISTS = new ArrayList<Playlists>();




}
