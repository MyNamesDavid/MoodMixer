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
    String songUri;
    ImageView songAlbumCover;

    String songId;


    private static Songs sSongs;

    private List<Songs> mSong;

    public Songs(String song, String artist, String uri){
        this.songName = song;
        this.songArtistName = artist;
        this.songUri = uri;
    }

    public String getID(){return songId;}
    public String getSongName(){
        return songName;
    }
    public String getGenre(){
        return songGenre;
    }
    public String getSongArtistName(){ return songArtistName; }
    public String getSongUrl() { return SongUrl; }
    public String getSongUri() {return songUri;}


    public void setSongName(String song) {
        songName = song;
    }
    public void setSongGenre(String song) {
        songGenre = song;
    }
    public void setSongArtist(String song) { songArtistName = song; }
    public void setSongUrl(String songUrl) { SongUrl = songUrl; }
    public void setSongId(String songid) { songId = songid; }
    public void setSongUri(String Uri){songUri = Uri;}




    /**
     * An array of sample (dummy) items.
     */
    public static final List<Songs> SONGS = new ArrayList<Songs>();

    /**
     * A map of sample (dummy) items, by ID.
     */
    public static final Map<String, Songs> SONGS_MAP = new HashMap<String, Songs>();




    public static void addItem(Songs item) {
        SONGS.add(item);
        SONGS_MAP.put(item.songName, item);
        SONGS_MAP.put(item.songArtistName, item);
    }


   public static Songs createSongs(String trackName, String artistName, String id) {
        Songs songs = new Songs("Song:" + trackName, "Artist" + artistName, id);
        return songs;
    }


}
