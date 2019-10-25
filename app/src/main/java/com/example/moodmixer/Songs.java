package com.example.moodmixer;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Songs {
    String songName;
    String songGenre;
    String songArtistName;
    ImageView songAlbumCover;
    int songId;


    private static Songs sSongs;

    private List<Songs> mSong;

    public Songs(String song, String artist, ImageView albumCover){
        this.songName = song;
        this.songArtistName = artist;
        this.songAlbumCover = albumCover;

    }

    public long getID(){return songId;}
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
    public void setSongArtist(String song) {
        songArtistName = song;
    }



    /**
     * An array of sample (dummy) items.
     */
    public static final List<Songs> SONGS = new ArrayList<Songs>();

    /**
     * A map of sample (dummy) items, by ID.
     */
    public static final Map<String, Songs> SONGS_MAP = new HashMap<String, Songs>();

    private static final int COUNT = 25;



    private static void addItem(Songs item) {
        SONGS.add(item);
        SONGS_MAP.put(item.songName, item);
        SONGS_MAP.put(item.songArtistName, item);
    }

    private static Songs createSongs(String trackName, String artistName, ImageView album, int id) {
        Songs songs = new Songs("Song:" + trackName, "Artist" + artistName, album);
        return songs;
    }




}
