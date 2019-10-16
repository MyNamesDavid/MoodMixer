package com.example.moodmixer;

import java.util.ArrayList;

public class Songs {
    String songName;
    String songGenre;

    public Songs(String song, String genre){
        this.songName = song;
        this.songGenre = genre;
    }

    public String getSongName(){
        return songName;
    }

    public String getGenre(){
        return songGenre;
    }

    public static ArrayList<Songs> getSongs(){
        ArrayList<Songs> songs = new ArrayList<Songs>();
        for(int i = 0; i < 20; i++){
            songs.add(new Songs("Generic song name " + String.valueOf(i) ,"Some genre IDK"));
        }
        return songs;
    }
}
