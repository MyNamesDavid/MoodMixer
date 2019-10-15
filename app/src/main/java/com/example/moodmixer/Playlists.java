package com.example.moodmixer;

import java.util.ArrayList;

public class Playlists {

    String playlistName;

    public Playlists(String playlist){
        this.playlistName = playlist;
    }

    public String getPlaylistName(){
        return playlistName;
    }


    public static ArrayList<Playlists> getPlaylists(){
        ArrayList<Playlists> playlists = new ArrayList<Playlists>();
        for(int i = 0; i < 20; i++){
            playlists.add(new Playlists("Generic playlist name " + String.valueOf(i)));
        }
        return playlists;
    }
}
