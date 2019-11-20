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

    public static ArrayList<Playlists> createPlaylist(int numOfPlaylist){
        ArrayList<Playlists> PLAYLISTS = new ArrayList<Playlists>();

        for(int i = 1; i <= numOfPlaylist; i++){
            PLAYLISTS.add(new Playlists("Playlist "+ i));
        }
        return PLAYLISTS;
    }






}
