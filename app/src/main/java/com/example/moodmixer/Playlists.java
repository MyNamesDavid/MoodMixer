package com.example.moodmixer;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Playlists {

    String playlistName;
    String playlistId;
    int mId = 0;

    public Playlists(String playlist, String id){
        this.playlistName = playlist;
        this.mId = mId++;
        this.playlistId = id;
    }

    public String getPlaylistName(){
        return playlistName;
    }

    public static final List<Playlists> PLAYLISTS = new ArrayList<Playlists>();

    public String getId() {
        return playlistId;
    }

}
