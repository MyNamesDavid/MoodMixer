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
    public int title;
    String songName;
    String songGenre;
    String songArtistName;
    String SongUrl;
    ImageView songAlbumCover;

    int songId;


    private static Songs sSongs;

    private List<Songs> mSong;

    public Songs(String song, String artist){
        this.songName = song;
        this.songArtistName = artist;


    }

    public long getID(){return songId;}
    public String getSongName(){
        return songName;
    }
    public String getGenre(){
        return songGenre;
    }
    public String getSongArtistName(){ return songArtistName; }
    public String getSongUrl() { return SongUrl; }


    public void setSongName(String song) {
        songName = song;
    }
    public void setSongGenre(String song) {
        songGenre = song;
    }
    public void setSongArtist(String song) {
        songArtistName = song;
    }
    public void setSongUrl(String songUrl) { SongUrl = songUrl; }




    /**
     * An array of sample (dummy) items.
     */
    public static final List<Songs> SONGS = new ArrayList<Songs>();

    /**
     * A map of sample (dummy) items, by ID.
     */
    public static final Map<String, Songs> SONGS_MAP = new HashMap<String, Songs>();

    private static final int COUNT = 25;

    static {
        // Add some sample items.
        for (int i = 1; i <= COUNT; i++) {
            addItem(createSongs("Song Name " + i, "Artist Name " + i));

        }
    }



    private static void addItem(Songs item) {
        SONGS.add(item);
        SONGS_MAP.put(item.songName, item);
        SONGS_MAP.put(item.songArtistName, item);
    }


   private static Songs createSongs(String trackName, String artistName) {
        Songs songs = new Songs("Song:" + trackName, "Artist" + artistName);
        return songs;
    }





}
