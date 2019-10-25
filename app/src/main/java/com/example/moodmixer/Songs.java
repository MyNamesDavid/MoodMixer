package com.example.moodmixer;

import android.content.Context;
import android.graphics.Bitmap;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Songs {
    String songName;
    String songGenre;
    String songArtistName;
    Bitmap songAlbumCover;
    int id;


    private static Songs sSongs;

    private List<Songs> mSong;

    public Songs(String song, String artist){
        this.songName = song;
        this.songArtistName = artist;
    }


    public String getSongName(){
        return songName;
    }

    public String getGenre(){
        return songGenre;
    }

    public String getSongArtistName(){
        return songArtistName;
    }


    public void setSongName(String song) {
        songName = song;
    }

    public void setSongGenre(String song) {
        songGenre = song;
    }



    /**
     * An array of sample (dummy) items.
     */
    public static final List<Songs> ITEMS = new ArrayList<Songs>();

    /**
     * A map of sample (dummy) items, by ID.
     */
    public static final Map<String, Songs> ITEM_MAP = new HashMap<String, Songs>();

    private static final int COUNT = 25;

    static {
        // Add some sample items.
        for (int i = 1; i <= COUNT; i++) {
            addItem(createSongs(i));
        }
    }

    private static void addItem(Songs item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.songName, item);
        ITEM_MAP.put(item.songArtistName, item);
    }

    private static Songs createSongs(int position) {
        Songs songs = new Songs("Song" + String.valueOf(position), "Artist " + position);
        return songs;
    }


}
