package com.example.moodmixer;

import android.content.ClipData;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.PopupMenu;
import android.widget.TextView;

import java.util.ArrayList;

public class SongListAdapter extends ArrayAdapter<Songs>{

    public SongListAdapter(Context context, ArrayList<Songs> songs){
        super(context, 0, songs);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        //Get data item for this position
        Songs song = getItem(position);
        //Check if an existing view is being reused else inflate view
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_song,
                    parent, false);
        }

        //Lookup view for data population
        TextView songName = (TextView) convertView.findViewById(R.id.list_item_song_name);
        TextView songGenre = (TextView) convertView.findViewById(R.id.list_item_artist_name);

        //Populate the data
        songName.setText(song.songName);
        songGenre.setText(song.songGenre);
        //Return view
        return  convertView;

    }




}
