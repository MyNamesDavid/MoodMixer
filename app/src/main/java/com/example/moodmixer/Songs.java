package com.example.moodmixer;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class Songs {
    public int title;
    String songName;
    String songGenre;
    String songArtistName;
    String SongUrl;
    String songUri;
    ImageView songAlbumCover;
    String songLength;
    String songId;


    private static Songs sSongs;

    private List<Songs> mSong;

    public Songs(String song, String artist, String uri, long timeMS){
        this.songName = song;
        this.songArtistName = artist;
        this.songUri = uri;
        this.songLength = convertTime(timeMS);
       // this.songGenre = genre;
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

    public String convertTime(long timeMS){
        long timeMin = (timeMS/1000) / 60;
        long timeSec = (timeMS/1000) % 60;
        if(timeSec > 9){
            return (timeMin + ":" + timeSec);
        }else{
            return (timeMin + ":0" + timeSec);
        }
    }


}
