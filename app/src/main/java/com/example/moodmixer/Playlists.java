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

    public static ArrayList<Playlists> createPlaylist(int numOfPlaylist){
        ArrayList<Playlists> PLAYLISTS = new ArrayList<Playlists>();

        for(int i = 1; i <= numOfPlaylist; i++){
            PLAYLISTS.add(new Playlists("Playlist "+ i));
        }
        return PLAYLISTS;
    }



    public int getId() {
        return mId;
    }

}
