package com.example.moodmixer;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Playlists {

    String playlistName;
    int mId = 0;

    public Playlists(String playlist){
        this.playlistName = playlist;
        this.mId = mId++;
    }

    public String getPlaylistName(){
        return playlistName;
    }

    public static final List<Playlists> PLAYLISTS = new ArrayList<Playlists>();

    public int getId() {
        return mId;
    }

}
