package com.example.moodmixer;

import android.content.Context;

import java.util.ArrayList;

public class TracksInPlaylistSingleton {
    private static TracksInPlaylistSingleton sTracklist;

    private ArrayList<Songs> mTracklist;

    public static TracksInPlaylistSingleton get(Context context) {
        if (sTracklist == null) {
            sTracklist = new TracksInPlaylistSingleton(context);
        }
        return sTracklist;
    }

    private TracksInPlaylistSingleton(Context context) {
        mTracklist = new ArrayList<>();

    }
    public void addTracklist(Songs c) {
        mTracklist.add(c);
    }

    public void clearTracklist(){
        int size = mTracklist.size();
        mTracklist.clear();
        notify();
    }

    public ArrayList<Songs> getTracklist() {
        return mTracklist;
    }
}
