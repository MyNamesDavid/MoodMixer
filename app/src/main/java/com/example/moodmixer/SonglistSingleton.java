package com.example.moodmixer;

import android.content.Context;

import java.util.ArrayList;

public class SonglistSingleton {
    private static SonglistSingleton sSonglist;

    private ArrayList<Songs> mSonglist;

    public static SonglistSingleton get(Context context) {
        if (sSonglist == null) {
            sSonglist = new SonglistSingleton(context);
        }
        return sSonglist;
    }

    private SonglistSingleton(Context context) {
        mSonglist = new ArrayList<>();

    }
    public void addSonglist(Songs c) {
        mSonglist.add(c);
    }

    public void clearSonglist(){
        int size = mSonglist.size();
        mSonglist.clear();
        notify();
    }

    public ArrayList<Songs> getSonglist() {
        return mSonglist;
    }
}

