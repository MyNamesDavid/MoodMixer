package com.example.moodmixer;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PlaylistSingleton {
    private static PlaylistSingleton sPlaylist;

    private ArrayList<Playlists> mPlaylist;

    public static PlaylistSingleton get(Context context) {
        if (sPlaylist == null) {
            sPlaylist = new PlaylistSingleton(context);
        }
        return sPlaylist;
    }

    private PlaylistSingleton(Context context) {
        mPlaylist = new ArrayList<>();

    }
    public void addPlaylist(Playlists c) {
        mPlaylist.add(c);
    }

    public void clearPlaylist(){
        int size = mPlaylist.size();
        mPlaylist.clear();
        notify();
    }

    public ArrayList<Playlists> getPlaylist() {
        return mPlaylist;
    }

}
